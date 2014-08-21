package sauer.listentospell.app;

import java.util.HashMap;
import java.util.Locale;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.speech.tts.TextToSpeech.OnUtteranceCompletedListener;
import android.util.Log;

public class Speaker implements OnUtteranceCompletedListener {

  private static final String TAG = Speaker.class.getName();

  private OnInitListener initListener;
  private boolean ttsReady;
  private TextToSpeech tts;
  private long utteranceSequence;
  private final ListenToSpellApplication app;
  private HashMap<String, Runnable> runnableMap = new HashMap<String, Runnable>();
  private HashMap<String, AsyncTask<Void, Void, Void>> runnableMap2 = new HashMap<String, AsyncTask<Void, Void, Void>>();

  public Speaker(ListenToSpellApplication app) {
    this.app = app;
  }

  public void initTts(final NextTask nextTask) {
    assert !ttsReady;
    initListener = new OnInitListener() {
      @Override
      public void onInit(int status) {
        if (status != TextToSpeech.SUCCESS) {
          Log.e(TAG, "TextToSpeech.OnInitListener.onInit(" + status + ")");
          return;
        }
        Locale locale = tts.getLanguage();
        Log.d(TAG, "tts.getLanguage()=" + locale);
        Log.e(TAG, "tts.isLanguageAvailable(Locale.US)=" + tts.isLanguageAvailable(Locale.US));
        ttsReady = true;
        nextTask.execute();
      }
    };

    tts = new TextToSpeech(app, initListener);
    tts.setOnUtteranceCompletedListener(this);
  }

  @Override
  public void onUtteranceCompleted(String utteranceId) {
    Runnable runnable = runnableMap.remove(utteranceId);
    if (runnable != null) {
      runnable.run();
    } else {
      AsyncTask<Void, Void, Void> onDone = runnableMap2.get(utteranceId);
      onDone.execute();
    }
  }

  private void sayNext(String text) {
    say(text, TextToSpeech.QUEUE_ADD, null);
  }

  private void sayNow(String text, String utteranceId) {
    say(text, TextToSpeech.QUEUE_FLUSH, utteranceId);
  }

  private String nextUtteranceId() {
    ++utteranceSequence;
    return Long.toString(utteranceSequence);
  }

  private boolean isNetworkAvailable() {
    ConnectivityManager connectivityManager = (ConnectivityManager) app.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
    return activeNetworkInfo != null;
  }

  public void say(String text) {
    sayNext(text);
  }

  public void sayNow(String text) {
    sayNow(text, null);
  }

  public void say(String text, NextTask nextTask) {
    String utteranceId = nextUtteranceId();
    runnableMap2.put(utteranceId, nextTask);
    say(text, TextToSpeech.QUEUE_ADD, utteranceId);
  }

  private void say(String text, int queueMode, String utterenceId) {
    Log.d(TAG, "say(" + text + ")");
    if (!ttsReady) {
      Log.e(TAG, "!ttsReady :(");
      return;
    }

    HashMap<String, String> map = new HashMap<String, String>();
    if (utterenceId != null) {
      map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, utterenceId);
    }
    if (isNetworkAvailable()) {
      map.put(TextToSpeech.Engine.KEY_FEATURE_NETWORK_SYNTHESIS, Boolean.TRUE.toString());
    }

    int speak = tts.speak(pronounce(text), queueMode, map);
    if (speak != TextToSpeech.SUCCESS) {
      Log.e(TAG, "tts.speak(text, queueMode, map) -> " + speak);
    }
  }

  private String pronounce(String letter) {
    if (letter.equals(" ")) {
      letter = "space";
    } else if (letter.equals("'")) {
      letter = "apostrophe";
    }
    return letter;
  }

}
