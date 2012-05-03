package sauer.listentospell;

import java.util.HashMap;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;

public abstract class SpeechActivity extends Activity {

  private final String TAG = SpeechActivity.class.getName() + ":" + this.getClass().getSimpleName();
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
        Log.i(TAG, "TextToSpeech.OnInitListener.onInit(" + status + ")");
        Locale locale = tts.getLanguage();
        Log.e(TAG, "locale=" + locale);
        Log.e(TAG, "tts.isLanguageAvailable(Locale.US)=" + tts.isLanguageAvailable(Locale.US));
        if (status == TextToSpeech.SUCCESS) {
          Log.i(TAG, "tts=" + tts);
          ttsReady = true;
          onTtsReady();
        } else {
          Log.e(TAG, "TextToSpeech.OnInitListener.onInit(ERROR = " + status + ")");
        }
      }
    };

    // May pause current activity
    checkTts();
  }

  protected void onTtsReady() {
  }

  private void checkTts() {
    Log.d(TAG, "checkTts()...");
    Intent checkIntent = new Intent();
    checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
    startActivityForResult(checkIntent, MY_DATA_CHECK_CODE);
    Log.d(TAG, "...checkTts()");
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    switch (requestCode) {
      case MY_DATA_CHECK_CODE:
        Log.d(TAG, "onActivityResult() resultCode=" + resultCode + "; data=" + data);
        switch (resultCode) {
          case TextToSpeech.Engine.CHECK_VOICE_DATA_PASS:
            Log.e(TAG, "Cool, ACTION_CHECK_TTS_DATA -> resultCode=CHECK_VOICE_DATA_PASS");
            tts = new TextToSpeech(getApplicationContext(), initListener);
            break;
          case TextToSpeech.Engine.CHECK_VOICE_DATA_MISSING_DATA:
            // missing data, install it
            Log.e(TAG, "Oh no, ACTION_CHECK_TTS_DATA -> resultCode=CHECK_VOICE_DATA_MISSING_DATA");

            Intent installIntent = new Intent();
            installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
            startActivity(installIntent);
            break;
          default:
            Log.e(TAG, "Oh no, ACTION_CHECK_TTS_DATA -> resultCode=" + resultCode);
        }
        if (tts == null) {
          Log.d(
              TAG,
              "Going to try 'tts = new TextToSpeech(getApplicationContext(), initListener)' even though ACTION_CHECK_TTS_DATA -> resultCode="
                  + resultCode);
          tts = new TextToSpeech(getApplicationContext(), initListener);
        }
        break;
      default:
        Log.d(TAG, "Unhandled: onActivityResult() requestCode=" + requestCode);
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    // TODO uncomment and workaround bug
    // tts.shutdown();
  }

  protected void sayNext(String text) {
    say(text, TextToSpeech.QUEUE_ADD, null);
  }

  protected void sayNow(String text) {
    say(text, TextToSpeech.QUEUE_FLUSH, null);
  }

  private boolean isNetworkAvailable() {
    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
    return activeNetworkInfo != null;
  }

  private void say(String text, int queueMode, String utterenceId) {
    Log.d(TAG, "say(" + text + ")");
    if (!ttsReady) {
      Log.d(TAG, "!ttsReady :(");
      return;
    }

    HashMap<String, String> map = new HashMap<String, String>();
    if (utterenceId != null) {
      map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, utterenceId);
    }
    if (isNetworkAvailable()) {
      map.put(/* TextToSpeech.Engine.KEY_FEATURE_NETWORK_SYNTHESIS */"networkTts",
          Boolean.TRUE.toString());
    }

    int speak = tts.speak(text, queueMode, map);
    Log.d(TAG, "tts.speak(text, queueMode, map) -> " + speak);
  }

}