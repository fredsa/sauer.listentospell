package sauer.listentospell;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class HelloTabWidget extends TabActivity {

  private static final String TAG = HelloTabWidget.class.getName();

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main_tab_layout);

    Resources res = getResources(); // Resource object to get Drawables
    TabHost tabHost = getTabHost(); // The activity TabHost
    TabHost.TabSpec spec; // Resusable TabSpec for each tab
    Intent intent; // Reusable Intent for each tab

    // Word Entry Tab
    intent = new Intent().setClass(this, TestActivity.class);
    spec = tabHost.newTabSpec("test").setIndicator("Test Me").setContent(intent);
    tabHost.addTab(spec);

    // Word Entry Tab
    intent = new Intent().setClass(this, WordEntryActivity.class);
    spec = tabHost.newTabSpec("entry").setIndicator("Enter Words").setContent(intent);
    tabHost.addTab(spec);

    // Word Entry Tab
    intent = new Intent().setClass(this, WordEntryActivity.class);
    spec = tabHost.newTabSpec("entry2").setIndicator("My Stats").setContent(intent);
    tabHost.addTab(spec);

    // Listen Tab
    //    intent = new Intent().setClass(this, ListenToSpellActivity.class);
    //    spec = tabHost.newTabSpec("listen").setIndicator("Listen");//.setContent(intent);

    //    tabHost.addTab(spec);

    //    intent = new Intent().setClass(this, SongsActivity.class);
    //    spec = tabHost.newTabSpec("songs").setIndicator("Songs",
    //        res.getDrawable(R.drawable.ic_tab_songs)).setContent(intent);
    //    tabHost.addTab(spec);

    tabHost.setCurrentTab(0);
  }
}
