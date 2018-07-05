package com.vladimirpetrovski.screenshotsender.mvi;

import com.vladimirpetrovski.screenshotsender.mvi.Mvi.Update;
import io.reactivex.Observable;
import java.util.concurrent.atomic.AtomicReference;

public abstract class MviInteractor<S> implements Mvi.Interactor<S> {

  private AtomicReference<S> latestState = new AtomicReference<>();

  @Override
  public Observable<S> interact(Observable<Update<S>> input) {
    latestState.compareAndSet(null, initialState());
    return Observable.just(latestState.get())
        .flatMap(state -> input.scan(state, (previous, partial) -> partial.reduce(previous)))
        .doOnNext(latestState::set);
  }

  @Override
  public void resetState() {
    latestState.set(initialState());
  }

  protected abstract S initialState();

  protected final S latestState() {
    return latestState.get();
  }
}
