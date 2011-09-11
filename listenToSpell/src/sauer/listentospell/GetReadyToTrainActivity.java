package sauer.listentospell;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class GetReadyToTrainActivity extends Activity {

  private static final String TAG = GetReadyToTrainActivity.class.getName();
  private Button startTrainingButton;
  private ListenToSpellApplication app;

  @Override
  protected void onResume() {
    super.onResume();
    setView();
  }

  private void setView() {
    if (!app.isSetup()) {
      setContentView(R.layout.cannot_train);
      return;
    }

    setContentView(R.layout.get_ready_to_train);

    startTrainingButton = (Button) findViewById(R.id.start_training);
    startTrainingButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent().setClass(GetReadyToTrainActivity.this, TrainActivity.class);
        startActivity(intent);
      }
    });
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    app = (ListenToSpellApplication) getApplication();

    if (!app.isSetup()) {
      setContentView(R.layout.cannot_train);
      return;
    }
  }
}