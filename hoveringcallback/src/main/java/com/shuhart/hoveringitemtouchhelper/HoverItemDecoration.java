package com.shuhart.hoveringitemtouchhelper;

import android.graphics.Canvas;
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
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(canvas, parent, state);

//        RecyclerView.ViewHolder viewHolder = callback.current;
//
//        if (viewHolder == null) {
//            drawDefaultBackground(canvas, parent);
//        }
    }

    private void drawDefaultBackground(Canvas canvas, RecyclerView parent) {
        callback.helper.prepare(parent);

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);

            if (child.getBackground() != null) continue;

            callback.helper.calculateBounds(parent, child);
            RecyclerView.ViewHolder childViewHolder = parent.findContainingViewHolder(child);
            callback.helper.drawSafelyWithinCalculatedBounds(
                    canvas,
                    backgroundCallback.getDefaultBackground(childViewHolder));
        }
    }




}
