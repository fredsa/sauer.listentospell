package sauer.listentospell.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;
import sauer.listentospell.Tuple;

import java.util.ArrayList;
import java.util.HashSet;

public class ListenToSpellApplication extends Application {
  private static final String WORDLIST = "wordlist";
  private static final String TAG = ListenToSpellApplication.class.getName();
  private SQLiteDatabase sql;

  public ArrayList<Tuple> getTupleList() {
    ArrayList<Tuple> list = new ArrayList<Tuple>();
    Cursor query = sql.query("wordlist", null, "listname = ?", new String[] {"foo"}, null, null,
        null);
    while (query.moveToNext()) {
      String word = query.getString(1);
      String senstence = query.getString(2);
      list.add(new Tuple(word, senstence));
    }
    return list;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    deleteLegacyPrefs();
    sql = new MySQLiteOpenHelper(this).getWritableDatabase();
  }

  private void deleteLegacyPrefs() {
    SharedPreferences prefs = getSharedPreferences("listtospell", Context.MODE_PRIVATE);
    prefs.edit().remove(WORDLIST).apply();
  }

  public void setTupleList(ArrayList<Tuple> list) {
    Log.d(TAG, "updateWordText(" + list + ")");

    sql.delete("wordlist", "listname = ?", new String[] {"foo"});
    HashSet<String> seen = new HashSet<String>();
    SQLiteStatement stmt = sql.compileStatement("INSERT INTO wordlist VALUES (?, ?, ?)");
    for (Tuple tuple : list) {
      if (seen.add(tuple.word)) {
        stmt.bindString(1, "foo");
        stmt.bindString(2, tuple.word);
        stmt.bindString(3, tuple.sentence);
        stmt.executeInsert();
      }
    }
  }

  public boolean isSetup() {
    return !getTupleList().isEmpty();
  }

  public ArrayList<String> getListNames() {
    ArrayList<String> list = new ArrayList<String>();
    Cursor query = sql.query("wordlist", new String[] {"listname"}, null, null, "listname", null,
        null);
    while (query.moveToNext()) {
      String listName = query.getString(0);
      list.add(listName);
    }
    return list;
  }

}
