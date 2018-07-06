package com.vladimirpetrovski.screenshotsender.mvi;

import com.vladimirpetrovski.screenshotsender.mvi.Mvi.Interactor;
import com.vladimirpetrovski.screenshotsender.mvi.Mvi.Update;
import com.vladimirpetrovski.screenshotsender.mvi.Mvi.View;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public abstract class MviPresenter<S, V extends View<S>, I extends Interactor<S>>
    implements Mvi.Presenter<V> {

  private final I interactor;
  private Disposable stateDisposable;

  public MviPresenter(I interactor) {
    this.interactor = interactor;
  }

  @Override
  public void bindView(V view) {
    stateDisposable =
        interactor
            .interact(intents(view))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(view::render);
  }

  @Override
  public void unbindView(boolean reset) {
    if (reset) {
      interactor.resetState();
    }
    if (stateDisposable != null) {
      stateDisposable.dispose();
    }
  }

  protected abstract Observable<Update<S>> intents(V view);

  public I getInteractor() {
    return interactor;
  }
}
