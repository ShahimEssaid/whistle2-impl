package com.essaid.whistle.spark;

import com.essaid.whistle.functions.DataToJsonStringFunction;
import com.essaid.whistle.functions.JsonStringToDataFunction;
import com.essaid.whistle.functions.WhistleDataTransformFunction;
import com.google.cloud.verticals.foundations.dataharmonization.init.Engine.InitializedBuilder;
import java.util.function.Function;
import org.apache.spark.sql.api.java.UDF1;

public class WhistleUDFStringToStringFunction implements UDF1<String, String> {
  private final Function<String, String> transform;

  public WhistleUDFStringToStringFunction(InitializedBuilder builder, boolean pretty) {
    transform = new JsonStringToDataFunction().andThen(
        new WhistleDataTransformFunction(builder)).andThen(new DataToJsonStringFunction(pretty));
  }

  @Override
  public String call(String jsonString) throws Exception {
    return transform.apply(jsonString);
  }
}
