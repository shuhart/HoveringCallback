package com.shuhart.hoveringitemtouchhelper;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

public class HoverItemDecoration extends ItemTouchHelper {

    public HoverItemDecoration(HoveringCallback callback) {
        super(callback);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }
}
