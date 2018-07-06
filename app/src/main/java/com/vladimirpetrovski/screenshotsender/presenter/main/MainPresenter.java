package com.vladimirpetrovski.screenshotsender.presenter.main;

import com.esafirm.imagepicker.features.ImagePicker;
import com.vladimirpetrovski.screenshotsender.domain.MainInteractor;
import com.vladimirpetrovski.screenshotsender.domain.MainState;
import com.vladimirpetrovski.screenshotsender.mvi.Mvi.Update;
import com.vladimirpetrovski.screenshotsender.mvi.MviPresenter;
import io.reactivex.Observable;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class MainPresenter extends MviPresenter<MainState, MainViewContract, MainInteractor> {

  private MainActivity activity;

  @Inject
  MainPresenter(MainInteractor interactor, MainActivity activity) {
    super(interactor);
    this.activity = activity;
  }

  @Override
  protected Observable<Update<MainState>> intents(MainViewContract view) {
    List<Observable<Update<MainState>>> intents = new ArrayList<>();

    intents.add(view.emailListChanged().flatMap(getInteractor()::handleEmailsListChanged));
    intents.add(view.addedScreenshots().flatMap(getInteractor()::handleAddedScreenshots));
    intents.add(view.sendClicks().flatMap(ignored -> getInteractor().handleSendButtonClicks()));
    intents.add(view.addScreenshotsClick()
        .doOnNext(o -> ImagePicker.create(activity).start())
        .flatMap(ignored -> getInteractor().handleAddScreenshotsClicks()));
    intents.add(view.removeScreenshotClick()
        .flatMap(getInteractor()::handleRemoveScreenshotClicks));

    return Observable.merge(intents);
  }
}
