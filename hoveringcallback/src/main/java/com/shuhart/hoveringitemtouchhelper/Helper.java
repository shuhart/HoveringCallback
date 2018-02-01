package com.shuhart.hoveringitemtouchhelper;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

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

    void drawSafelyWithinCalculatedBounds(Canvas canvas, @Nullable Drawable drawable) {
        drawBackgroundSafely(bounds, canvas, drawable);
    }

    void drawBackgroundSafely(Rect bounds, Canvas canvas, @Nullable Drawable drawable) {
        if (drawable != null) {
            drawable.setBounds(bounds);
            drawable.draw(canvas);
        }
    }

    boolean hover(Rect bounds, Rect target) {
        return bounds.bottom > target.top && bounds.top < target.bottom &&
                bounds.top < target.top && bounds.bottom < target.bottom;
    }
}
