package com.example.asm.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asm.Model.BietOn;
import com.example.asm.R;

import java.util.List;

public class BietOnAdapter extends RecyclerView.Adapter<BietOnAdapter.BietOnHoder> {

    private Context context;
    private List<BietOn> list;

    public BietOnAdapter(Context context, List<BietOn> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public BietOnHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itemloibieton,parent, false);
        return new BietOnHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BietOnHoder holder, int position) {
        BietOn bietOn = list.get(position);
        holder.txloibieton.setText(bietOn.getLoibieton());
        holder.txngayviet.setText(bietOn.getDate());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class BietOnHoder extends RecyclerView.ViewHolder {
        private TextView txloibieton, txngayviet;
        public BietOnHoder(@NonNull View itemView) {
            super(itemView);
            txloibieton = itemView.findViewById(R.id.txloibieton);
            txngayviet = itemView.findViewById(R.id.txngayviet);
        }
    }
}
