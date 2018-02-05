package com.shuhart.hoveringcallback;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

public class ItemBackgroundCallbackAdapter implements ItemBackgroundCallback {

    @Override
    @Nullable
    public Drawable getHoverBackground(@NonNull RecyclerView.ViewHolder viewHolder) {
        return null;
    }

    @Override
    @Nullable
    public Drawable getDefaultBackground(@NonNull RecyclerView.ViewHolder viewHolder) {
        return null;
    }

    @Override
    @Nullable
    public Drawable getDraggingBackground(@NonNull RecyclerView.ViewHolder viewHolder) {
        return null;
    }
}