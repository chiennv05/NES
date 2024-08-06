package com.example.asm.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asm.Model.ChayBo;
import com.example.asm.R;

import java.util.List;

public class ChayBoAdapter extends RecyclerView.Adapter<ChayBoAdapter.ViewHolder> {
    private List<ChayBo> mDataList;

    public ChayBoAdapter(List<ChayBo> dataList) {
        mDataList = dataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itemchaybo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ChayBo data = mDataList.get(position);
        holder.txBuocChan.setText(String.valueOf(data.getBuocchan()));  // Đã bật lại bước chân
        holder.txKilomet.setText(String.format("%.2f km", data.getKhoangcach()));
        holder.txCalo.setText(String.format("%.2f cal", data.getCalori()));
        holder.txTime.setText(String.format("%d min", data.getThoigian()));
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txBuocChan;
        public TextView txKilomet;
        public TextView txCalo;
        public TextView txTime;

        public ViewHolder(View itemView) {
            super(itemView);
            txBuocChan = itemView.findViewById(R.id.tx_buocchan);  // Đã bật lại bước chân
            txKilomet = itemView.findViewById(R.id.txkilomet);
            txCalo = itemView.findViewById(R.id.txcalo);
            txTime = itemView.findViewById(R.id.txtime);
        }
    }
}
