package com.essaid.whistle.functions;

import com.essaid.whistle.common.util.DataUtils;
import com.google.cloud.verticals.foundations.dataharmonization.data.Data;
import java.util.function.Function;

public class DataToJsonStringFunction implements Function<Data, String> {

  private final boolean pretty;

  public DataToJsonStringFunction(boolean prettyJson) {
    this.pretty = prettyJson;
  }

  @Override
  public String apply(Data data) {
    return DataUtils.toJsonString(data, pretty);
  }
}
