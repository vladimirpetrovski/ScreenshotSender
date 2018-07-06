package com.vladimirpetrovski.screenshotsender.presenter.main;

import static com.vladimirpetrovski.screenshotsender.presenter.main.MainPresenter.GALLERY_PICK_REQUEST_CODE;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.jakewharton.rxbinding2.view.RxView;
import com.pchmn.materialchips.ChipsInput;
import com.pchmn.materialchips.ChipsInput.ChipsListener;
import com.pchmn.materialchips.model.ChipInterface;
import com.vladimirpetrovski.screenshotsender.R;
import com.vladimirpetrovski.screenshotsender.data.Screenshot;
import com.vladimirpetrovski.screenshotsender.domain.MainState;
import dagger.android.support.DaggerAppCompatActivity;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;

public class MainActivity extends DaggerAppCompatActivity implements MainViewContract {

  @BindView(R.id.emails_chip_group)
  ChipsInput emailsInput;

  @BindView(R.id.screenshots_list)
  RecyclerView screenshotsList;

  @BindView(R.id.btn_send)
  Button sendBtn;

  @Inject
  MainPresenter presenter;

  private ScreenshotsAdapter screenshotsAdapter = new ScreenshotsAdapter();
  private PublishSubject<List<String>> emailsChangedSubject = PublishSubject.create();
  private PublishSubject<List<Screenshot>> screenshotsAddedSubject = PublishSubject.create();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    screenshotsList.setLayoutManager(new GridLayoutManager(this, 3));
    screenshotsList.setAdapter(screenshotsAdapter);
    emailsInput.addChipsListener(new ChipsListener() {
      @Override
      public void onChipAdded(ChipInterface chipInterface, int i) {
      }

      @Override
      public void onChipRemoved(ChipInterface chipInterface, int i) {
        notifyEmailsChanged(emailsInput.getEditText().getText());
      }

      @Override
      public void onTextChanged(CharSequence charSequence) {
        if (charSequence.toString().endsWith(" ")) {
          notifyEmailsChanged(charSequence);
        }
      }
    });
    presenter.bindView(this);
  }

  @Override
  protected void onDestroy() {
    presenter.unbindView(isFinishing());
    super.onDestroy();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == GALLERY_PICK_REQUEST_CODE && resultCode == RESULT_OK) {
      List<Uri> uris = new ArrayList<>();
      if (data.getData() != null) {
        uris.add(data.getData());
      } else {
        if (data.getClipData() != null) {
          ClipData mClipData = data.getClipData();
          for (int i = 0; i < mClipData.getItemCount(); i++) {
            ClipData.Item item = mClipData.getItemAt(i);
            Uri uri = item.getUri();
            uris.add(uri);
          }
        }
      }
      screenshotsAddedSubject.onNext(map(uris));
    }
    super.onActivityResult(requestCode, resultCode, data);
  }

  private List<Screenshot> map(List<Uri> uris) {
    List<Screenshot> screenshots = new ArrayList<>();
    for (Uri uri : uris) {
      screenshots.add(Screenshot.builder().name(uri.getPath()).path(uri.toString()).build());
    }
    return screenshots;
  }

  @Override
  public void render(MainState state) {
    screenshotsAdapter.submitList(state.getScreenshots());

    for (ChipInterface chipInterface : createChips(state.getEmails())) {
      emailsInput.addChip(chipInterface);
    }
    sendBtn.setEnabled(state.isSendEnabled());
  }

  @Override
  public Observable<List<String>> emailListChanged() {
    return emailsChangedSubject;
  }

  @Override
  public Observable<?> sendClicks() {
    return RxView.clicks(sendBtn);
  }

  @Override
  public Observable<List<Screenshot>> addedScreenshots() {
    return screenshotsAddedSubject;
  }

  @Override
  public Observable<?> addScreenshotsClick() {
    return screenshotsAdapter.addScreenshotClicks();
  }

  @Override
  public Observable<Screenshot> removeScreenshotClick() {
    return screenshotsAdapter.screenshotRemoveClicks();
  }

  private List<EmailChip> createChips(List<String> emails) {
    List<EmailChip> emailChips = new ArrayList<>();
    for (String email : emails) {
      emailChips.add(new EmailChip(email));
    }
    return emailChips;
  }

  private void notifyEmailsChanged(CharSequence charSequence) {
    String[] newEmails = charSequence.toString().trim().split("\\s+");
    List<String> emails = new ArrayList<>();
    for (EmailChip chip : (List<EmailChip>) emailsInput.getSelectedChipList()) {
      emails.add(chip.getLabel());
    }
    if (!charSequence.toString().equals("")) {
      emails.addAll(Arrays.asList(newEmails));
    }
    emailsChangedSubject.onNext(emails);
  }
}
