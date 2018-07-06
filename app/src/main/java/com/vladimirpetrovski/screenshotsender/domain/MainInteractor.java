package com.vladimirpetrovski.screenshotsender.domain;

import android.content.Context;
import com.vladimirpetrovski.screenshotsender.data.Screenshot;
import com.vladimirpetrovski.screenshotsender.mvi.Mvi.Update;
import com.vladimirpetrovski.screenshotsender.mvi.MviInteractor;
import io.reactivex.Observable;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MainInteractor extends MviInteractor<MainState> {

  private Context context;

  @Inject
  MainInteractor(Context context) {
    this.context = context;
  }

  @Override
  protected MainState initialState() {
    return MainState.builder().build();
  }

  public Observable<Update<MainState>> handleEmailsListChanged(List<String> emails) {
    return Observable.just(EmailListChangedUpdate.builder()
        .sendEnabled(emails.size() > 0 && latestState().getScreenshots().size() > 0)
        .emails(emails)
        .build());
  }

  public Observable<Update<MainState>> handleSendButtonClicks() {
    //TODO implement
    return Observable.empty();
  }

  public Observable<Update<MainState>> handleAddScreenshotsClicks() {
    return Observable.empty();
  }

  public Observable<Update<MainState>> handleRemoveScreenshotClicks(Screenshot screenshot) {
    List<Screenshot> screenshots = new ArrayList<>(latestState().getScreenshots());
    screenshots.remove(screenshot);
    return buildScreenshotsUpdate(screenshots);
  }

  public Observable<Update<MainState>> handleAddedScreenshots(List<Screenshot> list) {
    List<Screenshot> finalList = new ArrayList<>(latestState().getScreenshots());
    finalList.addAll(list);
    return buildScreenshotsUpdate(finalList);
  }

  private Observable<Update<MainState>> buildScreenshotsUpdate(List<Screenshot> screenshots) {
    return Observable.just(ScreenshotChangedUpdate.builder()
        .sendEnabled(latestState().getEmails().size() > 0 && screenshots.size() > 0)
        .screenshots(screenshots)
        .build());
  }
}
