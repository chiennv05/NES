package com.example.asm.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdviceAdapter extends RecyclerView.Adapter<AdviceAdapter.AdviceViewHolder> {

    private String[] adviceTopics;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(String topic);
    }

    public AdviceAdapter(String[] adviceTopics, OnItemClickListener listener) {
        this.adviceTopics = adviceTopics;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AdviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new AdviceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdviceViewHolder holder, int position) {
        holder.bind(adviceTopics[position], listener);
    }

    @Override
    public int getItemCount() {
        return adviceTopics.length;
    }

    public static class AdviceViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;

        public AdviceViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }

        public void bind(final String topic, final OnItemClickListener listener) {
            textView.setText(topic);
            itemView.setOnClickListener(v -> listener.onItemClick(topic));
        }
    }
}
