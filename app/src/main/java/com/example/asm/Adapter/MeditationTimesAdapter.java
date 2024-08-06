package com.example.asm.Adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class MeditationTimesAdapter extends RecyclerView.Adapter<MeditationTimesAdapter.ViewHolder> {

    private List<Long> meditationTimes;

    public MeditationTimesAdapter(List<Long> meditationTimes) {
        this.meditationTimes = meditationTimes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        long duration = meditationTimes.get(position);
        int hours = (int) (duration / 3600000);
        int minutes = (int) (duration % 3600000) / 60000;
        int seconds = (int) (duration % 60000) / 1000;
        holder.tvMeditationTime.setText(String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds));
    }

    @Override
    public int getItemCount() {
        return meditationTimes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMeditationTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMeditationTime = itemView.findViewById(android.R.id.text1);
        }
    }
}
