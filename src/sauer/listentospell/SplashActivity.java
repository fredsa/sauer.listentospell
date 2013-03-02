package sauer.listentospell;

import android.content.Intent;
import android.os.Bundle;

public class SplashActivity extends SpeechActivity {

  private static final String _42 = "42";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_splash);
  }

  @Override
  protected void onTtsReady() {
    super.onTtsReady();
    sayNow(getString(R.string.hello), _42);
  }

  @Override
  public void onUtteranceCompleted(String utteranceId) {
    super.onUtteranceCompleted(utteranceId);
    if (utteranceId.equals(_42)) {
      Intent intent = new Intent(this, MainActivity.class);
      startActivity(intent);
    }
  }

}
