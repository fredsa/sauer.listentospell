package sauer.listentospell;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class MainActivity extends TabActivity {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main_tab_layout);

    TabHost tabHost = getTabHost();
    TabHost.TabSpec spec;
    Intent intent;

    // Train
    intent = new Intent().setClass(this, TrainActivity.class);
    spec = tabHost.newTabSpec("train").setIndicator("Train").setContent(intent);
    tabHost.addTab(spec);

    // Enter Words
    intent = new Intent().setClass(this, EnterWordsActivity.class);
    spec = tabHost.newTabSpec("entry").setIndicator("Enter Words").setContent(intent);
    tabHost.addTab(spec);

    // My Stats
    intent = new Intent().setClass(this, MyStatsActivity.class);
    spec = tabHost.newTabSpec("entry2").setIndicator("My Stats").setContent(intent);
    tabHost.addTab(spec);

    ListenToSpellApplication app = (ListenToSpellApplication) getApplication();

    tabHost.setCurrentTab(app.isSetup() ? 0 : 1);
  }
}
