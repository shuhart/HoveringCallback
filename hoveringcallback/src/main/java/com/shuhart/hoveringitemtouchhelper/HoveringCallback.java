package com.shuhart.hoveringitemtouchhelper;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

public class HoveringCallback extends ItemTouchHelper.SimpleCallback {
    private final Rect draggedViewBounds = new Rect();

    ItemBackgroundCallback backgroundCallback;
    @Nullable
    RecyclerView.ViewHolder current;
    Helper helper = new Helper();

    public HoveringCallback() {
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0);
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        this.current = viewHolder;
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
        super.onChildDraw(canvas, parent, viewHolder, dX, dY, actionState, isCurrentlyActive);

        helper.prepare(parent);

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);

            helper.calculateBounds(parent, child);

            if (viewHolder.itemView == child) {
//                helper.drawSafelyWithinCalculatedBounds(canvas, backgroundCallback.getEmptySlotBackground());
                continue;
            }

            RecyclerView.ViewHolder childViewHolder = parent.findContainingViewHolder(child);

            parent.getDecoratedBoundsWithMargins(viewHolder.itemView, draggedViewBounds);
            draggedViewBounds.top += dY;
            draggedViewBounds.bottom += dY;

//            if (child.getBackground() != null) continue;

            if (!canDropOver(parent, viewHolder, childViewHolder)) {
//                helper.drawSafelyWithinCalculatedBounds(
//                        canvas,
//                        backgroundCallback.getDefaultBackground(childViewHolder));
                child.setHovered(false);
            } else if (helper.hover(draggedViewBounds, helper.bounds)) {
//                helper.drawSafelyWithinCalculatedBounds(
//                        canvas,
//                        backgroundCallback.getHoverBackground(childViewHolder));
                child.setHovered(true);
            } else {
//                helper.drawSafelyWithinCalculatedBounds(
//                        canvas,
//                        backgroundCallback.getDefaultBackground(childViewHolder));
                child.setHovered(false);
            }
        }
    }

    @Override
    public void onChildDrawOver(Canvas canvas, RecyclerView parent, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDrawOver(canvas, parent, viewHolder, dX, dY, actionState, isCurrentlyActive);

//        canvas.save();
//        canvas.clipRect(draggedViewBounds);
//
//        parent.getDecoratedBoundsWithMargins(viewHolder.itemView, draggedViewBounds);
//        draggedViewBounds.top += dY;
//        draggedViewBounds.bottom += dY;
//        if (viewHolder.itemView.getBackground() == null) {
//            helper.drawBackgroundSafely(
//                    draggedViewBounds,
//                    canvas,
//                    backgroundCallback.getDraggingBackground(viewHolder));
//        }
//        viewHolder.itemView.draw(canvas);
//
//        canvas.restore();
    }
}
