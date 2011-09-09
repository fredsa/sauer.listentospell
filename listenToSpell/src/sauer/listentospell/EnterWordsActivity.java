package sauer.listentospell;

import android.app.Activity;
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
    setContentView(R.layout.enter_words);

    wordListEditText = (EditText) findViewById(R.id.word_list);

    listenToSpellApplication = (ListenToSpellApplication) getApplication();

    String t = listenToSpellApplication.getWordText();
    wordListEditText.setText(t);

    Button saveButton = (Button) findViewById(R.id.word_list_save_button);
    saveButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        parseAndSave();
      }
    });
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    Log.d(TAG, "onDestroy()");
  }

  @Override
  protected void onStart() {
    super.onStart();
    Log.d(TAG, "onStart()");
  }

  @Override
  protected void onStop() {
    super.onStart();
    Log.d(TAG, "onStop()");
  }

  @Override
  protected void onRestart() {
    super.onRestart();
    Log.d(TAG, "onRestart()");
  }

  @Override
  protected void onResume() {
    super.onResume();
    Log.d(TAG, "onResume()");
  }

  @Override
  protected void onPause() {
    super.onPause();
    Log.d(TAG, "onPause()");
    parseAndSave();
  }

  private void parseAndSave() {
    String text = wordListEditText.getText().toString();
    Log.d(TAG, "parseAndSave(" + text + ")");
    text = listenToSpellApplication.normalize(text);
    wordListEditText.setText(text);
    listenToSpellApplication.updateWords(text);
  }

}