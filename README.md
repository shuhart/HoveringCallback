# HoveringCallback
Drag & drop item decorator for RecyclerView with support for highlighting hovered items.

<img src="/images/demo.gif" alt="Sample" width="300px" />

Usage
-----

1. Add mavenCentral() to repositories block in your gradle file.
2. Add `implementation 'com.github.shuhart:hoveringcallback:1.3.0'` to your dependencies.
3. Look into the sample for additional details on how to use and configure the library.

Example:

```java
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
```
License
=======

    Copyright 2018 Bogdan Kornev.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
