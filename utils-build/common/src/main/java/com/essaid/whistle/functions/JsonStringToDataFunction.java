package com.essaid.whistle.functions;

import com.google.cloud.verticals.foundations.dataharmonization.data.Data;
import com.google.cloud.verticals.foundations.dataharmonization.data.serialization.impl.JsonSerializerDeserializer;
import java.io.Serializable;
import java.util.function.Function;

public class JsonStringToDataFunction implements Function<String, Data>, Serializable {

  private JsonSerializerDeserializer serializerDeserializer = new JsonSerializerDeserializer();

  @Override
  public Data apply(String jsonString) {
    return serializerDeserializer.deserialize(jsonString);
  }
}
