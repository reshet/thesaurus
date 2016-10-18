package edu.naukma.reshetnov.plsi;


import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.parser.charniak.CharniakParser;
import edu.stanford.nlp.trees.Tree;

import java.util.Arrays;
import java.util.stream.Collectors;

public class ParserTest {
  public static String[] carniakPath = {
    "/Users/reshet/Projects/carniak/bllip-parser/", "/Users/ihorreshetnov/mycode/bllip-parser/"
  };
  public static void main(String[] args) {

    if (args.length == 0 || args.length > 1 || Integer.parseInt(args[0]) > 2) {
      throw new IllegalArgumentException("Provide run mode: 1: home, 2: work");
    }

    final int mode = Integer.parseInt(args[0]);

    CharniakParser parser = new CharniakParser(carniakPath[mode - 1], "./parse-50best.sh");

    Tree bestParse = parser.getBestParse(
        Arrays.asList("John gave presents to his colleagues".split(" ")).stream()
            .map(Word::new)
            .collect(Collectors.toList()));
    System.out.println(bestParse);
  }
}
