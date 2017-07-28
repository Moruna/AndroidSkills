package com.moruna.recycleviewtest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Author: Moruna
 * Date: 2017-07-28
 * Copyright (c) 2017,dudu Co.,Ltd. All rights reserved.
 */
public class ViewAdapter extends RecyclerView.Adapter<ViewAdapter.ViewHolder> {
    private LayoutInflater inflater;
    private ArrayList<String> list;
    private OnItemClickListener onItemClickListener;

    public ViewAdapter(Context context, ArrayList<String> list) {
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        private ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.e("adapter", "onCreateViewHolder: --in");
        View view = inflater.inflate(R.layout.item_layout, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(holder.itemView,
                        holder.getLayoutPosition());
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }
}
