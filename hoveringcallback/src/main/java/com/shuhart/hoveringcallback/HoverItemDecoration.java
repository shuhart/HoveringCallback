package com.shuhart.hoveringcallback;

import android.graphics.Canvas;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class HoverItemDecoration extends ItemTouchHelper {
    private ItemBackgroundCallback backgroundCallback;
    private HoveringCallback callback;

    public HoverItemDecoration(HoveringCallback callback, ItemBackgroundCallback backgroundCallback) {
        super(callback);
        this.callback = callback;
        this.backgroundCallback = backgroundCallback;
        callback.backgroundCallback = backgroundCallback;
    }

    @Override
    public void attachToRecyclerView(@Nullable RecyclerView recyclerView) {
        callback.attachToRecyclerView(recyclerView);
        super.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(canvas, parent, state);

        RecyclerView.ViewHolder viewHolder = callback.selected;
        if (viewHolder == null) {
            drawDefaultBackground(parent);
        }
    }

    private void drawDefaultBackground(RecyclerView parent) {
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            RecyclerView.ViewHolder viewHolder = parent.findContainingViewHolder(child);
            if (viewHolder == null) continue;
            child.setBackgroundColor(backgroundCallback.getDefaultBackgroundColor(viewHolder));
        }
    }
}
