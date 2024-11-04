package com.essaid.whistle.common.concurrent;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.google.cloud.verticals.foundations.dataharmonization.data.Data;
import com.google.cloud.verticals.foundations.dataharmonization.data.NullData;
import com.google.cloud.verticals.foundations.dataharmonization.data.serialization.impl.JsonSerializerDeserializer;
import com.google.cloud.verticals.foundations.dataharmonization.init.Engine;
import com.google.cloud.verticals.foundations.dataharmonization.init.Engine.InitializedBuilder;
import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.concurrent.Callable;

public class Transform implements Runnable {

  private final InitializedBuilder builder;
  private final Path inputPath;
  private final Path outputPath;
  private Data inputData;
  private Data outputData;

  private long timeStart;
  private long timeEngineBuilt;
  private long timeInputData;
  private long timeOutputData;
  private long timeJsonOut;

  public Transform(Path inputPath, Path outputPath, InitializedBuilder builder) {
    this.inputPath = inputPath;
    this.outputPath = outputPath;
    this.builder = builder;
  }

  private static Data readJson(Path path) {
    File file = path.toFile();
    try (FileInputStream fis = new FileInputStream(file)) {
      byte[] json = ByteStreams.toByteArray(fis);
      return new JsonSerializerDeserializer().deserialize(json);
    } catch (IOException e) {
      System.err.printf("Unable to read file %s%n", path);
      e.printStackTrace(System.err);
      return NullData.instance;
    }
  }

  private static String prettyPrintToJson(Data data) {
    byte[] json = new JsonSerializerDeserializer().serialize(data);
    Gson prettyPrinter = new GsonBuilder().setPrettyPrinting().create();
    return prettyPrinter.toJson(prettyPrinter.fromJson(new String(json, UTF_8), JsonElement.class));
  }


  @Override
  public void run() {
    this.timeStart = System.currentTimeMillis();
    try (Engine engine = builder.build()) {
      this.timeEngineBuilt = System.currentTimeMillis();
      Data inputData1 = getInputData();
      this.timeInputData = System.currentTimeMillis();
      outputData = engine.transform(getInputData());
      this.timeOutputData = System.currentTimeMillis();
      try {
        writeJson();
        this.timeJsonOut = System.currentTimeMillis();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  public Data getInputData() {
    if (inputData == null) {
      inputData = readJson(inputPath);
    }
    return inputData;
  }

  private void writeJson() throws IOException {
    if (outputPath != null) {
      File file = outputPath.toFile();
      file.getParentFile().mkdirs();
      try (OutputStream fos = new FileOutputStream(file)) {
        fos.write(prettyPrintToJson(outputData).getBytes(UTF_8));
      }
    } else {
      System.out.write(prettyPrintToJson(outputData).getBytes(UTF_8));
    }
  }


}
