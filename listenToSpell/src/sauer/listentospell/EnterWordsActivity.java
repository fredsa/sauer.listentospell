package sauer.listentospell;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class EnterWordsActivity extends Activity {
  private EditText wordList;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.enter_words);
    wordList = (EditText) findViewById(R.id.word_list);

    Button saveButton = (Button) findViewById(R.id.word_list_save_button);
    saveButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        String text = wordList.getText().toString();
        parseAndSave(text);
      }
    });
  }

  private void parseAndSave(String text) {
    String[] words = text.split("[^a-zA-Z-]");
    StringBuilder t = new StringBuilder();
    for (String word : words) {
      t.append(word).append(",");
    }
    wordList.setText(t);
  }

}