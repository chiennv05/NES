package com.example.asm.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asm.Model.BMIRecord;
import com.example.asm.R;

import java.util.List;

public class BMIRecordAdapter extends RecyclerView.Adapter<BMIRecordAdapter.ViewHolder> {
    private List<BMIRecord> bmiRecordList;

    public BMIRecordAdapter(List<BMIRecord> bmiRecordList) {
        this.bmiRecordList = bmiRecordList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bmi_record, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BMIRecord record = bmiRecordList.get(position);
        holder.txtWeight.setText("Cân nặng: " + record.getWeight());
        holder.txtHeight.setText("Chiều cao: " + record.getHeight());
        holder.txtBMI.setText("BMI: " + record.getBmi());
        holder.txtBMICategory.setText("Loại béo: " + record.getBmiCategory());
        holder.txtDietRecommendations.setText("Bạn nên ăn: " + record.getDietRecommendations());
    }

    @Override
    public int getItemCount() {
        return bmiRecordList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtWeight, txtHeight, txtBMI, txtBMICategory, txtDietRecommendations;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtWeight = itemView.findViewById(R.id.txtWeight);
            txtHeight = itemView.findViewById(R.id.txtHeight);
            txtBMI = itemView.findViewById(R.id.txtBMI);
            txtBMICategory = itemView.findViewById(R.id.txtBMICategory);
            txtDietRecommendations = itemView.findViewById(R.id.txtDietRecommendations);
        }
    }
}
