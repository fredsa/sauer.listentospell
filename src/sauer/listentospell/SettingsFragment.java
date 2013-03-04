package sauer.listentospell;

import sauer.listentospell.app.ListenToSpellApplication;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class SettingsFragment extends Fragment {

  @SuppressWarnings("unused")
  private static final String TAG = SettingsFragment.class.getName();

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    return inflater.inflate(R.layout.activity_settings, null);
  }

  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    final ListenToSpellApplication app = (ListenToSpellApplication) getActivity().getApplication();
    // Spell Correct Words
    CheckBox spellCorrectWordsCheckbox = (CheckBox) getView().findViewById(
        R.id.spell_correct_words_checkbox);
    spellCorrectWordsCheckbox.setChecked(app.getSpellCorrectWords());
    spellCorrectWordsCheckbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        app.setSpellCorrectWords(isChecked);
      }
    });

    // Color Code Words
    CheckBox colorCodeWordsCheckbox = (CheckBox) getView().findViewById(
        R.id.color_code_words_checkbox);
    colorCodeWordsCheckbox.setChecked(app.getColorCodeWords());
    colorCodeWordsCheckbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        app.setColorCodeWords(isChecked);
      }
    });
  }

}