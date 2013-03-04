package sauer.listentospell;

import java.util.ArrayList;
import java.util.Random;

import sauer.listentospell.app.ListenToSpellApplication;
import sauer.listentospell.app.NextTask;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class TrainActivity extends SpeechActivity {

  private static final Random random = new Random();
  private static final String TAG = TrainActivity.class.getName();
  private EditText answerEditText;
  private Tuple tuple;
  private ListenToSpellApplication app;
  private TextView trainTestStatus;
  private ArrayList<Tuple> allWords;
  private ArrayList<Tuple> remainingTuples;

  private TextWatcher textWatcher = new TextWatcher() {
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
      partialAnswer = s.toString();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void afterTextChanged(Editable s) {
      colorAnswerEditText();
    }
  };

  private String partialAnswer;
  private String listName;

  protected void colorAnswerEditText() {
    String answer = answerEditText.getText().toString();
    int color = Color.BLACK;
    if (app.getColorCodeWords()) {
      assert tuple != null;
      assert tuple.word != null;
      if (tuple.word.equals(answer)) {
        color = Color.GREEN;
      } else if (!tuple.word.startsWith(answer)) {
        color = Color.RED;
      }
    }
    answerEditText.setTextColor(color);
  }

  private void nextWord() {
    int cnt = remainingTuples.size();
    if (cnt == 0) {
      app.getSpeaker().say("Great job!", new NextTask() {
        @Override
        protected void onPostExecute(Void result) {
          Intent intent = new Intent().setClass(app, MainActivity.class);
          startActivity(intent);
        }
      });
      return;
    }
    boolean firstWord = remainingTuples.size() == allWords.size();
    tuple = remainingTuples.remove(random.nextInt(cnt));
    String say = firstWord ? "Spell" : "Now spell";
    say += ": " + tuple.word;
    app.getSpeaker().say(say);
    if (tuple.sentence.length() > 0) {
      app.getSpeaker().say(tuple.sentence);
      app.getSpeaker().say(tuple.word);
    }
    setStatus();
  }

  private void setStatus() {
    String status = getString(R.string.train_test_status, 1 + remainingTuples.size(),
        allWords.size());
    trainTestStatus.setText(status);
  }

  @Override
  public void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.d(TAG, "savedInstanceState=" + savedInstanceState);

    app = (ListenToSpellApplication) getApplication();

    listName = getIntent().getStringExtra("listName");

    Log.d(TAG, "initWordLists()...");
    initWordLists(listName);

    setContentView(R.layout.activity_train);

    answerEditText = (EditText) findViewById(R.id.answer_textbox);
    answerEditText.setText(partialAnswer);
    answerEditText.setOnEditorActionListener(new OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        // Log.d(TAG, "onEditorAction() v=" + v + "; actionId=" + actionId + "; event=" + event);
        if (event == null || event.getAction() == KeyEvent.ACTION_DOWN) {
          onDoneClick(v);
        }
        return true;
      }
    });
    answerEditText.addTextChangedListener(textWatcher);
    answerEditText.addTextChangedListener(new EnforceSingleWordTextWatcher());

    trainTestStatus = (TextView) findViewById(R.id.train_test_status);
    nextWord();
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    Log.d(TAG, "onSaveInstanceState()");
  }

  private void initWordLists(String listName) {
    allWords = app.getTupleList(listName);
    remainingTuples = new ArrayList<Tuple>(allWords);
  }

  public void onDoneClick(View view) {
    String text = answerEditText.getText().toString();
    if (tuple.word.equals(text)) {
      thatIsCorrect();
    } else {
      thatIsIncorrect(text);
    }
  }

  private void thatIsIncorrect(String text) {

    app.getSpeaker().say(text + "? That's incorrect.", new NextTask() {
      @Override
      protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        answerEditText.setText("");
      }
    });
    app.getSpeaker().say("Spell: " + tuple.word);
  }

  private void thatIsCorrect() {
    app.getSpeaker().say("That's right");
    if (!app.getSpellCorrectWords()) {
      answerEditText.setText("");
      nextWord();
      return;
    }
    for (int i = 0; i < tuple.word.length(); i++) {
      String letter = "" + tuple.word.charAt(i);
      app.getSpeaker().say(letter);
    }
    app.getSpeaker().say(" spells " + tuple.word + ".", new NextTask() {
      @Override
      protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        answerEditText.setText("");
        nextWord();
      }
    });
  }

  public void onRepeatClick(View view) {
    app.getSpeaker().sayNow(tuple.word);
    if (tuple.sentence.length() > 0) {
      app.getSpeaker().say(tuple.sentence);
      app.getSpeaker().say(tuple.word);
    }
  }
}