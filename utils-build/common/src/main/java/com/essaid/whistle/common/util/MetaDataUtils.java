package com.essaid.whistle.common.util;

import com.google.cloud.verticals.foundations.dataharmonization.function.context.impl.DefaultMetaData;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class MetaDataUtils {

  public static DefaultMetaData createDefaultEmptyMetaData() {
    return createDefaultMetaData(new HashMap<>(), new HashMap<>());
  }

  public static DefaultMetaData createDefaultMetaData(
      Map<String, Object> metadata, Map<String, Serializable> serializableMetadata) {
    return new DefaultMetaData(metadata, serializableMetadata);
  }


}
