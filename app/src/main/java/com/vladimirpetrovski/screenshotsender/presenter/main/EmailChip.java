package com.vladimirpetrovski.screenshotsender.presenter.main;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import com.pchmn.materialchips.model.ChipInterface;

public class EmailChip implements ChipInterface {

  private String email;

  EmailChip(String email) {
    this.email = email;
  }

  @Override
  public Object getId() {
    return null;
  }

  @Override
  public Uri getAvatarUri() {
    return null;
  }

  @Override
  public Drawable getAvatarDrawable() {
    return null;
  }

  @Override
  public String getLabel() {
    return email;
  }

  @Override
  public String getInfo() {
    return null;
  }
}
