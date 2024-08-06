package com.example.asm.Amnhac;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asm.R;

import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicViewHolder> {

    private List<String> musicNames;
    private OnItemClickListener listener;

    public MusicAdapter(List<String> musicNames) {
        this.musicNames = musicNames;
    }

    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song, parent, false);
        return new MusicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicViewHolder holder, int position) {
        String name = musicNames.get(position);
        holder.tvLink.setText(name);
    }

    @Override
    public int getItemCount() {
        return musicNames.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, String name); // Thêm tham số để truyền tên bài hát
    }

    class MusicViewHolder extends RecyclerView.ViewHolder {

        TextView tvLink;

        public MusicViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLink = itemView.findViewById(R.id.tvMusicLink);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position, musicNames.get(position));
                    }
                }
            });
        }
    }
}
