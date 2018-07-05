package com.vladimirpetrovski.screenshotsender.di;

import android.app.Application;
import com.vladimirpetrovski.screenshotsender.MainApp;
import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;
import javax.inject.Singleton;

@Singleton
@Component(
    modules = {
        AppModule.class,
        ActivityBindingModule.class,
        AndroidSupportInjectionModule.class
    })
public interface AppComponent extends AndroidInjector<MainApp> {

  @Component.Builder
  interface Builder {

    @BindsInstance
    AppComponent.Builder application(Application application);

    AppComponent build();
  }
}
