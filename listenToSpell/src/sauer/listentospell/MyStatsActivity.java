package sauer.listentospell;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;

public class MyStatsActivity extends Activity {
  private static final String TAG = MyStatsActivity.class.getName();

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.d(TAG, "onCreate()");

    TextView textview = new TextView(this);
    textview.setText("Here are your stats");
    setContentView(textview);
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    Log.d(TAG, "onSaveInstanceState()");
  }

  @Override
  public void onAttachedToWindow() {
    super.onAttachedToWindow();
    Log.d(TAG, "onAttachedToWindow()");
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    Log.d(TAG, "onBackPressed()");
  }

  @Override
  public void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    Log.d(TAG, "onDetachedFromWindow()");
  }

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    Log.d(TAG, "onKeyDown()");
    return super.onKeyDown(keyCode, event);
  }

  @Override
  public void onWindowFocusChanged(boolean hasFocus) {
    super.onWindowFocusChanged(hasFocus);
    Log.d(TAG, "onWindowFocusChanged()");
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    Log.d(TAG, "onDestroy()");
  }

  @Override
  protected void onStart() {
    super.onStart();
    Log.d(TAG, "onStart()");
  }

  @Override
  protected void onStop() {
    super.onStart();
    Log.d(TAG, "onStop()");
  }

  @Override
  protected void onRestart() {
    super.onRestart();
    Log.d(TAG, "onRestart()");
  }

  @Override
  protected void onResume() {
    super.onResume();
    Log.d(TAG, "onResume()");
  }

  @Override
  protected void onPause() {
    super.onPause();
    Log.d(TAG, "onPause()");
  }

}