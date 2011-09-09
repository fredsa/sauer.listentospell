package sauer.listentospell;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Arrays;

public class ListenToSpellApplication extends Application {
  private static final String WORDLIST = "wordlist";
  private SharedPreferences prefs;

  public ArrayList<String> getWordList() {
    return split(getWordText());
  }

  public String getWordText() {
    return prefs.getString(WORDLIST, "");
  }

  public String normalize(String text) {
    ArrayList<String> words = split(text);

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

  private String normalize(ArrayList<String> words) {
    StringBuilder t = new StringBuilder();
    for (String word : words) {
      t.append(word).append("\n");
    }
    return t.toString();
  }

  private final ArrayList<String> split(String text) {
    String[] arr = text.split("[^a-zA-Z-]");
    if (arr.length == 1 && text.equals(arr[0])) {
      return new ArrayList<String>();
    }
    return (ArrayList<String>) Arrays.asList(arr);
  }

  public boolean isSetup() {
    return !getWordList().isEmpty();
  }

}
