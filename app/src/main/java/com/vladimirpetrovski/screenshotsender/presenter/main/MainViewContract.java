package com.vladimirpetrovski.screenshotsender.presenter.main;

import com.vladimirpetrovski.screenshotsender.data.Screenshot;
import com.vladimirpetrovski.screenshotsender.domain.MainState;
import com.vladimirpetrovski.screenshotsender.mvi.Mvi;
import io.reactivex.Observable;
import java.util.List;

interface MainViewContract extends Mvi.View<MainState> {

  Observable<List<String>> emailListChanged();

  Observable<?> sendClicks();

  Observable<List<Screenshot>> addedScreenshots();

  Observable<?> addScreenshotsClick();

  Observable<Screenshot> removeScreenshotClick();
}
