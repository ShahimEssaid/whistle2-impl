package com.essaid.whistle.spark.utils;

import static org.apache.spark.sql.functions.udf;

import com.essaid.whistle.spark.WhistleUDFStringToStringFunction;
import com.google.cloud.verticals.foundations.dataharmonization.init.Engine.InitializedBuilder;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.expressions.UserDefinedFunction;
import org.apache.spark.sql.types.DataTypes;

public class SparkUtils {

  public static UserDefinedFunction createWhistleUDF(InitializedBuilder builder,
      boolean prettyJson) {
    UserDefinedFunction udf = udf(new WhistleUDFStringToStringFunction(builder, prettyJson),
        DataTypes.StringType);
    return udf;
  }

  public static void registerWhistleUDF(SparkSession spark, InitializedBuilder builder,
      boolean prettyJson) {
    spark.udf().register("whistle", new WhistleUDFStringToStringFunction(builder, prettyJson),
        DataTypes.StringType);
  }

}
