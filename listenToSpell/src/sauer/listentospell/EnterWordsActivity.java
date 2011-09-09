package sauer.listentospell;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class EnterWordsActivity extends Activity {
  private EditText wordListEditText;

  private EditText logArea;

  private ListenToSpellApplication listenToSpellApplication;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.enter_words);
    logArea = (EditText) findViewById(R.id.log_area);

    wordListEditText = (EditText) findViewById(R.id.word_list);

    listenToSpellApplication = (ListenToSpellApplication) getApplication();


    String t = listenToSpellApplication.getWordText();
    wordListEditText.setText(t);

    Button saveButton = (Button) findViewById(R.id.word_list_save_button);
    saveButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        String text = wordListEditText.getText().toString();
        parseAndSave(text);
      }
    });
  }

  private void parseAndSave(String text) {
    text = listenToSpellApplication.normalize(text);
    wordListEditText.setText(text);
    listenToSpellApplication.updateWords(text);
  }

  protected void log(String text) {
    logArea.setText(text + "\n" + logArea.getText());
  }

}