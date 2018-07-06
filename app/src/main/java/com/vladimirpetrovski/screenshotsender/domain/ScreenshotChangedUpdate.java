package com.vladimirpetrovski.screenshotsender.domain;

import com.vladimirpetrovski.screenshotsender.data.Screenshot;
import com.vladimirpetrovski.screenshotsender.mvi.Mvi;
import java.util.List;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ScreenshotChangedUpdate implements Mvi.Update<MainState> {

  boolean sendEnabled;
  List<Screenshot> screenshots;

  @Override
  public MainState reduce(MainState previousState) {
    return previousState.toBuilder().sendEnabled(sendEnabled).screenshots(screenshots).build();
  }
}
