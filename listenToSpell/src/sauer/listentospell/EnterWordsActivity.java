package sauer.listentospell;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class EnterWordsActivity extends Activity {
  private static final String TAG = EnterWordsActivity.class.getName();

  private ListenToSpellApplication app;

  private ArrayList<EditText> wordEditTextList = new ArrayList<EditText>();
  private ArrayList<EditText> descriptionEditTextList = new ArrayList<EditText>();

  private LinearLayout linearLayout;

  private Button addButton;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.d(TAG, "onCreate()");

    setContentView(R.layout.enter_words);
    addButton = (Button) findViewById(R.id.add_word_row_button);

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
      addWordRow(true);
    }
    addButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View arg0) {
        addWordRow(true);
      }
    });

    loadAndShow();

  }

  private void addWordRow(boolean requestFocus) {
    final int index = wordEditTextList.size();

    final LinearLayout masterLinearLayout = new LinearLayout(this);
    masterLinearLayout.setOrientation(LinearLayout.VERTICAL);

    final LinearLayout rowOneLinearLayout = new LinearLayout(this);
    rowOneLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

    final EditText wordEditText = new EditText(this);
    wordEditTextList.add(wordEditText);

    final EditText descriptionEditText = new EditText(this);
    descriptionEditTextList.add(descriptionEditText);

    wordEditText.setSingleLine();
    wordEditText.setWidth(300);

    descriptionEditText.setSingleLine();

    Button deleteButton = new Button(this);
    deleteButton.setText("delete");
    deleteButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        wordEditText.setText("");
        descriptionEditText.setText("");
        for (int i = 0; i < wordEditTextList.size(); i++) {
          updateHints(wordEditTextList.get(i), descriptionEditTextList.get(i), i);
        }
      }
    });

    rowOneLinearLayout.addView(wordEditText);
    rowOneLinearLayout.addView(deleteButton);

    masterLinearLayout.addView(rowOneLinearLayout);
    masterLinearLayout.addView(descriptionEditText);

    linearLayout.addView(masterLinearLayout);

    wordEditText.addTextChangedListener(new TextWatcher() {
      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        updateHints(wordEditText, descriptionEditText, index);
        Log.d(TAG, "index=" + index + "; size()=" + wordEditTextList.size());
        if (index == wordEditTextList.size() - 1 && wordEditText.getText().toString().length() > 0) {
          addWordRow(false);
        }
      }

      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      }

      @Override
      public void afterTextChanged(Editable s) {
      }
    });

    updateHints(wordEditText, descriptionEditText, index);

    if (requestFocus) {
      wordEditText.requestFocus();
    }
  }

  private void updateHints(final EditText wordEditText, final EditText descriptionEditText,
      int number) {
    String word = wordEditText.getText().toString();
    if (word.length() > 0) {
      descriptionEditText.setHint("Enter a brief sentence using '" + word + "' (optional)");
    } else {
      String w = "word #" + (number + 1);
      wordEditText.setHint(w);
      descriptionEditText.setHint("Enter a brief sentence using " + w + " (optional)");
    }
  }

  private void loadAndShow() {
    ArrayList<String> wordList = app.getWordList();
    Log.d(TAG, "wordList=" + wordList);
    Log.d(TAG, "wordEditTextList.size() = " + wordEditTextList.size());
    Log.d(TAG, "wordList.size() = " + wordList.size());
    while (wordEditTextList.size() < wordList.size()) {
      addWordRow(true);
    }
    int i = 0;
    for (String word : wordList) {
      Log.d(TAG, i + "::::::::::" + word);
      wordEditTextList.get(i).setText(word);
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
    for (EditText et : wordEditTextList) {
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