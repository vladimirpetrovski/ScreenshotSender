package com.vladimirpetrovski.screenshotsender.data;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode
public class Screenshot {

  String name;
  String path;
}
