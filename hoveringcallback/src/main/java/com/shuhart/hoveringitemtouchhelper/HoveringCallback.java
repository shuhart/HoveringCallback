package com.shuhart.hoveringitemtouchhelper;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import static android.support.v7.widget.helper.ItemTouchHelper.ACTION_STATE_DRAG;

public class HoveringCallback extends ItemTouchHelper.SimpleCallback {
    private final Rect bounds = new Rect();
    private final Rect draggedViewBounds = new Rect();
    private ItemBackgroundCallback backgroundCallback = new ItemBackgroundCallbackAdapter();

    public HoveringCallback() {
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0);
    }

    public void setBackgroundCallback(ItemBackgroundCallback backgroundCallback) {
        this.backgroundCallback = backgroundCallback;
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

//        if (actionState != ACTION_STATE_DRAG) return;

        final int left;
        final int right;
        //noinspection AndroidLintNewApi - NewApi lint fails to handle overrides.
        if (parent.getClipToPadding()) {
            left = parent.getPaddingLeft();
            right = parent.getWidth() - parent.getPaddingRight();
        } else {
            left = 0;
            right = parent.getWidth();
        }

        final int childCount = parent.getChildCount();
        Log.d(getClass().getSimpleName(), "Called!");
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            parent.getDecoratedBoundsWithMargins(child, bounds);

            if (viewHolder.itemView == child) {
                drawBackgroundSafely(bounds, canvas, backgroundCallback.getEmptySlotBackground());
                continue;
            }

            RecyclerView.ViewHolder childViewHolder = parent.findContainingViewHolder(child);

            bounds.bottom += Math.round(child.getTranslationY());
            bounds.top += Math.round(child.getTranslationY());
            bounds.left = left;
            bounds.right = right;

            parent.getDecoratedBoundsWithMargins(viewHolder.itemView, draggedViewBounds);
            draggedViewBounds.top += dY;
            draggedViewBounds.bottom += dY;

            if (child.getBackground() != null) continue;

            if (!canDropOver(parent, viewHolder, childViewHolder)) {
                drawBackgroundSafely(bounds, canvas, backgroundCallback.getDefaultBackground(childViewHolder));
            } else if (draggedViewBounds.bottom > bounds.top && draggedViewBounds.top < bounds.bottom &&
                    draggedViewBounds.top < bounds.top && draggedViewBounds.bottom < bounds.bottom) {
//                Log.d(getClass().getSimpleName(), "Bingo: db.bottom = " + draggedViewBounds.bottom +
//                        ", b.bottom = " + bounds.bottom + ", db.top = " + draggedViewBounds.top + ", b.top = " + bounds.top);
                drawBackgroundSafely(bounds, canvas, backgroundCallback.getHoverBackground(childViewHolder));
            } else {
                drawBackgroundSafely(bounds, canvas, backgroundCallback.getDefaultBackground(childViewHolder));
            }
        }
    }

    private void drawBackgroundSafely(Rect bounds, Canvas canvas, @Nullable Drawable drawable) {
        if (drawable != null) {
            drawable.setBounds(bounds);
            drawable.draw(canvas);
        }
    }

    public interface ItemBackgroundCallback {

        @Nullable Drawable getHoverBackground(RecyclerView.ViewHolder viewHolder);

        @Nullable Drawable getDefaultBackground(RecyclerView.ViewHolder viewHolder);

        @Nullable Drawable getDraggingBackground(RecyclerView.ViewHolder viewHolder);

        @Nullable Drawable getEmptySlotBackground();
    }

    public static class ItemBackgroundCallbackAdapter implements ItemBackgroundCallback {

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

        @Override
        @Nullable
        public Drawable getEmptySlotBackground() {
            return null;
        }
    }
}
