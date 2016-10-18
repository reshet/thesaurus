package edu.naukma.reshetnov.plsi;


import org.apache.spark.ml.Pipeline;
import org.apache.spark.ml.PipelineModel;
import org.apache.spark.ml.PipelineStage;
import org.apache.spark.ml.classification.LogisticRegression;
import org.apache.spark.ml.feature.HashingTF;
import org.apache.spark.ml.feature.Tokenizer;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.util.Arrays;

public class MllibPipelineExample {

    private static String SPARK_URL = "spark://Ihors-MacBook-Pro.local:7077";

    public static void main(String[] args) {
        System.out.println("Test Spark ML pipes");

        SparkSession spark = SparkSession
            .builder()
            .master(SPARK_URL)
            .appName("Java Spark SQL basic example")
            //.config("spark.some.config.option", "some-value")
            .getOrCreate();


        // Prepare training documents, which are labeled.
        Dataset<Row> training = spark.createDataFrame(Arrays.asList(
            new JavaLabeledDocument(0L, "a b c d e spark", 1.0),
            new JavaLabeledDocument(1L, "b d", 0.0),
            new JavaLabeledDocument(2L, "spark f g h", 1.0),
            new JavaLabeledDocument(3L, "hadoop mapreduce", 0.0)
        ), JavaLabeledDocument.class);

        // Configure an ML pipeline, which consists of three stages: tokenizer, hashingTF, and lr.
        Tokenizer tokenizer = new Tokenizer()
            .setInputCol("text")
            .setOutputCol("words");
        HashingTF hashingTF = new HashingTF()
            .setNumFeatures(1000)
            .setInputCol(tokenizer.getOutputCol())
            .setOutputCol("features");
        LogisticRegression lr = new LogisticRegression()
            .setMaxIter(10)
            .setRegParam(0.01);
        Pipeline pipeline = new Pipeline()
            .setStages(new PipelineStage[] {tokenizer, hashingTF, lr});

        // Fit the pipeline to training documents.
        PipelineModel model = pipeline.fit(training);

        // Prepare test documents, which are unlabeled.
        Dataset<Row> test = spark.createDataFrame(Arrays.asList(
            new JavaDocument(4L, "spark i j k"),
            new JavaDocument(5L, "l m n"),
            new JavaDocument(6L, "mapreduce spark"),
            new JavaDocument(7L, "apache hadoop")
        ), JavaDocument.class);

        // Make predictions on test documents.
        Dataset<Row> predictions = model.transform(test);
        for (Row r : predictions.select("id", "text", "features", "prediction").collectAsList()) {
            System.out.println("(" + r.get(0) + ", " + r.get(1) + ") --> prob=" + r.get(2)
                + ", prediction=" + r.get(3));
        }
    }

    public static class JavaLabeledDocument {
        private final long id;
        private final String text;
        private final double label;

        public JavaLabeledDocument(long id, String text, double label) {
            this.id = id;
            this.text = text;
            this.label = label;
        }

        public String getText() {
            return text;
        }

        public long getId() {
            return id;
        }

        public double getLabel() {
            return label;
        }
    }

    public static class JavaDocument {
        private final long id;
        private final String text;

        public JavaDocument(long id, String text) {
            this.id = id;
            this.text = text;
        }

        public String getText() {
            return text;
        }

        public long getId() {
            return id;
        }
    }
}
