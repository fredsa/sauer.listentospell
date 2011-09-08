package sauer.listentospell;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class TrainActivity extends Activity {
  private static final int MY_DATA_CHECK_CODE = 42;
  private EditText answerEditText;
  private EditText logArea;
  private OnInitListener initListener;
  private TextToSpeech tts;
  private String word = "restaurant";

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.train);
    answerEditText = (EditText) findViewById(R.id.answer_textbox);
    answerEditText.setOnEditorActionListener(new OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        // log("actionId=" + actionId + "; event=" + event + "; TextView=" + v);
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
          onDoneClick(v);
        }
        return true;
      }
    });
    logArea = (EditText) findViewById(R.id.log_area);

    initListener = new OnInitListener() {
      @Override
      public void onInit(int status) {
        log("OnInitListener.onInit(" + status + ")");
        if (status == TextToSpeech.SUCCESS) {
          tts.speak("Spell: " + word, TextToSpeech.QUEUE_ADD, null);
        } else {
          log("OnInitListener.onInit(ERROR = " + status + ")");
        }
      }
    };

    checkTts();

    //    tts = new TextToSpeech(this, initListener);
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
        log("onActivityResult() resultCode=" + resultCode + "; data=" + data);
        break;
      default:
        log("onActivityResult() requestCode=" + requestCode);
    }

    if (requestCode == MY_DATA_CHECK_CODE) {
      if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
        // success, create the TTS instance
        log("success...");
        //        tts = new TextToSpeech(this, initListener);
        tts = new TextToSpeech(getApplicationContext(), initListener);
        log("...success");
      } else {
        // missing data, install it
        log("missing data");
        Intent installIntent = new Intent();
        installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
        startActivity(installIntent);
      }
    }
  }

  public void onDoneClick(View view) {
    startActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER).setComponent(
        new ComponentName("sauer.listentospell", "sauer.listentospell.HelloTabWidget")));
    String text = answerEditText.getText().toString();
    if (word.equals(text)) {
      tts.speak(text + ". That's right.", TextToSpeech.QUEUE_FLUSH, null);
      for (int i = 0; i < word.length(); i++) {
        String letter = "" + word.charAt(i);
        if (letter.equals("a")) {
          letter = "eh"; // say 'eh', not 'uh'
        }
        tts.speak(letter, TextToSpeech.QUEUE_ADD, null);
      }
    } else {
      tts.speak(text + "? That's incorrect. Spell: " + word, TextToSpeech.QUEUE_ADD, null);
    }
  }

  protected void log(String text) {
    logArea.setText(text + "\n" + logArea.getText());
  }

  public void onPlayClick(View view) {
    tts.speak(word, TextToSpeech.QUEUE_FLUSH, null);
  }
}