package sauer.listentospell;

import sauer.listentospell.app.ListenToSpellApplication;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class SettingsActivity extends SpeechActivity {
  private static final String TAG = SettingsActivity.class.getName();

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.d(TAG, "onCreate()");
    setContentView(R.layout.settings);

    
    final ListenToSpellApplication app = (ListenToSpellApplication) getApplication();
    
    // Spell Correct Words
    CheckBox spellCorrectWordsCheckbox = (CheckBox) findViewById(R.id.spell_correct_words_checkbox);
    spellCorrectWordsCheckbox.setChecked(app.getSpellCorrectWords());
    spellCorrectWordsCheckbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        app.setSpellCorrectWords(isChecked);
      }
    });
    
    // Color Code Words
    CheckBox colorCodeWordsCheckbox = (CheckBox) findViewById(R.id.color_code_words_checkbox);
    colorCodeWordsCheckbox.setChecked(app.getColorCodeWords());
    colorCodeWordsCheckbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        app.setColorCodeWords(isChecked);
      }
    });
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

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    Log.d(TAG, "onConfigurationChanged()");
  }
}