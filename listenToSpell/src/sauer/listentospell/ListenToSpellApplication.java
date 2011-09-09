package sauer.listentospell;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class ListenToSpellApplication extends Application {
  private static final String WORDLIST = "wordlist";
  private SharedPreferences prefs;

  public String[] getWordList() {
    return split(getWordText());
  }

  public String getWordText() {
    return prefs.getString(WORDLIST, "");
  }

  public String normalize(String text) {
    String[] words = split(text);

    String t = normalize(words);
    return t;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    prefs = getSharedPreferences("listtospell", Context.MODE_PRIVATE);
  }

  public void updateWords(String text) {
    prefs.edit().putString(WORDLIST, text).apply();
  }

  private String normalize(String[] words) {
    StringBuilder t = new StringBuilder();
    for (String word : words) {
      t.append(word).append("\n");
    }
    return t.toString();
  }

  private final String[] split(String text) {
    return text.split("[^a-zA-Z-]");
  }

}
