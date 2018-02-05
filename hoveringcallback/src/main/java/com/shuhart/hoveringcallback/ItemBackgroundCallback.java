package com.shuhart.hoveringcallback;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

public interface ItemBackgroundCallback {

    @Nullable
    Drawable getHoverBackground(@NonNull RecyclerView.ViewHolder viewHolder);

    @Nullable
    Drawable getDefaultBackground(@NonNull RecyclerView.ViewHolder viewHolder);

    @Nullable
    Drawable getDraggingBackground(@NonNull RecyclerView.ViewHolder viewHolder);
}