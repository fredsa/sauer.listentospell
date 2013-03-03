package sauer.listentospell;

import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public abstract class SpeechActivity extends FragmentActivity {

  @SuppressWarnings("unused")
  private final String TAG = SpeechActivity.class.getName() + ":" + this.getClass().getSimpleName();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setVolumeControlStream(AudioManager.STREAM_MUSIC);
  }

}