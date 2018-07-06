package com.vladimirpetrovski.screenshotsender.presenter.main;

import android.net.Uri;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.squareup.picasso.Picasso;
import com.vladimirpetrovski.screenshotsender.R;
import com.vladimirpetrovski.screenshotsender.data.Screenshot;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class ScreenshotsAdapter extends ListAdapter<Screenshot, ViewHolder> {

  @IntDef(flag = true, value = {
      TYPE_SCREENSHOT,
      TYPE_ADD_SCREENSHOT
  })
  @Retention(RetentionPolicy.SOURCE)
  @interface ScreenshotsAdapterTypes {

  }

  private static final int TYPE_SCREENSHOT = 1;
  private static final int TYPE_ADD_SCREENSHOT = 2;

  private PublishSubject<Object> addScreenshotClicksSubject = PublishSubject.create();
  private PublishSubject<Screenshot> screenshotRemoveClicksSubject = PublishSubject.create();

  ScreenshotsAdapter() {
    super(new ItemCallback());
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
      @ScreenshotsAdapterTypes int type) {
    if (type == TYPE_SCREENSHOT) {
      return new ScreenshotViewHolder(
          LayoutInflater.from(parent.getContext())
              .inflate(R.layout.item_screenshot, parent, false));
    } else {
      return new AddScreenshotViewHolder(
          LayoutInflater.from(parent.getContext())
              .inflate(R.layout.item_add, parent, false));
    }
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
    int itemViewType = getItemViewType(position);
    if (itemViewType == TYPE_SCREENSHOT) {
      ScreenshotViewHolder holder = (ScreenshotViewHolder) viewHolder;
      Screenshot item = getItem(position);
      Picasso.get()
          .load(Uri.parse(item.getPath()))
          .resize(400, 400)
          .centerCrop()
          .transform(new RoundedCornersTransformation(30, 0))
          .into(holder.thumbnail);
      holder.removeBtn
          .setOnClickListener(
              v -> screenshotRemoveClicksSubject.onNext(getItem(viewHolder.getAdapterPosition())));
    } else {
      AddScreenshotViewHolder holder = (AddScreenshotViewHolder) viewHolder;
      holder.itemView
          .setOnClickListener(v -> addScreenshotClicksSubject.onNext(Observable.empty()));
    }
  }

  @Override
  public int getItemCount() {
    return super.getItemCount() + 1;
  }

  @Override
  public @ScreenshotsAdapterTypes
  int getItemViewType(int position) {
    if (position == getItemCount() - 1) {
      return TYPE_ADD_SCREENSHOT;
    }
    return TYPE_SCREENSHOT;
  }

  static class ScreenshotViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.item_remove)
    ImageView removeBtn;

    @BindView(R.id.item_thumbnail)
    ImageView thumbnail;

    ScreenshotViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }

  static class AddScreenshotViewHolder extends RecyclerView.ViewHolder {

    AddScreenshotViewHolder(View itemView) {
      super(itemView);
    }
  }

  static class ItemCallback extends DiffUtil.ItemCallback<Screenshot> {

    @Override
    public boolean areItemsTheSame(@NonNull Screenshot oldItem, @NonNull Screenshot newItem) {
      return oldItem.getName().equals(newItem.getName());
    }

    @Override
    public boolean areContentsTheSame(@NonNull Screenshot oldItem, @NonNull Screenshot newItem) {
      return oldItem.equals(newItem);
    }
  }

  public Observable<?> addScreenshotClicks() {
    return addScreenshotClicksSubject;
  }

  public Observable<Screenshot> screenshotRemoveClicks() {
    return screenshotRemoveClicksSubject;
  }
}
