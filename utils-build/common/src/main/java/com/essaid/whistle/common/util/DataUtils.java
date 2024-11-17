package com.essaid.whistle.common.util;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.google.cloud.verticals.foundations.dataharmonization.data.Data;
import com.google.cloud.verticals.foundations.dataharmonization.data.serialization.impl.JsonSerializerDeserializer;
import com.google.common.io.ByteStreams;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;

public class DataUtils {

  public static byte[] readJsonFile(Path path) {
    File file = path.toFile();
    try (FileInputStream fis = new FileInputStream(file)) {
      return ByteStreams.toByteArray(fis);
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static Data jsonBytesToData(byte[] jsonBytes) {
    return JsonSerializerDeserializer.jsonToData(jsonBytes);
  }

  public static Data readJson(Path path) {
    byte[] bytes = readJsonFile(path);
    return new JsonSerializerDeserializer().deserialize(bytes);
  }

  public static void writeJson(Data data, Path path, boolean prettyJson) throws IOException {
    File file = path.toFile();
    file.getParentFile().mkdirs();
    try (OutputStream fos = new FileOutputStream(file)) {
      fos.write(DataUtils.toJsonString(data, prettyJson).getBytes(UTF_8));
    }
  }

  public static String toJsonString(Data data, boolean prettyPrint) {
    if (prettyPrint) {
      return JsonSerializerDeserializer.dataToPrettyJson(data);
    }
    return JsonSerializerDeserializer.dataToJsonString(data);
  }
}
