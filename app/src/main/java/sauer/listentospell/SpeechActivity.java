package sauer.listentospell;

import android.app.Activity;
import android.media.AudioManager;
import android.os.Bundle;

public abstract class SpeechActivity extends Activity {

  @SuppressWarnings("unused")
  private final String TAG = SpeechActivity.class.getName() + ":" + this.getClass().getSimpleName();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setVolumeControlStream(AudioManager.STREAM_MUSIC);
  }

}