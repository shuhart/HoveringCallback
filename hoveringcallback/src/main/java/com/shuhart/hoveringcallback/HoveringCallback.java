package com.shuhart.hoveringcallback;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import java.lang.annotation.Retention;
import java.util.ArrayList;
import java.util.List;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public class HoveringCallback extends ItemTouchHelper.SimpleCallback {
    @Retention(SOURCE)
    @IntDef({UP, DONW, IDLE})
    public @interface DragDirection {
    }

    public static final int UP = 0;
    public static final int DONW = 1;
    public static final int IDLE = 2;

    public interface OnDroppedListener {
        void onDroppedOn(RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target);
    }

    private List<OnDroppedListener> onDroppedListeners = new ArrayList<>();
    private final Rect draggedViewBounds = new Rect();
    private Helper helper = new Helper();
    private @DragDirection
    int dragDirection = IDLE;
    @Nullable
    private RecyclerView recyclerView;

    ItemBackgroundCallback backgroundCallback;
    @Nullable
    RecyclerView.ViewHolder current;

    public HoveringCallback() {
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0);
    }

    public void attachToRecyclerView(@Nullable RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public void addOnDropListener(OnDroppedListener listener) {
        onDroppedListeners.add(listener);
    }

    public void removeOnDropListener(OnDroppedListener listener) {
        onDroppedListeners.remove(listener);
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        if (viewHolder == null) {
            if (current != null) {
                findDroppedOn();
            }
        }
        this.current = viewHolder;
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE && viewHolder != null) {
            viewHolder.itemView.setBackgroundDrawable(backgroundCallback.getDraggingBackground(viewHolder));
        }
    }

    private void findDroppedOn() {
        if (recyclerView == null) return;
        int childCount = recyclerView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = recyclerView.getChildAt(i);
            RecyclerView.ViewHolder holder = recyclerView.findContainingViewHolder(view);
            if (holder == null) continue;
            if (view.getBackground() == backgroundCallback.getHoverBackground(holder)) {
                notifyDroppedOnListeners(holder);
            }
        }
    }

    private void notifyDroppedOnListeners(RecyclerView.ViewHolder holder) {
        for (OnDroppedListener listener : onDroppedListeners) {
            listener.onDroppedOn(current, holder);
        }
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        viewHolder.itemView.setBackgroundDrawable(null);
        dragDirection = IDLE;
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
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            calculateDelta(viewHolder, dY);
        }

        super.onChildDraw(canvas, parent, viewHolder, dX, dY, actionState, isCurrentlyActive);

        if (actionState != ItemTouchHelper.ACTION_STATE_DRAG) {
            return;
        }

        helper.prepare(parent);

        parent.getDecoratedBoundsWithMargins(viewHolder.itemView, draggedViewBounds);
        draggedViewBounds.top += dY;
        draggedViewBounds.bottom += dY;

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);

            helper.calculateBounds(parent, child);

            if (viewHolder.itemView == child) {
                continue;
            }

            RecyclerView.ViewHolder childViewHolder = parent.findContainingViewHolder(child);

            if (childViewHolder == null) {
                continue;
            }

            if (hover(draggedViewBounds, helper.bounds, dragDirection) && canDropOver(parent, viewHolder, childViewHolder)) {
                child.setBackgroundDrawable(backgroundCallback.getHoverBackground(childViewHolder));
            } else {
                child.setBackgroundDrawable(backgroundCallback.getDefaultBackground(childViewHolder));
            }
        }
    }

    private void calculateDelta(RecyclerView.ViewHolder viewHolder, float dY) {
        View view = viewHolder.itemView;
        float translationY = view.getTranslationY();
        if (dragDirection == IDLE) {
            if (Math.abs(translationY - dY) >= view.getHeight() * getMoveThreshold(viewHolder)) {
                if (translationY < dY) {
                    dragDirection = DONW;
                } else if (dragDirection > dY) {
                    dragDirection = UP;
                }
            }
        } else if (dragDirection == DONW) {
            if (translationY > dY) {
                if (Math.abs(translationY - dY) >= view.getHeight() * getMoveThreshold(viewHolder)) {
                    dragDirection = UP;
                }
            }
        } else if (dragDirection == UP) {
            if (translationY < dY) {
                if (Math.abs(translationY - dY) >= view.getHeight() * getMoveThreshold(viewHolder)) {
                    dragDirection = DONW;
                }
            }
        }
    }

    protected boolean hover(Rect draggedViewBounds, Rect target, @DragDirection int dragDirection) {
        return helper.hover(draggedViewBounds, target, dragDirection);
    }

    @Override
    public float getMoveThreshold(RecyclerView.ViewHolder viewHolder) {
        return 0.05f;
    }

    @Override
    public RecyclerView.ViewHolder chooseDropTarget(RecyclerView.ViewHolder selected,
                                                    List<RecyclerView.ViewHolder> dropTargets,
                                                    int curX, int curY) {
        int right = curX + selected.itemView.getWidth();
        int bottom = curY + selected.itemView.getHeight();
        RecyclerView.ViewHolder winner = null;
        int winnerScore = -1;
        final int dx = curX - selected.itemView.getLeft();
        final int dy = curY - selected.itemView.getTop();
        final int targetsSize = dropTargets.size();
        for (int i = 0; i < targetsSize; i++) {
            final RecyclerView.ViewHolder target = dropTargets.get(i);
            if (dx > 0) {
                int diff = target.itemView.getRight() - right;
                if (diff < 0 && target.itemView.getRight() > selected.itemView.getRight()) {
                    final int score = Math.abs(diff);
                    if (score > winnerScore) {
                        winnerScore = score;
                        winner = target;
                    }
                }
            }
            if (dx < 0) {
                int diff = target.itemView.getLeft() - curX;
                if (diff > 0 && target.itemView.getLeft() < selected.itemView.getLeft()) {
                    final int score = Math.abs(diff);
                    if (score > winnerScore) {
                        winnerScore = score;
                        winner = target;
                    }
                }
            }
            if (dy < 0) {
                int diff = target.itemView.getTop() - curY;
                if (target.itemView.getTop() < selected.itemView.getTop()) {
                    final int score = Math.abs(diff);
                    if (score > winnerScore) {
                        winnerScore = score;
                        winner = target;
                    }
                }
            }

            if (dy > 0) {
                int diff = target.itemView.getBottom() - bottom;
                if (target.itemView.getBottom() > selected.itemView.getBottom()) {
                    final int score = Math.abs(diff);
                    if (score > winnerScore) {
                        winnerScore = score;
                        winner = target;
                    }
                }
            }
        }
        return winner;
    }
}
