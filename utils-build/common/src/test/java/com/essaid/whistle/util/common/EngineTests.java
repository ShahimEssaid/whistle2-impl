package com.essaid.whistle.util.common;

import com.google.cloud.verticals.foundations.dataharmonization.data.Data;
import com.google.cloud.verticals.foundations.dataharmonization.data.NullData;
import com.google.cloud.verticals.foundations.dataharmonization.imports.ImportPath;
import com.google.cloud.verticals.foundations.dataharmonization.imports.impl.FileLoader;
import com.google.cloud.verticals.foundations.dataharmonization.init.Engine;
import com.google.cloud.verticals.foundations.dataharmonization.init.Engine.Builder;
import com.google.cloud.verticals.foundations.dataharmonization.init.Engine.InitializedBuilder;
import com.google.cloud.verticals.foundations.dataharmonization.init.initializer.ExternalConfigExtractor;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;

public class EngineTests {

  @Test
  void runEngine() throws IOException {

    Path cwd = Path.of(".").toAbsolutePath().normalize();
    Path exampleOnePath = Path.of(".", "src/test/resources/wstl/example-1.wstl").toAbsolutePath().normalize();
    ImportPath importPath = ImportPath.of(FileLoader.NAME, exampleOnePath, exampleOnePath.getParent());
    ExternalConfigExtractor externalConfigExtractor = ExternalConfigExtractor.of(importPath);

    long l = System.nanoTime();
    InitializedBuilder initialize = new Builder(externalConfigExtractor).initialize();
    long l1 = System.nanoTime();

    long l4 = l1 - l;

    Engine engine = initialize.build();
    long l2 = System.nanoTime();
    long l5 = l2 - l1;

    Engine build = initialize.build();
    long l3 = System.nanoTime();
    long l6 = l3 - l2;

    Engine build1 = initialize.build();
    long l7 = System.nanoTime();
    long l8 = l7 - l3;

    Engine build2 = initialize.build();
    long l9 = System.nanoTime();
    long l10 = l9 - l7;

    Data transform = engine.transform(NullData.instance);
    engine.close();
  }

}
