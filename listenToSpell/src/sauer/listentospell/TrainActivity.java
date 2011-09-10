package sauer.listentospell;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.speech.tts.TextToSpeech.OnUtteranceCompletedListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import java.util.HashMap;
import java.util.List;

public class TrainActivity extends Activity {

  private static final String CLEAR_ANSWER_EDIT_TEXT = "CLEAR_ANSWER_EDIT_TEXT";
  private static final int MY_DATA_CHECK_CODE = 42;
  private static final String TAG = TrainActivity.class.getName();
  private EditText answerEditText;
  private OnInitListener initListener;
  private TextToSpeech tts;
  private boolean ttsReady;
  private String word;
  private ListenToSpellApplication app;

  @Override
  protected void onResume() {
    super.onResume();
    setView();
    sayCurrentWord();
  }

  private void setView() {
    if (!app.isSetup()) {
      setContentView(R.layout.cannot_train);
      return;
    }

    if (!ttsReady) {
      setContentView(R.layout.initializing_tts);
      return;
    }

    setContentView(R.layout.train);

    answerEditText = (EditText) findViewById(R.id.answer_textbox);
    answerEditText.setOnEditorActionListener(new OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
          onDoneClick(v);
        }
        return true;
      }
    });

    getWindow().setSoftInputMode(0);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    tts.shutdown();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    app = (ListenToSpellApplication) getApplication();
    chooseNewWord();

    Log.d(TAG, "isSetup============" + app.isSetup());
    Log.d(TAG, "getWordText============" + app.getWordText());
    Log.d(TAG, "getWordList============" + app.getWordList().size());
    if (!app.isSetup()) {
      setContentView(R.layout.cannot_train);
      return;
    }

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
          sayCurrentWord();
        } else {
          Log.e(TAG, "OnInitListener.onInit(ERROR = " + status + ")");
        }
      }
    };

    checkTts();
  }

  private void chooseNewWord() {
    List<String> wordList = app.getWordList();
    int idx = (int) (Math.random() * wordList.size());
    word = wordList.size() == 0 ? "" : wordList.get(idx);
    Log.e(TAG, "word=" + word);
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
        // log("onActivityResult() resultCode=" + resultCode + "; data=" + data);
        break;
      default:
        // log("onActivityResult() requestCode=" + requestCode);
    }

    if (requestCode == MY_DATA_CHECK_CODE) {
      if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
        // success, create the TTS instance
        tts = new TextToSpeech(getApplicationContext(), initListener);
      } else {
        // missing data, install it
        Log.e(TAG, "Missing TTS data");
        Intent installIntent = new Intent();
        installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
        startActivity(installIntent);
      }
    }
  }

  public void onDoneClick(View view) {
    Log.d(TAG, "onDoneClick()");
    String text = answerEditText.getText().toString();
    Log.d(TAG, "word=" + word + "; answered=" + answerEditText.getText().toString());
    String say;
    if (word.equals(text)) {
      say = "That's right.";
      for (int i = 0; i < word.length(); i++) {
        String letter = "" + word.charAt(i);
        if (letter.equals("a")) {
          letter = "eh"; // say 'eh', not 'uh'
        }
        Log.d(TAG, "letter=" + letter);
        say += " " + letter;
      }
      say += " spells " + word + ".";
      chooseNewWord();
      say += "Now spell: " + word;
      say(say);
    } else {
      say = text + "? That's incorrect. Spell: " + word;
    }
    say(say, CLEAR_ANSWER_EDIT_TEXT);
    answerEditText.setText("");
  }

  private void say(String text, String utterenceId) {
    Log.d(TAG, "say(" + text + ")");
    if (!ttsReady) {
      return;
    }

    if (utterenceId != null) {
      HashMap<String, String> map = new HashMap<String, String>();
      map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, utterenceId);

      tts.speak(text, TextToSpeech.QUEUE_FLUSH, map);
    } else {
      tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }
  }

  private void sayCurrentWord() {
    say("Spell: " + word);
  }

  public void onPlayClick(View view) {
    say(word);
  }

  private void say(String text) {
    say(text, null);
  }
}