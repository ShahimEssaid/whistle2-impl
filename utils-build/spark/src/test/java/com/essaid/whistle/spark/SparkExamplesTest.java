package com.essaid.whistle.spark;

import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SparkExamplesTest {

  public static  String JSON_DIR = "tmp-miter-synthea";


  public static SparkSession getSession(){
    SparkSession session = SparkSession.builder()
        .appName("JSOn dataset")
        .master("local")
        .getOrCreate();

    return  session;
  }

  public static Dataset<Row> loadJson(){
    SparkSession session = getSession();
    SparkContext sparkContext = session.sparkContext();
    JavaSparkContext javaSparkContext = new JavaSparkContext(sparkContext);
    return null;
  }

  @Test
  void hello(){
    loadJson();
  }

}
