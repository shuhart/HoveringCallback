package com.shuhart.hoveringcallback;

import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

public class HoverItemDecoration extends ItemTouchHelper {
    private ItemBackgroundCallback backgroundCallback = new ItemBackgroundCallbackAdapter();
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

        RecyclerView.ViewHolder viewHolder = callback.current;
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
            child.setBackgroundDrawable(backgroundCallback.getDefaultBackground(viewHolder));
        }
    }
}
