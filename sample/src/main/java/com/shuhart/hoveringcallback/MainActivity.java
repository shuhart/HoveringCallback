package com.shuhart.hoveringcallback;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        Adapter adapter = new Adapter(new ArrayList<String>() {{
            for (int i = 0; i < 20; i++) {
                add("Item " + i);
            }
        }});
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        HoverItemDecoration itemDecoration = new HoverItemDecoration(
                new HoveringCallback() {
                    @Override
                    public void attachToRecyclerView(@Nullable RecyclerView recyclerView) {
                        super.attachToRecyclerView(recyclerView);
                        addOnDropListener(new OnDroppedListener() {
                            @Override
                            public void onDroppedOn(RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                                Toast.makeText(
                                        MainActivity.this,
                                        "Dropped on position " + target.getAdapterPosition(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public boolean canDropOver(RecyclerView recyclerView, RecyclerView.ViewHolder current, RecyclerView.ViewHolder target) {
                        return target.getAdapterPosition() % 4 != 0 && super.canDropOver(recyclerView, current, target);
                    }
                },
                new ItemBackgroundCallback() {
                    private int hoverColor = Color.parseColor("#e9effb");

                    @Override
                    public int getDefaultBackgroundColor(@NonNull RecyclerView.ViewHolder viewHolder) {
                        return Color.WHITE;
                    }

                    @Override
                    public int getDraggingBackgroundColor(@NonNull RecyclerView.ViewHolder viewHolder) {
                        return Color.WHITE;
                    }

                    @Override
                    public int getHoverBackgroundColor(@NonNull RecyclerView.ViewHolder viewHolder) {
                        return hoverColor;
                    }
                });
        itemDecoration.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter);
    }

    class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

        private List<String> items;

        Adapter(List<String> items) {
            this.items = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.textView.setText(items.get(position));
            float height = getItemHeight(holder.itemView.getContext());
            if (position % 2 == 0) {
                height *= 2;
            }
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            lp.height = (int) height;
            holder.itemView.setLayoutParams(lp);
        }

        private float getItemHeight(Context context) {
            TypedValue typedValue = new TypedValue();
            context.getTheme().resolveAttribute(android.R.attr.listPreferredItemHeight, typedValue, true);
            return TypedValue.complexToDimensionPixelSize(typedValue.data, context.getResources().getDisplayMetrics());
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView textView = itemView.findViewById(R.id.textView);

            ViewHolder(View itemView) {
                super(itemView);
            }
        }
    }
}
