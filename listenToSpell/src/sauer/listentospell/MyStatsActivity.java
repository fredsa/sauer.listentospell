package sauer.listentospell;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MyStatsActivity extends Activity {
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    TextView textview = new TextView(this);
    textview.setText("Here are your stats");
    setContentView(textview);
  }
}