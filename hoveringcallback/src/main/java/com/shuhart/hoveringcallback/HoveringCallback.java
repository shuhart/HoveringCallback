package com.shuhart.hoveringcallback;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

public class HoveringCallback extends ItemTouchHelper.SimpleCallback {
    private static final float threshold = 10;
    private final Rect draggedViewBounds = new Rect();
    private Helper helper = new Helper();
    private float deltaY;

    ItemBackgroundCallback backgroundCallback;
    @Nullable
    RecyclerView.ViewHolder current;

    public HoveringCallback() {
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0);
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        this.current = viewHolder;
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            viewHolder.itemView.setBackgroundDrawable(backgroundCallback.getDraggingBackground(viewHolder));
        }
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        viewHolder.itemView.setBackgroundDrawable(null);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
    }

    @Override
    public void onChildDraw(Canvas canvas, RecyclerView parent, RecyclerView.ViewHolder viewHolder,
                            float dX, float dY, int actionState, boolean isCurrentlyActive) {
        calculateDelta(viewHolder.itemView, dY);

        super.onChildDraw(canvas, parent, viewHolder, dX, dY, actionState, isCurrentlyActive);

        helper.prepare(parent);

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);

            helper.calculateBounds(parent, child);

            if (viewHolder.itemView == child) {
                continue;
            }

            RecyclerView.ViewHolder childViewHolder = parent.findContainingViewHolder(child);

            parent.getDecoratedBoundsWithMargins(viewHolder.itemView, draggedViewBounds);
            draggedViewBounds.top += dY;
            draggedViewBounds.bottom += dY;

            if (helper.hover(draggedViewBounds, helper.bounds, deltaY) && canDropOver(parent, viewHolder, childViewHolder)) {
                child.setBackgroundDrawable(backgroundCallback.getHoverBackground(childViewHolder));
            } else {
                child.setBackgroundDrawable(backgroundCallback.getDefaultBackground(childViewHolder));
            }
        }
    }

    private void calculateDelta(View view, float dY) {
        float translationY = view.getTranslationY();
        if (Math.abs(translationY - dY) >= threshold) {
            if (translationY > dY) {
                deltaY = -1;
            } else {
                deltaY = 1;
            }
        }
    }
}
