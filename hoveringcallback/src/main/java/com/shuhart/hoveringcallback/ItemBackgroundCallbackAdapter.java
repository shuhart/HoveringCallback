package com.shuhart.hoveringcallback;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

public class ItemBackgroundCallbackAdapter implements ItemBackgroundCallback {

    @Override
    @Nullable
    public Drawable getHoverBackground(RecyclerView.ViewHolder viewHolder) {
        return null;
    }

    @Override
    @Nullable
    public Drawable getDefaultBackground(RecyclerView.ViewHolder viewHolder) {
        return null;
    }

    @Override
    @Nullable
    public Drawable getDraggingBackground(RecyclerView.ViewHolder viewHolder) {
        return null;
    }
}