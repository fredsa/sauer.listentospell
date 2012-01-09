package sauer.listentospell;

import java.util.ArrayList;

import sauer.listentospell.app.ListenToSpellApplication;
import android.content.Intent;
import android.graphics.LightingColorFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WordListActivity extends SpeechActivity {
  private static final String TAG = WordListActivity.class.getName();

  private ListenToSpellApplication app;

  private ArrayList<EditText> wordEditTextList = new ArrayList<EditText>();
  private ArrayList<EditText> sentenceEditTextList = new ArrayList<EditText>();

  private LinearLayout linearLayout;

  private String listName;

  private TextView wordListNameEditView;

  private OnFocusChangeListener speakOnFocusLostListener = new OnFocusChangeListener() {
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
      if (!hasFocus) {
        sayNow(((EditText) v).getText().toString());
      }
    }
  };

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.d(TAG, "onCreate()");

    listName = getIntent().getStringExtra("listName");
    Log.d(TAG, "getIntent().getStringExtra(\"listName\") = " + listName);

    setContentView(R.layout.word_list);

    wordListNameEditView = (TextView) findViewById(R.id.word_list_name);
    wordListNameEditView.setText(listName);

    app = (ListenToSpellApplication) getApplication();

    Button saveButton = (Button) findViewById(R.id.word_list_save_button);
    saveButton.getBackground().setColorFilter(new LightingColorFilter(0x00000000, 0xFF348017));
    saveButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        parseAndSave();
        startActivity(new Intent().setClass(WordListActivity.this, MainActivity.class));
      }
    });

    linearLayout = (LinearLayout) findViewById(R.id.word_list_linear_layout);
    for (int i = 1; i <= 1; i++) {
      addWordRow(true);
    }

    loadAndShow(listName);
  }

  private void addWordRow(boolean requestFocus) {
    View wordSentenceItem = View.inflate(this, R.layout.word_sentence_item, null);
    linearLayout.addView(wordSentenceItem);

    final int index = wordEditTextList.size();

    final EditText wordEditText = (EditText) wordSentenceItem.findViewById(R.id.word_edit_text);
    wordEditTextList.add(wordEditText);

    final EditText sentenceEditText = (EditText) wordSentenceItem.findViewById(R.id.sentence_edit_text);
    sentenceEditTextList.add(sentenceEditText);

    Button deleteButton = (Button) wordSentenceItem.findViewById(R.id.delete_button);
    deleteButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        wordEditText.setText("");
        sentenceEditText.setText("");
        for (int i = 0; i < wordEditTextList.size(); i++) {
          updateHints(wordEditTextList.get(i), sentenceEditTextList.get(i), i);
        }
      }
    });

    wordEditText.setOnFocusChangeListener(speakOnFocusLostListener);
    sentenceEditText.setOnFocusChangeListener(speakOnFocusLostListener);

    wordEditText.addTextChangedListener(new EnforceSingleWordTextWatcher());

    wordEditText.addTextChangedListener(new TextWatcher() {
      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        updateHints(wordEditText, sentenceEditText, index);
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

    updateHints(wordEditText, sentenceEditText, index);

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

  private void loadAndShow(String listname) {
    ArrayList<Tuple> tupleList = app.getTupleList(listname);
    if (tupleList.isEmpty()) {

      //      tupleList = new ArrayList<Tuple>();
      //      tupleList.add(new Tuple("fox", "The quick brown fox jumped over the lazy dog"));
      //      tupleList.add(new Tuple("wet", "The dog got all wet in the rain"));
      //      tupleList.add(new Tuple("nut", "Did you neat the nut I left on the table?"));
      //      tupleList.add(new Tuple("job", "Nice job!"));
      //      tupleList.add(new Tuple("leg", "I have a left leg and a right one"));
      //      tupleList.add(new Tuple("fun", "Have fun at school tomorrow!"));
      //      tupleList.add(new Tuple("went", "I went home for dinner"));
      //      tupleList.add(new Tuple("mop", "We need a mop to help cleanup"));
      //      tupleList.add(new Tuple("hug", "Give your brother a big hug"));
      //      tupleList.add(new Tuple("from", "I got a letter from grandma"));
      //      tupleList.add(new Tuple("any", "Did you see any penguins?"));
      //      tupleList.add(new Tuple("of", "What do you think of that?"));

      //      tupleList = new ArrayList<Tuple>();
      //      tupleList.add(new Tuple("cocoon", "Caterpillars use cocoons to turn into butterflies"));
      //      tupleList.add(new Tuple("tissue", "I need a tissue because I'm going to sneeze"));
      //      tupleList.add(new Tuple("excuse", "I need to excuse myself to go to the bathroom"));
      //      tupleList.add(new Tuple("brew", "Let's brew some tea"));
      //      tupleList.add(new Tuple("confuse", "Do not confuse him"));
      //      tupleList.add(new Tuple("bruise", "I got a bruise in tennis"));
      //      tupleList.add(new Tuple("pollute", "Do not pollute the seas"));
      //      tupleList.add(new Tuple("stew", "I would like some stew to eat"));
      //      tupleList.add(new Tuple("lose", "Be careful not to lose that gum ball"));
      //      tupleList.add(new Tuple("loose", "I have a loose tooth"));
      //      tupleList.add(new Tuple("chute", "Go down the chute"));
      //      tupleList.add(new Tuple("suit", "Nice suit"));
      //      tupleList.add(new Tuple("fuel", "The car needs fuel to run"));
      //      tupleList.add(new Tuple("proof", "I have proof that he's innocent"));
      //      tupleList.add(new Tuple("prove", "I can prove that this answer is correct"));
      //      tupleList.add(new Tuple("whose", "Whose ice cream sandwich is that?"));
      //      tupleList.add(new Tuple("who's", "Who's going to eat that ice cream sandwich?"));
      //      tupleList.add(new Tuple("school", "I had fun at school today"));
      //      tupleList.add(new Tuple("include", "I want to include my brother in this game"));
      //      tupleList.add(new Tuple("scoop", "Let's have a scoop of ice cream"));
    }
    Log.d(TAG, "wordList=" + tupleList);
    Log.d(TAG, "wordEditTextList.size() = " + wordEditTextList.size());
    Log.d(TAG, "wordList.size() = " + tupleList.size());
    while (wordEditTextList.size() < tupleList.size()) {
      addWordRow(false);
    }
    int i = 0;
    for (Tuple tuple : tupleList) {
      Log.d(TAG, i + "::::::::::" + tuple);
      wordEditTextList.get(i).setText(tuple.word);
      sentenceEditTextList.get(i).setText(tuple.sentence);
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
    loadAndShow(listName);
  }

  @Override
  protected void onPause() {
    super.onPause();
    Log.d(TAG, "onPause()");
    parseAndSave();
  }

  private void parseAndSave() {
    ArrayList<Tuple> list = new ArrayList<Tuple>();
    for (int i = 0; i < wordEditTextList.size(); i++) {
      String word = wordEditTextList.get(i).getText().toString().trim();
      String sentence = sentenceEditTextList.get(i).getText().toString().trim();
      if (word.length() > 0) {
        list.add(new Tuple(word, sentence));
      }
    }
    Log.d(TAG, "parseAndSave: " + list);
    app.setTupleList(listName, new ArrayList<Tuple>());
    listName = wordListNameEditView.getText().toString();
    app.setTupleList(listName, list);

    Log.d(TAG, "..........reading back what we saved: " + app.getTupleList(listName));
  }

}