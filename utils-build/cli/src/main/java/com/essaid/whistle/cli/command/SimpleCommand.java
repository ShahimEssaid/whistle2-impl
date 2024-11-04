package com.essaid.whistle.cli.command;

import com.essaid.whistle.common.concurrent.Transform;
import com.google.cloud.verticals.foundations.dataharmonization.imports.ImportPath;
import com.google.cloud.verticals.foundations.dataharmonization.imports.impl.FileLoader;
import com.google.cloud.verticals.foundations.dataharmonization.init.Engine.Builder;
import com.google.cloud.verticals.foundations.dataharmonization.init.Engine.InitializedBuilder;
import com.google.cloud.verticals.foundations.dataharmonization.init.initializer.ExternalConfigExtractor;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.stream.Stream;

import picocli.CommandLine.ArgGroup;
import picocli.CommandLine.Command;
import picocli.CommandLine.Help.Visibility;
import picocli.CommandLine.Option;

@Command(name = "simple", description = "A simple CLI matching the existing Whistle Main CLI."
        , mixinStandardHelpOptions = true
)
public class SimpleCommand implements Callable<Void> {

    private static final String STDOUT = "stdout";

    @Option(names = {"-m", "--main"}, description = "The entry Whistle file.")
    Path main;

    @Option(names = {"--mock"}, description = "The mock Whistle files.")
    List<Path> mocks = new ArrayList<>();

    @Option(names = {"-i", "--input"}, description = "One or more JSON file paths.")
    List<Path> inputs = new ArrayList<>();

    @Option(names = {"-o", "--output"}, description = "The output directory")
    Path output;

    @Option(names = {"--time-prefix"}, description = "Prefix output files with a timestamp. Default "
            + "false.")
    boolean timePrefix;

    @Option(names = {"--time-suffix"}, description = "Suffix output files with a timestamp. Default "
            + "false.")
    boolean timeSuffix;

    @Option(names = {"--cores"}, description = "How many cores to use? Defaults to 1. Set 0 to use "
            + "all available cores")
    int cores = 1;

    @ArgGroup(exclusive = false, multiplicity = "0..1", heading = "Input directory, pattern, and "
            + "output directory.\n")
    List<DirectoryAndPattern> directoryAndPatterns;


    private String timePrefixString = "";
    private String timeSuffixString = "";
    //  @Option(names = {"-id", "--input-dir"}, description = "A directory path to load files from.  "
    //      + "See: https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/nio/file/FileSystem.html#getPathMatcher(java.lang.String)")
    private InitializedBuilder initializedBuilder;
    private Date now;
    private ThreadPoolExecutor executor;
    private LinkedBlockingQueue<Runnable> executorQueue;
    private ExecutorCompletionService<Transform> completion;


