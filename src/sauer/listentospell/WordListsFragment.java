package sauer.listentospell;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import sauer.listentospell.app.ListenToSpellApplication;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.LightingColorFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WordListsFragment extends Fragment {

  @SuppressWarnings("unused")
  private static final String TAG = WordListsFragment.class.getName();

  private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy.MM.dd", Locale.US);

  private Button addWordListButton;

  private ListenToSpellApplication app;
  private LinearLayout linearLayout;

  private LayoutInflater inflater;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    this.inflater = inflater;
    super.onCreateView(inflater, container, savedInstanceState);

    View view = inflater.inflate(R.layout.activity_word_lists, null);

    addWordListButton = (Button) view.findViewById(R.id.add_word_list_button);
    addWordListButton.getBackground().setColorFilter(
        new LightingColorFilter(0x00000000, 0xFF2554C7));

    addWordListButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View arg0) {
        addWordListPrompt();
      }
    });

    linearLayout = (LinearLayout) view.findViewById(R.id.word_lists_linear_layout);

    return view;
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    app = (ListenToSpellApplication) getActivity().getApplication();
    loadAndShow();
  }

  private void addWordListPrompt() {
    AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

    alert.setTitle("Create a new spelling list");
    alert.setMessage(R.string.prompt_new_list_name);
    app.getSpeaker().sayNow(getString(R.string.prompt_new_list_name));

    // Set an EditText view to get user input 
    final EditText listNameEditText = new EditText(getActivity());
    listNameEditText.setText("Word list " + DATE_FORMAT.format(new Date()));
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
    View item = inflater.inflate(R.layout.fragment_word_list, null);
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

}