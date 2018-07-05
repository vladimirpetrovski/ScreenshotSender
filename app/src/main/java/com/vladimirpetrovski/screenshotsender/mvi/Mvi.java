package com.vladimirpetrovski.screenshotsender.mvi;

import io.reactivex.Observable;

public interface Mvi {

  interface View<S> {

    void render(S state);
  }

  interface Presenter<V extends View> {

    void bindView(V view);

    void unbindView(boolean reset);
  }

  interface Interactor<S> {

    Observable<S> interact(Observable<Update<S>> input);

    void resetState();
  }

  interface Update<S> {

    S reduce(S previousState);
  }
}
