package sauer.listentospell.app;

import java.util.ArrayList;
import java.util.HashSet;

import sauer.listentospell.R;
import sauer.listentospell.Tuple;
import android.app.Application;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.preference.PreferenceManager;
import android.util.Log;

public class ListenToSpellApplication extends Application {

  private static final String TAG = ListenToSpellApplication.class.getName();

  private static final String WORDLIST_TABLE = "wordlist";

  private SQLiteDatabase sql;
  private SharedPreferences prefs;
  private Speaker speaker;

  public ArrayList<Tuple> getTupleList(String listname) {
    ArrayList<Tuple> list = new ArrayList<Tuple>();
    Cursor query = sql.query(WORDLIST_TABLE, null, "listname = ?", new String[] {listname}, null, null,
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
    PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
    prefs = PreferenceManager.getDefaultSharedPreferences(this);
    sql = new MySQLiteOpenHelper(this).getWritableDatabase();
    speaker = new Speaker(this);
  }

  public void setTupleList(String listname, ArrayList<Tuple> list) {
    Log.d(TAG, "updateWordText(" + list + ")");

    sql.delete(WORDLIST_TABLE, "listname = ?", new String[] {listname});
    HashSet<String> seen = new HashSet<String>();
    SQLiteStatement stmt = sql.compileStatement("INSERT INTO " + WORDLIST_TABLE + " VALUES (?, ?, ?)");
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
    Cursor query = sql.query(WORDLIST_TABLE, new String[] {"listname"}, null, null, "listname", null,
        "listname DESC");
    while (query.moveToNext()) {
      String listName = query.getString(0);
      list.add(listName);
    }
    return list;
  }

  public boolean getColorCodeWords() {
    return prefs.getBoolean(getString(R.string.preference_immediate_feedback_key), true);
  }

  public boolean getSpellCorrectWords() {
    return prefs.getBoolean(getString(R.string.preference_reinforce_correct_spelling_key), true);
  }

  public Speaker getSpeaker() {
    return speaker;
  }

}
