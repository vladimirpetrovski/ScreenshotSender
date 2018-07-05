package com.vladimirpetrovski.screenshotsender;

import com.vladimirpetrovski.screenshotsender.di.DaggerAppComponent;
import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;

public class MainApp extends DaggerApplication {

  @Override
  public void onCreate() {
    super.onCreate();
  }

  @Override
  protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
    return DaggerAppComponent.builder().application(this).build();
  }
}
