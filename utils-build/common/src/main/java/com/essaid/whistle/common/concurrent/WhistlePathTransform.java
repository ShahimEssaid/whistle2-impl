package com.essaid.whistle.common.concurrent;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.essaid.whistle.common.util.DataUtils;
import com.google.cloud.verticals.foundations.dataharmonization.data.Data;
import com.google.cloud.verticals.foundations.dataharmonization.init.Engine;
import com.google.cloud.verticals.foundations.dataharmonization.init.Engine.InitializedBuilder;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;

public class WhistlePathTransform implements Runnable {

  private final InitializedBuilder builder;
  private final Path inputPath;
  private final Path outputPath;
  private final boolean prettyJson;
  private  WhistleDataTransform dataTransform;
//  private Data inputData;
//  private Data outputData;

  private long timeStart;
  private long timeEngineBuilt;
  private long timeInputData;
  private long timeOutputData;
  private long timeJsonOut;

  public WhistlePathTransform(Path inputPath, Path outputPath, InitializedBuilder builder,
      boolean prettyJson) {
    this.inputPath = inputPath;
    this.outputPath = outputPath;
    this.builder = builder;
    this.prettyJson = prettyJson;
  }

  @Override
  public void run() {
    this.timeStart = System.currentTimeMillis();
    this.dataTransform = new WhistleDataTransform(DataUtils.readJson(inputPath), builder);
    dataTransform.run();

    try {
      DataUtils.writeJson(dataTransform.getOutputData(), outputPath, prettyJson);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    this.timeJsonOut = System.currentTimeMillis();
  }

  public Data getInputData() {
    return dataTransform == null? null: dataTransform.getInputData();
  }

  public Data getOutputData() {
    return dataTransform == null? null: dataTransform.getOutputData();
  }

}
