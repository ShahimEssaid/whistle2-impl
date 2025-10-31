package com.essaid.whistle.spark;

import com.essaid.whistle.spark.utils.SparkUtils;
import com.google.cloud.verticals.foundations.dataharmonization.imports.ImportPath;
import com.google.cloud.verticals.foundations.dataharmonization.imports.impl.FileLoader;
import com.google.cloud.verticals.foundations.dataharmonization.init.Engine;
import com.google.cloud.verticals.foundations.dataharmonization.init.Engine.Builder;
import com.google.cloud.verticals.foundations.dataharmonization.init.Engine.InitializedBuilder;
import com.google.cloud.verticals.foundations.dataharmonization.init.initializer.ExternalConfigExtractor;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.rdd.RDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoder;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import scala.Tuple2;

import static org.junit.jupiter.api.Assertions.*;

public class SparkExamplesTest {

  public static  String JSON_DIR = "tmp-inputs/miter-synthea/input_small/*.json";


  public static SparkSession getSession(){

    SparkConf conf = new SparkConf().setAppName("SparkExamplesTest")
        .setMaster("local");

    SparkSession session = SparkSession.builder().config(conf)
        .getOrCreate();

    return  session;
  }

  public static Dataset<Row> loadJson(){
    SparkSession session = getSession();

    SparkContext context = session.sparkContext();
    context.setLogLevel("INFO");
    JavaSparkContext javaSparkContext = new JavaSparkContext(context);
    JavaPairRDD<String, String> pairRDD = javaSparkContext.wholeTextFiles(JSON_DIR);
    Dataset<Tuple2<String, String>> tuple2Dataset = session.createDataset(JavaPairRDD.toRDD(pairRDD),
        Encoders.tuple(Encoders.STRING(), Encoders.STRING()));
    Dataset<Row> df = tuple2Dataset.toDF("path", "inJson");
    return df;
  }

  public static InitializedBuilder getBuilder() throws IOException {
    Path mainPath = Path.of("src", "test", "resources", "whistle", "main.wstl");
    ImportPath importPath = ImportPath.of(FileLoader.NAME,mainPath, mainPath.getParent());
    Builder builder = new Builder(ExternalConfigExtractor.of(importPath));
    InitializedBuilder initialized = builder.initialize();
    return initialized;

  }

  @Test
  @Disabled
  void hello(){
    loadJson();
  }

  @Test
  @Disabled
  void whistleRun() throws IOException {
    Dataset<Row> inData = loadJson();
    Dataset<Row> outData = null;

    try(SparkSession session = inData.sparkSession()){
      SparkContext sparkContext = session.sparkContext();
      SparkUtils.registerWhistleUDF(session, getBuilder(), true);
     outData = inData.withColumn("outJson",
          functions.call_udf("whistle", functions.col("inJson")));

      Object o = outData.collect();
     // System.out.println(o);

      outData.foreach(row -> {
        //System.out.println(row.toString());
      });
    }





  }

}
