package com.vladimirpetrovski.screenshotsender.domain;

import com.vladimirpetrovski.screenshotsender.mvi.Mvi;
import java.util.List;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class EmailListChangedUpdate implements Mvi.Update<MainState> {

  boolean sendEnabled;
  List<String> emails;

  @Override
  public MainState reduce(MainState previousState) {
    return previousState.toBuilder().sendEnabled(sendEnabled).emails(emails).build();
  }
}
