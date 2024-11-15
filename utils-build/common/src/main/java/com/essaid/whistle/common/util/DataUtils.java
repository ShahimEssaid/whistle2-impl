package com.essaid.whistle.common.util;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.google.cloud.verticals.foundations.dataharmonization.data.Data;
import com.google.cloud.verticals.foundations.dataharmonization.data.NullData;
import com.google.cloud.verticals.foundations.dataharmonization.data.serialization.impl.JsonSerializerDeserializer;
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

public class DataUtils {

  public static Data readJson(Path path) {
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

  public static void writeJson(Data data, Path path, boolean prettyJson) throws IOException {
    File file = path.toFile();
    file.getParentFile().mkdirs();
    try (OutputStream fos = new FileOutputStream(file)) {
      fos.write(DataUtils.toJsonString(data, prettyJson).getBytes(UTF_8));
    }
  }

  public static String toJsonString(Data data, boolean prettyPrint) {
    if(prettyPrint) {
      return JsonSerializerDeserializer.dataToPrettyJson(data);
    }
    return JsonSerializerDeserializer.dataToJsonString(data);
  }
}
