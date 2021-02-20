package com.shuhart.hoveringcallback;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public interface ItemBackgroundCallback {

    @ColorInt
    int getHoverBackgroundColor(@NonNull RecyclerView.ViewHolder viewHolder);

    @ColorInt
    int getDefaultBackgroundColor(@NonNull RecyclerView.ViewHolder viewHolder);

    @ColorInt
    int getDraggingBackgroundColor(@NonNull RecyclerView.ViewHolder viewHolder);
}