package sauer.listentospell;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;

public abstract class SpeechActivity extends Activity {

  private static final String TAG = SpeechActivity.class.getName();
  private static final int MY_DATA_CHECK_CODE = 42;
  private OnInitListener initListener;
  private TextToSpeech tts;
  private boolean ttsReady;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setVolumeControlStream(AudioManager.STREAM_MUSIC);

    initListener = new OnInitListener() {
      @Override
      public void onInit(int status) {
        Log.i(TAG, "OnInitListener.onInit(" + status + ")");
        if (status == TextToSpeech.SUCCESS) {
          ttsReady = true;
          onTtsReady();
        } else {
          Log.e(TAG, "OnInitListener.onInit(ERROR = " + status + ")");
        }
      }
    };

    Log.d(TAG, "checkTts()...");
    // May pause current activity
    checkTts();
  }

  protected void onTtsReady() {
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


  @Override
  protected void onDestroy() {
    super.onDestroy();
    tts.shutdown();
  }

  protected void sayNext(String text) {
    say(text, TextToSpeech.QUEUE_ADD, null);
  }

  protected void sayNow(String text) {
    say(text, TextToSpeech.QUEUE_FLUSH, null);
  }

  private void say(String text, int queueMode, String utterenceId) {
    Log.d(TAG, "say(" + text + ")");
    if (!ttsReady) {
      return;
    }

    HashMap<String, String> map = new HashMap<String, String>();
    if (utterenceId != null) {
      map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, utterenceId);
    }
    map.put(/* TextToSpeech.Engine.KEY_FEATURE_NETWORK_SYNTHESIS */"networkTts",
        Boolean.TRUE.toString());

    tts.speak(text, queueMode, map);
  }


}