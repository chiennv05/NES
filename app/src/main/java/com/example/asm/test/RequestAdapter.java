package com.example.asm.test;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asm.R;

import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestViewHolder> {

    private Context context;
    private List<String> requestList;
    private OnAcceptListener onAcceptListener;

    public RequestAdapter(Context context, List<String> requestList, OnAcceptListener onAcceptListener) {
        this.context = context;
        this.requestList = requestList;
        this.onAcceptListener = onAcceptListener;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_request, parent, false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        String friendEmail = requestList.get(position);
        holder.tvFriendEmail.setText(friendEmail);

        holder.btnAccept.setOnClickListener(v -> onAcceptListener.onAccept(friendEmail));
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public interface OnAcceptListener {
        void onAccept(String friendEmail);
    }

    public static class RequestViewHolder extends RecyclerView.ViewHolder {
        private TextView tvFriendEmail;
        private Button btnAccept;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFriendEmail = itemView.findViewById(R.id.tvFriendEmail);
            btnAccept = itemView.findViewById(R.id.btnAccept);
        }
    }
}
