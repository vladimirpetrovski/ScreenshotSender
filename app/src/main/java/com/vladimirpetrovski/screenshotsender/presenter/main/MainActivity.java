package com.vladimirpetrovski.screenshotsender.presenter.main;

import android.os.Bundle;
import com.vladimirpetrovski.screenshotsender.R;
import dagger.android.support.DaggerAppCompatActivity;

public class MainActivity extends DaggerAppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }
}
