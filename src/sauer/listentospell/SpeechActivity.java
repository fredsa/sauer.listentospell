package sauer.listentospell;

import java.util.HashMap;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;

public abstract class SpeechActivity extends Activity {

  private final String TAG = SpeechActivity.class.getName() + ":" + this.getClass().getSimpleName();
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

    tts = new TextToSpeech(getApplicationContext(), initListener);
  }

  protected void onTtsReady() {
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