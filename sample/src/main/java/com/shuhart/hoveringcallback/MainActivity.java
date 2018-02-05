package com.shuhart.hoveringcallback;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
                            public void onDroppedOn(RecyclerView.ViewHolder target) {
                                Toast.makeText(
                                        MainActivity.this,
                                        "Dropped on position " + target.getAdapterPosition(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                },
                new ItemBackgroundCallbackAdapter() {
                    private Drawable defaultBackground = new ColorDrawable(Color.WHITE);
                    private Drawable hoverBackground = new ColorDrawable(Color.parseColor("#e9effb"));

                    @Override
                    public Drawable getDefaultBackground(RecyclerView.ViewHolder viewHolder) {
                        return defaultBackground;
                    }

                    @Override
                    public Drawable getDraggingBackground(RecyclerView.ViewHolder viewHolder) {
                        return defaultBackground;
                    }

                    @Override
                    public Drawable getHoverBackground(RecyclerView.ViewHolder viewHolder) {
                        return hoverBackground;
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
