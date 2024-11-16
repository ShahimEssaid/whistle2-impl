package com.essaid.whistle.spark;

import com.essaid.whistle.functions.DataToJsonStringFunction;
import com.essaid.whistle.functions.JsonStringToDataFunction;
import com.essaid.whistle.functions.WhistleDataTransformFunction;
import com.google.cloud.verticals.foundations.dataharmonization.init.Engine.InitializedBuilder;
import java.io.IOException;
import java.io.Serializable;
import java.util.function.Function;
import org.apache.spark.sql.api.java.UDF1;

public class WhistleUDFStringToStringFunction implements UDF1<String, String>, Serializable {
  private transient Function<String, String> transform;
  private final InitializedBuilder builder;
  private final boolean pretty;

  public WhistleUDFStringToStringFunction(InitializedBuilder builder, boolean pretty) {
    this.builder = builder;
    this.pretty = pretty;
    transform = new JsonStringToDataFunction().andThen(
        new WhistleDataTransformFunction(builder)).andThen(new DataToJsonStringFunction(pretty));
  }

  @Override
  public String call(String jsonString) throws Exception {
    return transform.apply(jsonString);
  }

  private void setTransform(){

  }
  private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
    in.defaultReadObject();
    transform = new JsonStringToDataFunction().andThen(
        new WhistleDataTransformFunction(builder)).andThen(new DataToJsonStringFunction(pretty));
    
  }

  private  static class Transform implements Function<String, String>, Serializable {



    @Override
    public String apply(String s) {
      return "";
    }
  }
}
