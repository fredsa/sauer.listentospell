package sauer.listentospell;

import android.app.Activity;
import android.media.AudioManager;
import android.os.Bundle;

public class ListenToSpellActivity extends Activity {

  private static final String TAG = ListenToSpellActivity.class.getName();

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setVolumeControlStream(AudioManager.STREAM_MUSIC);
    
  }
}