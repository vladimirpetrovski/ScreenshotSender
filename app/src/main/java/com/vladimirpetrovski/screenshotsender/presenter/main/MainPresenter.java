package com.vladimirpetrovski.screenshotsender.presenter.main;

import android.content.Intent;
import com.vladimirpetrovski.screenshotsender.domain.MainInteractor;
import com.vladimirpetrovski.screenshotsender.domain.MainState;
import com.vladimirpetrovski.screenshotsender.mvi.Mvi.Update;
import com.vladimirpetrovski.screenshotsender.mvi.MviPresenter;
import io.reactivex.Observable;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class MainPresenter extends MviPresenter<MainState, MainViewContract, MainInteractor> {

  public static int GALLERY_PICK_REQUEST_CODE = 20;

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
        .doOnNext(o -> {
          Intent intent = new Intent(Intent.ACTION_GET_CONTENT,
              android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
          intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
          intent.setType("image/*");
          activity.startActivityForResult(intent, GALLERY_PICK_REQUEST_CODE);
        })
        .flatMap(ignored -> getInteractor().handleAddScreenshotsClicks()));
    intents.add(view.removeScreenshotClick()
        .flatMap(getInteractor()::handleRemoveScreenshotClicks));

    return Observable.merge(intents);
  }
}
