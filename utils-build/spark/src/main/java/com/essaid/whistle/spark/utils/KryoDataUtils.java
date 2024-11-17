package com.essaid.whistle.spark.utils;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Registration;
import com.google.cloud.verticals.foundations.dataharmonization.data.NullData;
import com.google.cloud.verticals.foundations.dataharmonization.data.impl.DefaultPrimitive;
import org.objenesis.instantiator.ObjectInstantiator;

public class KryoDataUtils {

  public static Kryo getKryo() {
    Kryo kryo = new Kryo();
    setupKryo(kryo);
    return kryo;
  }

  public static void setupKryo(Kryo kryo) {

    // null
    NullData nullData;

    // primitive
    Registration registration = kryo.register(DefaultPrimitive.class);
    registration.setInstantiator(new ObjectInstantiator() {
      @Override
      public Object newInstance() {
        return new DefaultPrimitive(false);
      }
    });


    registration = kryo.register(NullData.class);
    registration.setInstantiator(new ObjectInstantiator() {

      @Override
      public Object newInstance() {
        return NullData.instance;
      }
    });


  }

}
