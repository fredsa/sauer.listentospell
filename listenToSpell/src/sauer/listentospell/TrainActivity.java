package sauer.listentospell;

import sauer.listentospell.app.ListenToSpellApplication;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.speech.tts.TextToSpeech.OnUtteranceCompletedListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class TrainActivity extends Activity {

  private static final Random random = new Random();
  private static final String CLEAR_ANSWER_EDIT_TEXT = "CLEAR_ANSWER_EDIT_TEXT";
  private static final int MY_DATA_CHECK_CODE = 42;
  private static final String TAG = TrainActivity.class.getName();
  private EditText answerEditText;
  private OnInitListener initListener;
  private TextToSpeech tts;
  private boolean ttsReady;
  private Tuple tuple;
  private ListenToSpellApplication app;
  private TextView trainTestStatus;
  private ArrayList<Tuple> allWords;
  private ArrayList<Tuple> remainingTuples;

  private TextWatcher textWatcher = new TextWatcher() {
    @Override
    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
      partialAnswer = arg0.toString();
    }

    @Override
    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
    }

    @Override
    public void afterTextChanged(Editable arg0) {
      colorAnswerEditText();
    }
  };
  private String partialAnswer;

  @Override
  protected void onResume() {
    super.onResume();
    setView();
  }

  protected void colorAnswerEditText() {
    String answer = answerEditText.getText().toString();
    int color = Color.BLACK;
    if (tuple.word.equals(answer)) {
      color = Color.GREEN;
    } else if (!tuple.word.startsWith(answer)) {
      color = Color.RED;
    }
    answerEditText.setTextColor(color);
  }

  private void setView() {
    if (!ttsReady) {
      setContentView(R.layout.initializing_tts);
      return;
    }

    setContentView(R.layout.train);

    answerEditText = (EditText) findViewById(R.id.answer_textbox);
    answerEditText.setText(partialAnswer);
    answerEditText.setOnEditorActionListener(new OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        Log.d(TAG, "onEditorAction() v=" + v + "; actionId=" + actionId + "; event=" + event);
        if (event == null || event.getAction() == KeyEvent.ACTION_DOWN) {
          onDoneClick(v);
        }
        return true;
      }
    });
    answerEditText.addTextChangedListener(textWatcher);
    trainTestStatus = (TextView) findViewById(R.id.train_test_status);
  }

  private void nextWord() {
    int cnt = remainingTuples.size();
    if (cnt == 0) {
      sayNext("Great job!");
      Intent intent = new Intent().setClass(this, MainActivity.class);
      startActivity(intent);
      return;
    }
    boolean firstWord = remainingTuples.size() == allWords.size();
    tuple = remainingTuples.remove(random.nextInt(cnt));
    String say = firstWord ? "Spell" : "Now spell";
    say += ": " + tuple.word;
    sayNext(say);
    if (tuple.sentence.length() > 0) {
      sayNext(tuple.sentence);
      sayNext(tuple.word);
    }
    setStatus();
  }

  private void setStatus() {
    trainTestStatus.setText(1 + remainingTuples.size() + "/" + allWords.size() + " words remaining");
    Log.d(TAG, "tuple=" + tuple + "; " + remainingTuples.size() + " tuple(s): " + remainingTuples);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    tts.shutdown();
  }

  @Override
  public void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.d(TAG, "savedInstanceState=" + savedInstanceState);

    app = (ListenToSpellApplication) getApplication();

    initListener = new OnInitListener() {
      @Override
      public void onInit(int status) {
        Log.e(TAG, "OnInitListener.onInit(" + status + ")");
        if (status == TextToSpeech.SUCCESS) {
          ttsReady = true;

          tts.setOnUtteranceCompletedListener(new OnUtteranceCompletedListener() {
            @Override
            public void onUtteranceCompleted(String utteranceId) {
              Log.d(TAG, "onUtteranceCompleted(" + utteranceId + ")");
              if (CLEAR_ANSWER_EDIT_TEXT.equals(utteranceId)) {
                //                answerEditText.setText("");
              }
            }
          });

          setView();
          nextWord();
        } else {
          Log.e(TAG, "OnInitListener.onInit(ERROR = " + status + ")");
        }
      }
    };

    Log.d(TAG, "initWordLists()...");
    initWordLists();

    Log.d(TAG, "checkTts()...");
    // May pause current activity
    checkTts();
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    Log.d(TAG, "onSaveInstanceState()");
  }

  private void initWordLists() {
    allWords = app.getTupleList();
    remainingTuples = new ArrayList<Tuple>(allWords);
  }

  private void checkTts() {
    Intent checkIntent = new Intent();
    checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
    startActivityForResult(checkIntent, MY_DATA_CHECK_CODE);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    switch (requestCode) {
      case MY_DATA_CHECK_CODE:
        Log.d(TAG, "onActivityResult() resultCode=" + resultCode + "; data=" + data);
        if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
          tts = new TextToSpeech(getApplicationContext(), initListener);
        } else {
          // missing data, install it
          Log.e(TAG, "Missing TTS data; resultCode=" + resultCode);
          Intent installIntent = new Intent();
          installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
          startActivity(installIntent);
        }
        break;
      default:
        Log.d(TAG, "onActivityResult() requestCode=" + requestCode);
    }
  }

  public void onDoneClick(View view) {
    Log.d(TAG, "onDoneClick()");
    String text = answerEditText.getText().toString();
    Log.d(TAG, "tuple=" + tuple + "; answered=" + answerEditText.getText().toString());
    if (tuple.word.equals(text)) {
      thatIsCorrect();
    } else {
      thatIsIncorrect(text);
    }
    answerEditText.setText("");
  }

  private void thatIsIncorrect(String text) {
    String say = text + "? That's incorrect. Spell: " + tuple.word;
    sayNow(say, CLEAR_ANSWER_EDIT_TEXT);
  }

  private void thatIsCorrect() {
    sayNow("That's right", null);
    for (int i = 0; i < tuple.word.length(); i++) {
      String letter = "" + tuple.word.charAt(i);
      Log.d(TAG, "letter=" + letter);
      String say = pronounce(letter);
      sayNext(say);
    }
    sayNext(" spells " + tuple.word + ".");
    nextWord();
  }

  private String pronounce(String letter) {
    if (letter.equals("a")) {
      letter = "eh";
    } else if (letter.equals(" ")) {
      letter = "space";
    } else if (letter.equals("'")) {
      letter = "apostrophe";
    }
    return letter;
  }

  private void sayNext(String text) {
    say(text, TextToSpeech.QUEUE_ADD, null);
  }

  private void sayNow(String text, String utterenceId) {
    say(text, TextToSpeech.QUEUE_FLUSH, utterenceId);
  }

  private void say(String text, int queueMode, String utterenceId) {
    Log.d(TAG, "say(" + text + ")");
    if (!ttsReady) {
      return;
    }

    if (utterenceId != null) {
      HashMap<String, String> map = new HashMap<String, String>();
      map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, utterenceId);

      tts.speak(text, queueMode, map);
    } else {
      tts.speak(text, queueMode, null);
    }
  }

  public void onRepeatClick(View view) {
    sayNow(tuple.word, null);
    sayNext(tuple.sentence);
    sayNext(tuple.word);
  }
}