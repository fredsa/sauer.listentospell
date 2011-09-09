package sauer.listentospell;

import android.content.Context;
import android.content.SharedPreferences;

public class Persisted {

  private final SharedPreferences prefs;

  private Persisted(SharedPreferences prefs) {
    this.prefs = prefs;
  }

  public final String[] toWords(String text) {
    return text.split("[^a-zA-Z-]");
  }

  public String normalize(String text) {
    String[] words = toWords(text);

    StringBuilder t = new StringBuilder();
    for (String word : words) {
      t.append(word).append("\n");
    }
    return t.toString();
  }

  public String[] getWordList() {
    String t = getWords();
    String[] wordList = toWords(t);
    return wordList;
  }

  public String getWords() {
    return prefs.getString("wordlist", "");
  }

  public static Persisted get(Context ctx) {
    SharedPreferences prefs = ctx.getSharedPreferences("listtospell", Context.MODE_PRIVATE);
    return new Persisted(prefs);
  }

  public void updateWords(String text) {
    prefs.edit().putString("wordlist", text).apply();
  }

}
