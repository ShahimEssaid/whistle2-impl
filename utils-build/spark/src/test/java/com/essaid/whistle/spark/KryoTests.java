package com.essaid.whistle.spark;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Registration;
import com.esotericsoftware.kryo.io.ByteBufferInput;
import com.esotericsoftware.kryo.io.ByteBufferOutput;
import com.esotericsoftware.kryo.io.Input;
import com.essaid.whistle.common.util.DataUtils;
import com.essaid.whistle.spark.utils.KryoDataUtils;
import com.google.cloud.verticals.foundations.dataharmonization.data.Container;
import com.google.cloud.verticals.foundations.dataharmonization.data.Data;
import com.google.cloud.verticals.foundations.dataharmonization.data.NullData;
import com.google.cloud.verticals.foundations.dataharmonization.data.impl.DefaultContainer;
import com.google.cloud.verticals.foundations.dataharmonization.data.impl.DefaultPrimitive;
import com.google.cloud.verticals.foundations.dataharmonization.data.serialization.impl.JsonSerializerDeserializer;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class KryoTests {

  public static Path FHIR_JSON = Path.of("tmp-inputs", "miter-synthea", "input_small",
      "Columbus656_Wisoky380_6a87ca22-3836-25e0-a853-7295d8cbc83c.json");
  ByteBufferOutput output = new ByteBufferOutput(1000, Integer.MAX_VALUE);

  @Test
  void fhirJson() throws IOException {

    String jsonString = Files.readString(FHIR_JSON);
    int length = jsonString.length();

    long now = System.currentTimeMillis();
    Data data = JsonSerializerDeserializer.jsonToData(jsonString);
    System.out.println("Parsing JSON string took: " + (System.currentTimeMillis() - now) / 1000F);

    Kryo kryo = KryoDataUtils.getKryo();
    kryo.writeClassAndObject(output, data);
    int position = output.getByteBuffer().position();

    now = System.currentTimeMillis();
    Object o = (Container) kryo.readClassAndObject(getInput());
    System.out.println("Parsing buffer took: " + (System.currentTimeMillis() - now) / 1000F);
    assertEquals(data, o);

    Container container = (Container) o;
//    container.setField("changed", new DefaultPrimitive("value"));


    assertEquals(data, container);


    now = System.currentTimeMillis();
    data = JsonSerializerDeserializer.jsonToData(jsonString);
    System.out.println("\nParsing JSON string took: " + (System.currentTimeMillis() - now) / 1000F);
    now = System.currentTimeMillis();
    o = kryo.readClassAndObject(getInput());
    System.out.println("Reading buffer took: " + (System.currentTimeMillis() - now) / 1000F);



    now = System.currentTimeMillis();
    data = JsonSerializerDeserializer.jsonToData(jsonString);
    System.out.println("\nParsing JSON string took: " + (System.currentTimeMillis() - now) / 1000F);
    now = System.currentTimeMillis();
    o = kryo.readClassAndObject(getInput());
    System.out.println("Reading buffer took: " + (System.currentTimeMillis() - now) / 1000F);

    now = System.currentTimeMillis();
    data = JsonSerializerDeserializer.jsonToData(jsonString);
    System.out.println("\nParsing JSON string took: " + (System.currentTimeMillis() - now) / 1000F);
    now = System.currentTimeMillis();
    o = kryo.readClassAndObject(getInput());
    System.out.println("Reading buffer took: " + (System.currentTimeMillis() - now) / 1000F);

    now = System.currentTimeMillis();
    data = JsonSerializerDeserializer.jsonToData(jsonString);
    System.out.println("\nParsing JSON string took: " + (System.currentTimeMillis() - now) / 1000F);
    now = System.currentTimeMillis();
    o = kryo.readClassAndObject(getInput());
    System.out.println("Reading buffer took: " + (System.currentTimeMillis() - now) / 1000F);

    now = System.currentTimeMillis();
    data = JsonSerializerDeserializer.jsonToData(jsonString);
    System.out.println("\nParsing JSON string took: " + (System.currentTimeMillis() - now) / 1000F);
    now = System.currentTimeMillis();
    o = kryo.readClassAndObject(getInput());
    System.out.println("Reading buffer took: " + (System.currentTimeMillis() - now) / 1000F);


//    System.out.println(o);
  }


  @Test
  void helloContainer() {
    Container container = new DefaultContainer();
    container.setField("null", NullData.instance);
    container.setField("hello", new DefaultPrimitive("world"));

    Kryo kryo = new Kryo();
    KryoDataUtils.setupKryo(kryo);

    kryo.writeClassAndObject(output, container);
//    kryo.writeObject(output, container);
    Object data = kryo.readClassAndObject(getInput());
    System.out.println(data);
  }

  ByteBufferInput getInput() {
    return new ByteBufferInput(output.getByteBuffer().rewind());
  }


}
