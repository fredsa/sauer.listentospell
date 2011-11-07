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

    //    // Train
    //    intent = new Intent().setClass(this, GetReadyToTrainActivity.class);
    //    spec = tabHost.newTabSpec("train").setIndicator("Train").setContent(intent);
    //    tabHost.addTab(spec);
    //
    //    // Word List
    //    intent = new Intent().setClass(this, WordListActivity.class);
    //    spec = tabHost.newTabSpec("entry").setIndicator("Word List").setContent(intent);
    //    tabHost.addTab(spec);

    // Word Lists
    intent = new Intent().setClass(this, WordListsActivity.class);
    spec = tabHost.newTabSpec("entry").setIndicator("Word Lists").setContent(intent);
    tabHost.addTab(spec);

    // My Stats
    intent = new Intent().setClass(this, MyStatsActivity.class);
    spec = tabHost.newTabSpec("entry2").setIndicator("My Stats").setContent(intent);
    tabHost.addTab(spec);

    //    ListenToSpellApplication app = (ListenToSpellApplication) getApplication();
    //    tabHost.setCurrentTab(app.isSetup() ? 0 : 1);
    tabHost.setCurrentTab(0);
  }
}
