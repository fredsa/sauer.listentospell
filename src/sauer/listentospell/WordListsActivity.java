package sauer.listentospell;

import java.util.ArrayList;
import java.util.Date;

import sauer.listentospell.app.ListenToSpellApplication;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.LightingColorFilter;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WordListsActivity extends SpeechActivity {
  private static final String TAG = WordListsActivity.class.getName();

  private Button addWordListButton;

  private ListenToSpellApplication app;
  private LinearLayout linearLayout;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.word_lists);
    addWordListButton = (Button) findViewById(R.id.add_word_list_button);
    addWordListButton.getBackground().setColorFilter(new LightingColorFilter(0x00000000, 0xFF2554C7));
    
    app = (ListenToSpellApplication) getApplication();

    linearLayout = (LinearLayout) findViewById(R.id.word_lists_linear_layout);
    addWordListButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View arg0) {
        addWordListPrompt();
      }

    });
  }

  private void addWordListPrompt() {
    AlertDialog.Builder alert = new AlertDialog.Builder(this);

    alert.setTitle("Create a new spelling list");
    alert.setMessage("Enter the name of your new list");

    // Set an EditText view to get user input 
    final EditText listNameEditText = new EditText(this);
    listNameEditText.setText("Word list " + DateFormat.getDateFormat(this).format(new Date()));
    alert.setView(listNameEditText);

    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int whichButton) {
        edit(listNameEditText.getText().toString());
      }
    });

    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int whichButton) {
        // do nothing
      }
    });

    alert.show();
  }

  private void edit(final String listName) {
    Intent intent = new Intent(app, WordListActivity.class);
    intent.putExtra("listName", listName);
    startActivity(intent);
  }

  private void addWordList(final String listName) {
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
        edit(listName);
      }
    });

    Button takeTestButton = (Button) item.findViewById(R.id.word_list_item_take_test);
    takeTestButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(app, TrainActivity.class);
        intent.putExtra("listName", listName);
        startActivity(intent);
      }
    });

    word.setText(listName);
  }

  private void loadAndShow() {
    linearLayout.removeAllViews();
    ArrayList<String> list = app.getListNames();
    for (String listName : list) {
      addWordList(listName);
    }
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

    loadAndShow();
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