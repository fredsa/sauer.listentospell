package sauer.listentospell;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;

public class ListenToSpellApplication extends Application {
  private static final String WORDLIST = "wordlist";
  private static final String TAG = ListenToSpellApplication.class.getName();
  private SQLiteDatabase sql;

  public ArrayList<String> getWordList() {
    ArrayList<String> list = new ArrayList<String>();
    Cursor query = sql.query("wordlist", null, "listname = ?", new String[] {"foo"}, null, null,
        null);
    while (query.moveToNext()) {
      String word = query.getString(1);
      list.add(word);
    }
    return list;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    deleteLegacyPrefs();
  }

  private void deleteLegacyPrefs() {
    SharedPreferences prefs = getSharedPreferences("listtospell", Context.MODE_PRIVATE);
    prefs.edit().remove(WORDLIST).apply();
    sql = new MySQLiteOpenHelper(this).getWritableDatabase();
  }

  public void updateWordText(ArrayList<String> list) {
    Log.d(TAG, "updateWordText(" + list + ")");

    sql.delete("wordlist", "listname = ?", new String[] {"foo"});
    HashSet<String> seen = new HashSet<String>();
    SQLiteStatement stmt = sql.compileStatement("INSERT INTO wordlist VALUES (?, ?)");
    for (String word : list) {
      if (seen.add(word)) {
        stmt.bindString(1, "foo");
        stmt.bindString(2, word);
        stmt.executeInsert();
      }
    }
  }

  public boolean isSetup() {
    return !getWordList().isEmpty();
  }

}
