package sauer.listentospell;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

public class MyStatsActivity extends Activity {
  private static final String TAG = MyStatsActivity.class.getName();

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.d(TAG, "onCreate() findViewById(R.id.foo)=" + findViewById(R.id.foo));

    setContentView(R.layout.my_stats);
    Log.d(TAG, "onCreate() findViewById(R.id.foo)=" + findViewById(R.id.foo));
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    Log.d(TAG, "onSaveInstanceState() findViewById(R.id.foo)=" + findViewById(R.id.foo));
  }

  @Override
  public void onAttachedToWindow() {
    super.onAttachedToWindow();
    Log.d(TAG, "onAttachedToWindow() findViewById(R.id.foo)=" + findViewById(R.id.foo));
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    Log.d(TAG, "onBackPressed() findViewById(R.id.foo)=" + findViewById(R.id.foo));
  }

  @Override
  public void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    Log.d(TAG, "onDetachedFromWindow() findViewById(R.id.foo)=" + findViewById(R.id.foo));
  }

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    Log.d(TAG, "onKeyDown() findViewById(R.id.foo)=" + findViewById(R.id.foo));
    return super.onKeyDown(keyCode, event);
  }

  @Override
  public void onWindowFocusChanged(boolean hasFocus) {
    super.onWindowFocusChanged(hasFocus);
    Log.d(TAG, "onWindowFocusChanged() findViewById(R.id.foo)=" + findViewById(R.id.foo));
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    Log.d(TAG, "onDestroy() findViewById(R.id.foo)=" + findViewById(R.id.foo));
  }

  @Override
  protected void onStart() {
    super.onStart();
    Log.d(TAG, "onStart() findViewById(R.id.foo)=" + findViewById(R.id.foo));
  }

  @Override
  protected void onStop() {
    super.onStart();
    Log.d(TAG, "onStop() findViewById(R.id.foo)=" + findViewById(R.id.foo));
  }

  @Override
  protected void onRestart() {
    super.onRestart();
    Log.d(TAG, "onRestart() findViewById(R.id.foo)=" + findViewById(R.id.foo));
  }

  @Override
  protected void onResume() {
    super.onResume();
    Log.d(TAG, "onResume() findViewById(R.id.foo)=" + findViewById(R.id.foo));
  }

  @Override
  protected void onPause() {
    super.onPause();
    Log.d(TAG, "onPause() findViewById(R.id.foo)=" + findViewById(R.id.foo));
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    Log.d(TAG, "onConfigurationChanged() findViewById(R.id.foo)=" + findViewById(R.id.foo));
  }
}