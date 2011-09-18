package sauer.listentospell;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import sauer.listentospell.app.ListenToSpellApplication;

import java.util.ArrayList;

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
        addWordList("New List");
      }
    });

    loadAndShow();
  }

  private void addWordList(String listName) {
    //    Button button = new Button(this);
    //    button.setText(listName);
    //    linearLayout.addView(button);

    //    LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

    View item = View.inflate(this, R.layout.word_list_item, null);
    linearLayout.addView(item);

    TextView word = (TextView) item.findViewById(R.id.word_list_item_word);

    Button editButton = (Button) item.findViewById(R.id.word_list_item_edit_button);
    editButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(app, WordListActivity.class);
        startActivity(intent);
      }
    });

    Button takeTestButton = (Button) item.findViewById(R.id.word_list_item_take_test);
    takeTestButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(app, TrainActivity.class);
        startActivity(intent);
      }
    });

    word.setText(listName);
  }

  private void loadAndShow() {
    ArrayList<String> list = app.getListNames();
    for (String listName : list) {
      addWordList(listName);
      addWordList(listName);
      addWordList(listName);
    }
  }

}