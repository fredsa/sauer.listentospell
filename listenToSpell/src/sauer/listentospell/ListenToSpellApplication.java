package sauer.listentospell;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class ListenToSpellApplication extends Application {
  private static final String WORDLIST = "wordlist";
  private static final String TAG = ListenToSpellApplication.class.getName();
  private SharedPreferences prefs;

  public ArrayList<String> getWordList() {
    return split(getWordText());
  }

  public String getWordText() {
    return prefs.getString(WORDLIST, "");
  }

  private String normalize(String text) {
    Log.d(TAG, "normalize(" + text + ")");
    List<String> words = split(text);
    Log.d(TAG, "words = " + words.size());

    String t = normalize(words);
    return t;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    prefs = getSharedPreferences("listtospell", Context.MODE_PRIVATE);
  }

  public void updateWordText(String text) {
    text = normalize(text);
    Log.d(TAG, "updateWordText(" + text + ")");
    prefs.edit().putString(WORDLIST, text).apply();
  }

  private String normalize(List<String> words) {
    HashSet<String> seen = new HashSet<String>();
    Log.d(TAG, "normalize(" + words.size() + ")");
    StringBuilder t = new StringBuilder();
    for (String word : words) {
      if (seen.add(word)) {
        t.append(word).append("\n");
      }
    }
    return t.toString();
  }

  private final ArrayList<String> split(String text) {
    String[] arr = text.trim().split("[^a-zA-Z-']");
    if (arr.length == 1 && arr[0].length() == 0) {
      return new ArrayList<String>();
    }
    return new ArrayList<String>(Arrays.asList(arr));
  }

  public boolean isSetup() {
    return !getWordList().isEmpty();
  }

}
