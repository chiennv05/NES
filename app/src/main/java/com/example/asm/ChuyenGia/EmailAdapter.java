package com.example.asm.ChuyenGia;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asm.R;

import java.util.List;

public class EmailAdapter extends RecyclerView.Adapter<EmailAdapter.EmailViewHolder> {

    private List<String> emailList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(String email);
    }

    public EmailAdapter(List<String> emailList, OnItemClickListener listener) {
        this.emailList = emailList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public EmailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.email_item, parent, false);
        return new EmailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmailViewHolder holder, int position) {
        String email = emailList.get(position);
        holder.emailTextView.setText(email);
        holder.itemView.setOnClickListener(v -> listener.onItemClick(email));
    }

    @Override
    public int getItemCount() {
        return emailList.size();
    }

    public static class EmailViewHolder extends RecyclerView.ViewHolder {

        public TextView emailTextView;

        public EmailViewHolder(@NonNull View itemView) {
            super(itemView);
            emailTextView = itemView.findViewById(R.id.emailTextView);
        }
    }
}
