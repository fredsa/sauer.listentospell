package sauer.listentospell.app;

import java.util.ArrayList;
import java.util.HashSet;

import sauer.listentospell.Tuple;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class ListenToSpellApplication extends Application {
  private static final String TAG = ListenToSpellApplication.class.getName();

  private static final String COLOR_CODE_WORDS = "COLOR_CODE_WORDS";
  private static final String SPELL_CORRECT_WORDS = "SPELL_CORRECT_WORDS";
  private static final String WORDLIST = "wordlist";

  private SQLiteDatabase sql;
  private SharedPreferences prefs;

  public ArrayList<Tuple> getTupleList(String listname) {
    ArrayList<Tuple> list = new ArrayList<Tuple>();
    Cursor query = sql.query(WORDLIST, null, "listname = ?", new String[] {listname}, null, null,
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
    prefs = getSharedPreferences("listtospell", Context.MODE_PRIVATE);
    sql = new MySQLiteOpenHelper(this).getWritableDatabase();
  }

  public void setTupleList(String listname, ArrayList<Tuple> list) {
    Log.d(TAG, "updateWordText(" + list + ")");

    sql.delete(WORDLIST, "listname = ?", new String[] {listname});
    HashSet<String> seen = new HashSet<String>();
    SQLiteStatement stmt = sql.compileStatement("INSERT INTO " + WORDLIST + " VALUES (?, ?, ?)");
    for (Tuple tuple : list) {
      if (seen.add(tuple.word)) {
        stmt.bindString(1, listname);
        stmt.bindString(2, tuple.word);
        stmt.bindString(3, tuple.sentence);
        stmt.executeInsert();
      }
    }
  }

  public boolean isSetup() {
    return !getListNames().isEmpty();
  }

  public ArrayList<String> getListNames() {
    ArrayList<String> list = new ArrayList<String>();
    Cursor query = sql.query(WORDLIST, new String[] {"listname"}, null, null, null, null,
        "listname DESC");
    while (query.moveToNext()) {
      String listName = query.getString(0);
      list.add(listName);
    }
    return list;
  }

  public boolean getColorCodeWords() {
    return prefs.getBoolean(COLOR_CODE_WORDS, true);
  }

  public void setColorCodeWords(boolean colorCodeWords) {
    prefs.edit().putBoolean(COLOR_CODE_WORDS, colorCodeWords).apply();
  }

  public boolean getSpellCorrectWords() {
    return prefs.getBoolean(SPELL_CORRECT_WORDS, true);
  }

  public void setSpellCorrectWords(boolean spellCorrectWords) {
    prefs.edit().putBoolean(SPELL_CORRECT_WORDS, spellCorrectWords).apply();
  }

}
