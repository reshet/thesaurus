package edu.naukma.reshetnov.plsi;


import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.parser.charniak.CharniakParser;
import edu.stanford.nlp.trees.Tree;

import java.util.Arrays;
import java.util.stream.Collectors;

public class ParserTest {
  public static void main(String[] args) {
    CharniakParser parser = new CharniakParser("/Users/reshet/Projects/carniak/bllip-parser/", "./parse-50best.sh");

    Tree bestParse = parser.getBestParse(
        Arrays.asList("John gave presents to his colleagues".split(" ")).stream()
            .map(Word::new)
            .collect(Collectors.toList()));
    System.out.println(bestParse);
  }
}
