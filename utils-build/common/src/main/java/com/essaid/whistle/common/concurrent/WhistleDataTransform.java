package com.essaid.whistle.common.concurrent;

import com.google.cloud.verticals.foundations.dataharmonization.data.Data;
import com.google.cloud.verticals.foundations.dataharmonization.init.Engine;
import com.google.cloud.verticals.foundations.dataharmonization.init.Engine.InitializedBuilder;

public class WhistleDataTransform implements Runnable {

  private final InitializedBuilder builder;
  private final Data inputData;
  private Data outputData;

  private long timeStart;
  private long timeEngineBuilt;
  private long timeInputData;
  private long timeOutputData;
  private long timeJsonOut;

  public WhistleDataTransform(Data inputData, InitializedBuilder builder) {
    this.inputData = inputData;
    this.builder = builder;
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
    }
  }

  public Data getInputData() {
    return inputData;
  }

  public Data getOutputData() {
    return outputData;
  }

}
