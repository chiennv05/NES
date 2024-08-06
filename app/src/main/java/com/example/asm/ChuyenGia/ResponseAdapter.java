package com.example.asm.ChuyenGia;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asm.R;


import java.util.List;

public class ResponseAdapter extends RecyclerView.Adapter<ResponseAdapter.ResponseViewHolder> {

    private List<QuestionResponse> responseList;

    public ResponseAdapter(List<QuestionResponse> responseList) {
        this.responseList = responseList;
    }

    @NonNull
    @Override
    public ResponseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_response, parent, false);
        return new ResponseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResponseViewHolder holder, int position) {
        QuestionResponse response = responseList.get(position);
        holder.tvQuestion.setText(response.getQuestion());
        holder.tvResponse.setText(response.getResponse());
    }

    @Override
    public int getItemCount() {
        return responseList.size();
    }

    public static class ResponseViewHolder extends RecyclerView.ViewHolder {

        TextView tvQuestion, tvResponse;

        public ResponseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQuestion = itemView.findViewById(R.id.tvQuestion);
            tvResponse = itemView.findViewById(R.id.tvResponse);
        }
    }
}