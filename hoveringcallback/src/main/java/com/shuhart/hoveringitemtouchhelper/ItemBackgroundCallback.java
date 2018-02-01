package com.shuhart.hoveringitemtouchhelper;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

public interface ItemBackgroundCallback {

    @Nullable
    Drawable getHoverBackground(RecyclerView.ViewHolder viewHolder);

    @Nullable
    Drawable getDefaultBackground(RecyclerView.ViewHolder viewHolder);

    @Nullable
    Drawable getDraggingBackground(RecyclerView.ViewHolder viewHolder);

    @Nullable
    Drawable getEmptySlotBackground();
}