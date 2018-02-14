package com.shuhart.hoveringcallback;

import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

public interface ItemBackgroundCallback {

    @ColorInt
    int getHoverBackgroundColor(@NonNull RecyclerView.ViewHolder viewHolder);

    @ColorInt
    int getDefaultBackgroundColor(@NonNull RecyclerView.ViewHolder viewHolder);

    @ColorInt
    int getDraggingBackgroundColor(@NonNull RecyclerView.ViewHolder viewHolder);
}