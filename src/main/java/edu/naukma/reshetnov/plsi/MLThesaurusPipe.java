package edu.naukma.reshetnov.plsi;


import org.apache.spark.ml.Pipeline;
import org.apache.spark.ml.PipelineModel;
import org.apache.spark.ml.PipelineStage;
import org.apache.spark.ml.Transformer;
import org.apache.spark.ml.classification.LogisticRegression;
import org.apache.spark.ml.feature.HashingTF;
import org.apache.spark.ml.feature.Tokenizer;
import org.apache.spark.ml.param.ParamMap;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class MLThesaurusPipe {

    private static String SPARK_URL = "spark://Ihors-MacBook-Pro.local:7077";
    private static String INPUT_FILE_PATH =
        "/Users/reshet/Projects/thesaurus-data/eng_wikipedia_2010_300K-text/eng_wikipedia_2010_300K-sentences_short.txt";

    private static SparkSession obtainSparkSession() {
        return SparkSession
            .builder()
            .master(SPARK_URL)
            .appName("Java Spark SQL Thesaurus transformation pipe")
            //.config("spark.some.config.option", "some-value")
            .getOrCreate();
    }

    private static List<InputSentence> readInputSentences() {
        try {
            return Files.lines(Paths.get(INPUT_FILE_PATH))
                .map(line -> {
                    String[] split = line.split("\\t");
                    return new InputSentence(split[0], split[1]);
                })
                .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public static void main(String[] args) {
        System.out.println("Test Spark ML pipes");

        SparkSession spark = obtainSparkSession();
        List<InputSentence> sentences = readInputSentences();

        // Prepare training documents, which are labeled.
        Dataset<Row> training = spark.createDataFrame(sentences, InputSentence.class);


        // Configure an ML pipeline, which consists of three stages: tokenizer, hashingTF, and lr.

        Pipeline pipeline = new Pipeline()
            .setStages(new PipelineStage[] {
            //    tokenizer, hashingTF, lr
            });

        // Fit the pipeline to training documents.
        PipelineModel model = pipeline.fit(training);

        try {
            model.save("/Users/reshet/Projects/thesaurus-data/model.ml");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //
//        // Prepare test documents, which are unlabeled.
//        Dataset<Row> test = spark.createDataFrame(Arrays.asList(
//            new JavaDocument(4L, "spark i j k"),
//            new JavaDocument(5L, "l m n"),
//            new JavaDocument(6L, "mapreduce spark"),
//            new JavaDocument(7L, "apache hadoop")
//        ), JavaDocument.class);
//
//        // Make predictions on test documents.
//        Dataset<Row> predictions = model.transform(test);
//        for (Row r : predictions.select("id", "text", "features", "prediction").collectAsList()) {
//            System.out.println("(" + r.get(0) + ", " + r.get(1) + ") --> prob=" + r.get(2)
//                + ", prediction=" + r.get(3));
//        }
    }

    static class InputSentence {
        private final String id;
        private final String text;

        public InputSentence(String id, String text) {

            this.id = id;
            this.text = text;
        }

        public String getId() {
            return id;
        }

        public String getText() {
            return text;
        }
    }

//    static class PosTaggerCarniak extends Transformer {
//
//        @Override
//        public Dataset<Row> transform(Dataset<?> dataset) {
//            return null;
//        }
//    }

}
