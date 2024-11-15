package com.essaid.whistle.spark;

import java.util.List;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.rdd.RDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.junit.jupiter.api.Test;
import scala.Tuple2;

import static org.junit.jupiter.api.Assertions.*;

public class SparkExamplesTest {

  public static  String JSON_DIR = "tmp-inputs/miter-synthea/input_small/*.json";


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
    JavaPairRDD<String, String> stringStringJavaPairRDD =
        javaSparkContext.wholeTextFiles(JSON_DIR);

    RDD<Tuple2<String, String>> tuple2RDD = sparkContext.wholeTextFiles(null, 0);

    JavaRDD<String> map = stringStringJavaPairRDD.map(v1 -> {
      System.out.println(v1);
      return v1.toString();
    });

    List<String> collect = map.collect();
    System.out.println(map);
    return null;
  }

  @Test
  void hello(){
    loadJson();
  }

  @Test
  void whistleRun(){

    SparkSession sparkSession = getSession();
    SparkContext sparkContext = sparkSession.sparkContext();

  }

}
