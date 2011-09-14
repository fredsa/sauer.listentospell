package sauer.listentospell;

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
  private String word;
  private ListenToSpellApplication app;
  private TextView trainTestStatus;
  private ArrayList<String> allWords;
  private ArrayList<String> remainingWords;

  private TextWatcher textWatcher = new TextWatcher() {

    @Override
    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
    }

    @Override
    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
    }

    @Override
    public void afterTextChanged(Editable arg0) {
      colorAnswerEditText();
    }
  };

  @Override
  protected void onResume() {
    super.onResume();
    setView();
  }

  protected void colorAnswerEditText() {
    String answer = answerEditText.getText().toString();
    int color = Color.BLACK;
    if (word.equals(answer)) {
      color = Color.GREEN;
    } else if (!word.startsWith(answer)) {
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
    //    answerEditText.requestFocus();
  }

  private void nextWord() {
    int cnt = remainingWords.size();
    if (cnt == 0) {
      sayNext("Great job!");
      Intent intent = new Intent().setClass(this, MainActivity.class);
      startActivity(intent);
      return;
    }
    boolean firstWord = remainingWords.size() == allWords.size();
    word = remainingWords.remove(random.nextInt(cnt));
    String say = firstWord ? "Spell" : "Now spell";
    say += ": " + word;
    sayNext(say);
    setStatus();
  }

  private void setStatus() {
    trainTestStatus.setText(1 + remainingWords.size() + "/" + allWords.size() + " words remaining");
    Log.d(TAG, "word=" + word + "; " + remainingWords.size() + " word(s): " + remainingWords);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    tts.shutdown();
  }

  @Override
  public void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

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
          if (savedInstanceState == null) {
            nextWord();
          }
          sayCurrentWord();
        } else {
          Log.e(TAG, "OnInitListener.onInit(ERROR = " + status + ")");
        }
      }
    };

    if (savedInstanceState != null) {
      allWords = savedInstanceState.getStringArrayList("allWords");
      remainingWords = savedInstanceState.getStringArrayList("remainingWords");
      word = savedInstanceState.getString("word");
    } else {
      initWordLists();
    }

    checkTts();
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putStringArrayList("allWords", allWords);
    outState.putStringArrayList("remainingWords", remainingWords);
    outState.putString("word", word);
    Log.d(TAG, "onSaveInstanceState()");
  }

  private void initWordLists() {
    allWords = app.getWordList();
    remainingWords = new ArrayList<String>(allWords);
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
    Log.d(TAG, "word=" + word + "; answered=" + answerEditText.getText().toString());
    if (word.equals(text)) {
      thatIsCorrect();
    } else {
      thatIsIncorrect(text);
    }
    answerEditText.setText("");
  }

  private void thatIsIncorrect(String text) {
    String say = text + "? That's incorrect. Spell: " + word;
    sayNow(say, CLEAR_ANSWER_EDIT_TEXT);
  }

  private void thatIsCorrect() {
    sayNext("That's right");
    for (int i = 0; i < word.length(); i++) {
      String letter = "" + word.charAt(i);
      Log.d(TAG, "letter=" + letter);
      String say = pronounce(letter);
      sayNext(say);
    }
    sayNext(" spells " + word + ".");
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

  private void sayCurrentWord() {
    sayNext("Spell: " + word);
  }

  public void onPlayClick(View view) {
    sayNext(word);
  }
}