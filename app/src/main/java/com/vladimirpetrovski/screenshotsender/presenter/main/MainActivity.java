package com.vladimirpetrovski.screenshotsender.presenter.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
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
    if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
      List<Image> images = ImagePicker.getImages(data);
      screenshotsAddedSubject.onNext(map(images));
    }
    super.onActivityResult(requestCode, resultCode, data);
  }

  private List<Screenshot> map(List<Image> images) {
    List<Screenshot> screenshots = new ArrayList<>();
    for (Image image : images) {
      screenshots.add(Screenshot.builder().name(image.getName()).path(image.getPath()).build());
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