    @Override
    public Void call() throws Exception {

        long start = System.currentTimeMillis();

        validate();

        ImportPath importPath = ImportPath.of(FileLoader.NAME, main, main.getParent());
        Builder builder = new Builder(ExternalConfigExtractor.of(importPath));

        for (Path mock : mocks) {
            ImportPath mockImportPath = ImportPath.of(FileLoader.NAME, mock, mock.getParent());
            builder.addMock(ExternalConfigExtractor.of(mockImportPath));
        }

        initializedBuilder = builder.initialize();

        for (Path input : inputs) {
            Path outputPath = output.resolve(input.getFileName().toString().replace(".json", ".output"
                    + ".json"));
            transform(input, outputPath);
        }

        if (directoryAndPatterns == null) {
            return null;
        }

        try {
            for (DirectoryAndPattern dap : directoryAndPatterns) {
                PathMatcher pathMatcher = getMatcher(dap.inputDirectory, dap.pattern);
                try (Stream<Path> walk = Files.walk(dap.inputDirectory)) {
                    walk.filter(path -> Files.isRegularFile(path) && pathMatcher.matches(path))
                            .forEach(path -> {
                                Path relative = dap.inputDirectory.relativize(path);
                                try {
                                    transform(path, geteOutputPath(dap.outputDirectory, relative));
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            });
                }
            }

            while (!this.executorQueue.isEmpty()) {
                System.out.println("Waiting to finish, task count: " + executorQueue.size());
                Thread.sleep(100);
            }

            executor.shutdown();
            executor.awaitTermination(10, TimeUnit.MINUTES);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executor.shutdownNow();
        }

        System.out.println("=========   CLI finished in seconds: " + ((System.currentTimeMillis() - start) / 1000F));
        return null;
    }

    private void validate() {
        boolean errors = false;

        this.now = new Date();

        if (timePrefix) {
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy.MM.dd_HH.mm.ss_");
            this.timePrefixString = sdf1.format(now);
        }

        if (timeSuffix) {
            SimpleDateFormat sdf1 = new SimpleDateFormat("_yyyy.MM.dd_HH.mm.ss");
            this.timeSuffixString = sdf1.format(now);
        }

        if (main == null) {
            warn("-m can't be null");
            errors = true;
        } else {
            main = main.toAbsolutePath();
            if (!Files.exists(main)) {
                warn("-m " + main + " does not exist");
                errors = true;
            }
        }

        List<Path> mocks = new ArrayList<>();
        for (Path mock : this.mocks) {
            mock = mock.toAbsolutePath();
            if (!Files.exists(mock)) {
                warn("Mock file does not exist, skipping: " + mock);
            } else {
                mocks.add(mock);
            }
        }
        this.mocks = mocks;

        List<Path> paths = new ArrayList<>();
        for (Path input : inputs) {
            Path absolutePath = input.toAbsolutePath();
            if (!Files.exists(absolutePath)) {
                warn("Intput file -i " + absolutePath + " does not exist. Skipping");

            } else {
                paths.add(absolutePath);
            }
        }
        this.inputs = paths;

        if (output != null) {
            output = output.toAbsolutePath();
            try {
                Files.createDirectories(output);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        if (directoryAndPatterns != null) {
            for (DirectoryAndPattern dap : directoryAndPatterns) {
                dap.inputDirectory = dap.inputDirectory.toAbsolutePath();
                if (dap.outputDirectory != null) {
                    dap.outputDirectory = dap.outputDirectory.toAbsolutePath();
                }
                if (!dap.pattern.startsWith("glob:") && !dap.pattern.startsWith("regex:")) {
                    warn("Pattern must start with glob: or regex: " + dap.pattern);
                    errors = true;
                }
            }
        }

        cores = cores == 0 ? Runtime.getRuntime().availableProcessors() : cores;
        this.executorQueue = new LinkedBlockingQueue<Runnable>(10);
        this.executor = new ThreadPoolExecutor(cores, cores, 1, TimeUnit.SECONDS, executorQueue);
//    this.completion = new ExecutorCompletionService<Transform>(executor);

        if (errors) {
            System.out.print("Errors found, exiting 1");
            System.exit(1);
        }

    }

    private void warn(String warn) {
        System.out.print(warn);
    }

    private PathMatcher getMatcher(Path basePath, String matchPattern) {
        FileSystem fs = FileSystems.getDefault();
        if (matchPattern.toLowerCase().startsWith("glob:")) {
            matchPattern = matchPattern.substring(5);

            String matchString = "glob:" + basePath.toString() + fs.getSeparator() + matchPattern;
            if (File.separatorChar == '\\') {
                matchString = matchString.replaceAll("/", "\\\\");
                matchString = matchString.replaceAll("\\\\", "\\\\\\\\");
            }

            PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher(matchString);
            return pathMatcher;
        } else if (matchPattern.toLowerCase().startsWith("regex:")) {
            matchPattern = matchPattern.substring(6);

            String matchString = "regex:" + basePath.toString() + fs.getSeparator() + matchPattern;
            if (File.separatorChar == '\\') {
                matchString = matchString.replaceAll("/", "\\\\");
                matchString = matchString.replaceAll("\\\\", "\\\\\\\\");
            }

            PathMatcher pathMatcher = FileSystems.getDefault()
                    .getPathMatcher(matchString);
            return pathMatcher;
        } else {
            throw new IllegalArgumentException("Unsupported match pattern: " + matchPattern);
        }

    }

    private Path geteOutputPath(Path outputBase, Path relativePath) {
        if (outputBase == null) {
            return null;
        }

        String fileName = timePrefixString + relativePath.getFileName().toString();
        fileName = fileName.replace(".json", ".output" + timeSuffixString + ".json");
        Path outputParent = outputBase.resolve(relativePath).getParent();
        return outputParent.resolve(fileName);
    }

    void transform(Path inputPath, Path outputPath) throws IOException {
        long l = System.currentTimeMillis();
        Transform transform = new Transform(inputPath, outputPath, initializedBuilder);

        while (executor.getQueue().size() > 5) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        this.executor.execute(transform);
    }

    static class DirectoryAndPattern {

        @Option(names = {"-id", "--input-dir"}, description = "Directory path.", required = true)
        Path inputDirectory;

        @Option(names = {"-idp",
                "--input-dir-pattern"}, defaultValue = "glob:*.json", showDefaultValue = Visibility.ALWAYS)
        String pattern;

        @Option(names = {"-od", "--output-dir"}, description = "Directory path.", required = true)
        Path outputDirectory;

    }
}
