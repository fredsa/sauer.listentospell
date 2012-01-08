package sauer.listentospell;

import android.app.Activity;
import android.media.AudioManager;
import android.os.Bundle;

public class SpeechActivity extends Activity {

  private static final String TAG = SpeechActivity.class.getName();

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setVolumeControlStream(AudioManager.STREAM_MUSIC);
    
  }
}