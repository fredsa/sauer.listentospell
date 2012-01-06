package sauer.listentospell.app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

class MySQLiteOpenHelper extends SQLiteOpenHelper {

  private static final int DATABASE_VERSION = 5;
  private static final String DATABASE_NAME = "listentospell";
  private static final String TAG = MySQLiteOpenHelper.class.getName();

  MySQLiteOpenHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
    Log.d(TAG, "SQL ctor version=" + DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    Log.d(TAG, "SQL onCreate()");
    db.execSQL("CREATE TABLE wordlist (listname TEXT, word TEXT, sentence TEXT);");
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    Log.d(TAG, "SQL onUpgrade() " + oldVersion + " -> " + newVersion);
    db.execSQL("DROP TABLE wordlist;");
    db.execSQL("CREATE TABLE wordlist (listname TEXT, word TEXT, sentence TEXT);");
  }

}