package sauer.listentospell;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class EnterWordsActivity extends Activity {
  private static final String TAG = EnterWordsActivity.class.getName();

  private ListenToSpellApplication app;

  private int wordCount;

  private ArrayList<EditText> editText = new ArrayList<EditText>();

  private LinearLayout linearLayout;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.d(TAG, "onCreate()");

    setContentView(R.layout.enter_words);

    app = (ListenToSpellApplication) getApplication();

    Button saveButton = (Button) findViewById(R.id.word_list_save_button);
    saveButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        parseAndSave();
        startActivity(new Intent().setClass(EnterWordsActivity.this, MainActivity.class));
      }
    });

    linearLayout = (LinearLayout) findViewById(R.id.word_list_linear_layout);
    for (int i = 1; i <= 1; i++) {
      addWordRow();
    }
    Button addButton = (Button) findViewById(R.id.add_word_row_button);
    addButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View arg0) {
        addWordRow();
      }
    });

    loadAndShow();

  }

  private void addWordRow() {
    LinearLayout container = new LinearLayout(this);
    container.setOrientation(LinearLayout.HORIZONTAL);

    TextView tv = new TextView(this);
    tv.setText(Integer.toString(wordCount + 1) + " ");

    EditText et = new EditText(this);
    editText.add(et);
    et.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

    container.addView(tv);
    container.addView(et);

    linearLayout.addView(container, wordCount);

    et.requestFocus();
    wordCount++;
  }

  private void loadAndShow() {
    ArrayList<String> wordList = app.getWordList();
    Log.d(TAG, "wordList=" + wordList);
    Log.d(TAG, "editText.size() = " + editText.size());
    Log.d(TAG, "wordList.size() = " + wordList.size());
    while (editText.size() < wordList.size()) {
      addWordRow();
    }
    int i = 0;
    for (String word : wordList) {
      Log.d(TAG, i + "::::::::::" + word);
      editText.get(i).setText(word);
      i++;
    }
    // TODO remove blank spots due to duplicate words
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
    loadAndShow();
  }

  @Override
  protected void onPause() {
    super.onPause();
    Log.d(TAG, "onPause()");
    parseAndSave();
  }

  private void parseAndSave() {
    ArrayList<String> list = new ArrayList<String>();
    for (EditText et : editText) {
      String word = et.getText().toString().trim();
      if (word.length() > 0) {
        list.add(word);
      }
    }
    Log.d(TAG, "parseAndSave: " + list);
    app.updateWordText(list);

    Log.d(TAG, "..........reading back what we saved: " + app.getWordList());
  }

}