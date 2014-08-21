package sauer.listentospell;

import java.util.ArrayList;

public class Tuple {

  public final String sentence;
  public final String word;

  public Tuple(String word, String sentence) {
    this.word = word;
    this.sentence = sentence;
  }

  public Tuple(String t) {
    int pos = t.indexOf("|");
    word = t.substring(0, pos);
    sentence = t.substring(pos + 1);
  }

  @Override
  public String toString() {
    return word + "|" + sentence;
  }

  public static ArrayList<String> toString(ArrayList<Tuple> tuples) {
    ArrayList<String> list = new ArrayList<String>();
    for (Tuple tuple : tuples) {
      list.add(tuple.toString());
    }
    return list;
  }

  public static ArrayList<Tuple> toTuples(ArrayList<String> list) {
    ArrayList<Tuple> tuples = new ArrayList<Tuple>();
    for (String t : list) {
      tuples.add(new Tuple(t));
    }
    return tuples;
  }
}
