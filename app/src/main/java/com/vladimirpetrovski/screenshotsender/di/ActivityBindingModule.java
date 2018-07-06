package com.vladimirpetrovski.screenshotsender.di;

import com.vladimirpetrovski.screenshotsender.presenter.main.MainActivity;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract class ActivityBindingModule {

  @ContributesAndroidInjector
  abstract MainActivity configureMainActivityInjection();
}
