package com.vladimirpetrovski.screenshotsender.domain;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.vladimirpetrovski.screenshotsender.R;
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
    List<String> emails = latestState().getEmails();
    List<Screenshot> screenshots = latestState().getScreenshots();

    Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
    emailIntent.setType("plain/text");
    emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, emails.toArray(new String[0]));
    emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
        context.getString(R.string.screenshots));
    ArrayList<Uri> uris = new ArrayList<>();
    for (Screenshot screenshot : screenshots) {
      uris.add(Uri.parse(screenshot.getPath()));
    }
    emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
    context.startActivity(emailIntent);

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
