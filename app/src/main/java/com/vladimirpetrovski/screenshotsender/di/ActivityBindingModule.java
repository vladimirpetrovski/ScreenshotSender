package com.vladimirpetrovski.screenshotsender.di;

import com.vladimirpetrovski.screenshotsender.presenter.main.MainActivity;
import com.vladimirpetrovski.screenshotsender.presenter.main.MainModule;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract class ActivityBindingModule {

  @ContributesAndroidInjector(modules = MainModule.class)
  abstract MainActivity configureMainActivityInjection();
}
