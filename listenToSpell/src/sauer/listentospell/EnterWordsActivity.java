package sauer.listentospell;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class EnterWordsActivity extends Activity {
  private static final String TAG = EnterWordsActivity.class.getName();

  private EditText wordListEditText;

  private ListenToSpellApplication listenToSpellApplication;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.d(TAG, "onCreate()");

    setContentView(R.layout.enter_words);

    wordListEditText = (EditText) findViewById(R.id.word_list);

    listenToSpellApplication = (ListenToSpellApplication) getApplication();

    loadAndShow();

    Button saveButton = (Button) findViewById(R.id.word_list_save_button);
    saveButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        parseAndSave();
        startActivity(new Intent().setClass(EnterWordsActivity.this, MainActivity.class));
      }
    });
  }

  private void loadAndShow() {
    String t = listenToSpellApplication.getWordText();
    wordListEditText.setText(t);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    Log.d(TAG, "onDestroy()");
    Log.d(TAG, "getWindow().getCurrentFocus() ^^^^^^^^^^^^^^^^^^^^^"
        + getWindow().getCurrentFocus());
  }

  @Override
  protected void onStart() {
    super.onStart();
    Log.d(TAG, "onStart()");
    Log.d(TAG, "getWindow().getCurrentFocus() ^^^^^^^^^^^^^^^^^^^^^"
        + getWindow().getCurrentFocus());
  }

  @Override
  protected void onStop() {
    super.onStart();
    Log.d(TAG, "onStop()");
    Log.d(TAG, "getWindow().getCurrentFocus() ^^^^^^^^^^^^^^^^^^^^^"
        + getWindow().getCurrentFocus());
  }

  @Override
  protected void onRestart() {
    super.onRestart();
    Log.d(TAG, "onRestart()");
    Log.d(TAG, "getWindow().getCurrentFocus() ^^^^^^^^^^^^^^^^^^^^^"
        + getWindow().getCurrentFocus());
  }

  @Override
  protected void onResume() {
    super.onResume();
    Log.d(TAG, "onResume()");
    Log.d(TAG, "getWindow().getCurrentFocus() ^^^^^^^^^^^^^^^^^^^^^"
        + getWindow().getCurrentFocus());
    loadAndShow();
  }

  @Override
  protected void onPause() {
    super.onPause();
    Log.d(TAG, "onPause()");
    Log.d(TAG, "getWindow().getCurrentFocus() ^^^^^^^^^^^^^^^^^^^^^"
        + getWindow().getCurrentFocus());
    parseAndSave();
  }

  private void parseAndSave() {
    String text = wordListEditText.getText().toString();
    Log.d(TAG, "parseAndSave(" + text + ")");
    text = listenToSpellApplication.normalize(text);
    Log.d(TAG, "normalized = " + text);
    wordListEditText.setText(text);
    listenToSpellApplication.updateWordText(text);

    Log.d(TAG, "..........reading back what we saved: " + listenToSpellApplication.getWordText());
  }

}