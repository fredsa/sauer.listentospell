package sauer.listentospell;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import sauer.listentospell.app.ListenToSpellApplication;

public class WordListsActivity extends Activity {
  private static final String TAG = WordListsActivity.class.getName();

  private Button addWordListButton;

  private ListenToSpellApplication app;
  private LinearLayout linearLayout;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.word_lists);
    addWordListButton = (Button) findViewById(R.id.add_word_list_button);

    app = (ListenToSpellApplication) getApplication();

    linearLayout = (LinearLayout) findViewById(R.id.word_lists_linear_layout);
    addWordListButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View arg0) {
        addWordList();
      }

    });

    loadAndShow();
  }

  private void addWordList() {

  }

  private void loadAndShow() {
  }

}