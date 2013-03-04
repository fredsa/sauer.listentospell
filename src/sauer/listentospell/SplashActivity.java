package sauer.listentospell;

import sauer.listentospell.app.ListenToSpellApplication;
import sauer.listentospell.app.NextTask;
import android.content.Intent;
import android.os.Bundle;

public class SplashActivity extends SpeechActivity {

  @SuppressWarnings("unused")
  private static final String TAG = SplashActivity.class.getName();

  private ListenToSpellApplication app;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);
    app = (ListenToSpellApplication) getApplication();
  }

  @Override
  protected void onStart() {
    super.onStart();
    app.getSpeaker().initTts(new NextTask() {
      @Override
      protected void onPostExecute(Void result) {
        app.getSpeaker().say(getString(R.string.hello), new NextTask() {
          @Override
          protected void onPostExecute(Void result) {
            Intent intent = new Intent(app, MainActivity.class);
            startActivity(intent);
          }
        });
      }
    });
  }
}
