package sauer.listentospell;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class MainActivity extends SpeechActivity {

  private class Foo implements TabListener {

    private final Fragment fragment;

    public Foo(Fragment fragment) {
      this.fragment = fragment;
    }

    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
    }

    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
      getFragmentManager().beginTransaction().replace(R.id.main_fragment_container, fragment).commit();

    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
    }

  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    ActionBar actionBar = getActionBar();
    actionBar.setHomeButtonEnabled(false);

    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

    actionBar.addTab(actionBar.newTab().setText("Word Lists").setTabListener(
        new Foo(new WordListsFragment())));
    actionBar.addTab(actionBar.newTab().setText("Preferences").setTabListener(
        new Foo(new SpellerPreferenceFragment())));
  }
}
