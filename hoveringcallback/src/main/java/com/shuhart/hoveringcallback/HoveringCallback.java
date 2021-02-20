package com.shuhart.hoveringcallback;

import android.graphics.Canvas;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HoveringCallback extends ItemTouchHelper.SimpleCallback {
    //re-used list for selecting a swap target
    private List<RecyclerView.ViewHolder> swapTargets = new ArrayList<>();
    //re used for for sorting swap targets
    private List<Integer> distances = new ArrayList<>();
    private float selectedStartX;
    private float selectedStartY;

    public interface OnDroppedListener {
        void onDroppedOn(RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target);
    }

    private List<OnDroppedListener> onDroppedListeners = new ArrayList<>();
    @Nullable
    private RecyclerView recyclerView;
    @Nullable
    RecyclerView.ViewHolder selected;
    @Nullable
    private RecyclerView.ViewHolder hovered;

    ItemBackgroundCallback backgroundCallback;

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
            if (hovered != null) {
                notifyDroppedOnListeners(hovered);
            }
        } else {
            selectedStartX = viewHolder.itemView.getLeft();
            selectedStartY = viewHolder.itemView.getTop();
        }
        this.selected = viewHolder;
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE && viewHolder != null) {
            viewHolder.itemView.setBackgroundColor(backgroundCallback.getDraggingBackgroundColor(viewHolder));
        }
    }

    private void notifyDroppedOnListeners(RecyclerView.ViewHolder holder) {
        for (OnDroppedListener listener : onDroppedListeners) {
            listener.onDroppedOn(selected, holder);
        }
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        viewHolder.itemView.setBackgroundColor(backgroundCallback.getDefaultBackgroundColor(viewHolder));
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
    }

    @Override
    public void onChildDraw(Canvas canvas, RecyclerView parent, RecyclerView.ViewHolder viewHolder,
                            float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(canvas, parent, viewHolder, dX, dY, actionState, isCurrentlyActive);

        if (actionState != ItemTouchHelper.ACTION_STATE_DRAG) {
            return;
        }

        if (recyclerView == null || selected == null) {
            return;
        }

        final RecyclerView.LayoutManager lm = recyclerView.getLayoutManager();
        final int childCount = lm.getChildCount();
        List<RecyclerView.ViewHolder> swapTargets = findSwapTargets(viewHolder, dX, dY);
        final int x = (int) (selectedStartX + dX);
        final int y = (int) (selectedStartY + dY);
        hovered = chooseDropTarget(viewHolder, swapTargets, x, y);
        if (hovered == null) {
            this.swapTargets.clear();
            this.distances.clear();
        }
        for (int i = 0; i < childCount; i++) {
            final View child = lm.getChildAt(i);

            if (viewHolder.itemView == child) {
                continue;
            }

            RecyclerView.ViewHolder childViewHolder = parent.findContainingViewHolder(child);

            if (childViewHolder == null || childViewHolder.getAdapterPosition() == RecyclerView.NO_POSITION) {
                continue;
            }

            if (childViewHolder == hovered) {
                child.setBackgroundColor(backgroundCallback.getHoverBackgroundColor(childViewHolder));
            } else {
                child.setBackgroundColor(backgroundCallback.getDefaultBackgroundColor(childViewHolder));
            }
        }
    }

    private List<RecyclerView.ViewHolder> findSwapTargets(RecyclerView.ViewHolder viewHolder, float dX, float dY) {
        swapTargets.clear();
        distances.clear();
        final int margin = getBoundingBoxMargin();
        final int left = Math.round(selectedStartX + dX) - margin;
        final int top = Math.round(selectedStartY + dY) - margin;
        final int right = left + viewHolder.itemView.getWidth() + 2 * margin;
        final int bottom = top + viewHolder.itemView.getHeight() + 2 * margin;
        final int centerX = (left + right) / 2;
        final int centerY = (top + bottom) / 2;
        final RecyclerView.LayoutManager lm = recyclerView.getLayoutManager();
        final int childCount = lm.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View other = lm.getChildAt(i);
            if (other == viewHolder.itemView) {
                continue; //myself!
            }
            if (other.getBottom() < top || other.getTop() > bottom
                    || other.getRight() < left || other.getLeft() > right) {
                continue;
            }
            final RecyclerView.ViewHolder otherVh = recyclerView.getChildViewHolder(other);
            if (canDropOver(recyclerView, selected, otherVh)) {
                // find the index to add
                final int dx = Math.abs(centerX - (other.getLeft() + other.getRight()) / 2);
                final int dy = Math.abs(centerY - (other.getTop() + other.getBottom()) / 2);
                final int dist = dx * dx + dy * dy;

                int pos = 0;
                final int cnt = swapTargets.size();
                for (int j = 0; j < cnt; j++) {
                    if (dist > distances.get(j)) {
                        pos++;
                    } else {
                        break;
                    }
                }
                swapTargets.add(pos, otherVh);
                distances.add(pos, dist);
            }
        }
        return swapTargets;
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
