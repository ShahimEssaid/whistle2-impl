package com.essaid.whistle.functions;

import com.google.cloud.verticals.foundations.dataharmonization.data.Data;
import com.google.cloud.verticals.foundations.dataharmonization.init.Engine;
import com.google.cloud.verticals.foundations.dataharmonization.init.Engine.InitializedBuilder;
import java.util.function.Function;

public class WhistleDataTransformFunction implements Function<Data, Data> {

  private final InitializedBuilder builder;

  public WhistleDataTransformFunction(InitializedBuilder builder){
    this.builder = builder;
  }

  @Override
  public Data apply(Data data) {
    try(Engine engine = builder.build()){
      return engine.transform(data);
    }
  }
}
