package sauer.listentospell;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import java.util.ArrayList;

public class TrainActivity extends Activity {

  private static final int MY_DATA_CHECK_CODE = 42;
  private static final String TAG = TrainActivity.class.getName();
  private EditText answerEditText;
  private OnInitListener initListener;
  private TextToSpeech tts;
  private String word;
  private ListenToSpellApplication app;
  private TextView waitForTtsTextView;

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

    setContentView(R.layout.train);

    waitForTtsTextView = (TextView) findViewById(R.id.wait_for_tts);
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

    initListener = new OnInitListener() {
      @Override
      public void onInit(int status) {
        Log.e(TAG, "OnInitListener.onInit(" + status + ")");
        if (status == TextToSpeech.SUCCESS) {
          waitForTtsTextView.setVisibility(View.GONE);
          say("Spell: " + word, TextToSpeech.QUEUE_ADD);
        } else {
          Log.e(TAG, "OnInitListener.onInit(ERROR = " + status + ")");
        }
      }
    };

    checkTts();
  }

  private void chooseNewWord() {
    ArrayList<String> wordList = app.getWordList();
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
        //        log("onActivityResult() resultCode=" + resultCode + "; data=" + data);
        break;
      default:
        //        log("onActivityResult() requestCode=" + requestCode);
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
    if (word.equals(text)) {
      say(text + ". That's right.", TextToSpeech.QUEUE_FLUSH);
      for (int i = 0; i < word.length(); i++) {
        String letter = "" + word.charAt(i);
        if (letter.equals("a")) {
          letter = "eh"; // say 'eh', not 'uh'
        }
        Log.d(TAG, "letter=" + letter);
        say(letter, TextToSpeech.QUEUE_ADD);
      }
      chooseNewWord();
      say("Spell: " + word, TextToSpeech.QUEUE_ADD);
    } else {
      say(text + "? That's incorrect. Spell: " + word, TextToSpeech.QUEUE_ADD);
    }
  }

  public void onPlayClick(View view) {
    say(word, TextToSpeech.QUEUE_FLUSH);
  }

  private void say(String text, int queueMode) {
    Log.d(TAG, "******************************************** say(" + text + ")");
    tts.speak(text, queueMode, null);
  }
}