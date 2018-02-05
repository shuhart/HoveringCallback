package com.shuhart.hoveringcallback;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import static com.shuhart.hoveringcallback.HoveringCallback.DONW;
import static com.shuhart.hoveringcallback.HoveringCallback.UP;

class Helper {
    private int left;
    private int right;

    final Rect bounds = new Rect();

    void prepare(RecyclerView parent) {
        //noinspection AndroidLintNewApi - NewApi lint fails to handle overrides.
        if (parent.getClipToPadding()) {
            left = parent.getPaddingLeft();
            right = parent.getWidth() - parent.getPaddingRight();
        } else {
            left = 0;
            right = parent.getWidth();
        }
    }

    void calculateBounds(RecyclerView parent, View child) {
        parent.getDecoratedBoundsWithMargins(child, bounds);

        bounds.bottom += Math.round(child.getTranslationY());
        bounds.top += Math.round(child.getTranslationY());
        bounds.left = left;
        bounds.right = right;
    }

    boolean hover(Rect bounds, Rect target, @HoveringCallback.DragDirection int dragDirection) {
        if (dragDirection == DONW) {
            return bounds.bottom > target.top && bounds.top < target.bottom &&
                    bounds.top < target.top && bounds.bottom < target.bottom;
        } else if (dragDirection == UP) {
            return bounds.bottom > target.top && bounds.top < target.bottom &&
                    bounds.top > target.top && bounds.bottom > target.bottom;
        }
        return false;
    }
}
