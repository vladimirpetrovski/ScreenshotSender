package com.vladimirpetrovski.screenshotsender.domain;

import com.vladimirpetrovski.screenshotsender.data.Screenshot;
import java.util.Collections;
import java.util.List;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class MainState {

  @Default
  boolean sendEnabled = false;
  @Default
  List<String> emails = Collections.emptyList();
  @Default
  List<Screenshot> screenshots = Collections.emptyList();
}
