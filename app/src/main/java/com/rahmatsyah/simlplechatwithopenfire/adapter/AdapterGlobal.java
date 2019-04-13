package com.rahmatsyah.simlplechatwithopenfire.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rahmatsyah.simlplechatwithopenfire.R;
import com.rahmatsyah.simlplechatwithopenfire.model.MessageGlobal;

import java.util.ArrayList;

public class AdapterGlobal extends RecyclerView.Adapter<AdapterGlobal.ViewHolder> {

    private ArrayList<MessageGlobal> items;

    public AdapterGlobal(ArrayList<MessageGlobal> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_global, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        MessageGlobal data = items.get(i);

        viewHolder.tvHeadGlobal.setText(data.getHeding());
        viewHolder.tvMessHead.setText(data.getMessage());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(MessageGlobal message) {
        items.add(message);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvHeadGlobal, tvMessHead;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHeadGlobal = itemView.findViewById(R.id.tvHeadingGlobal);
            tvMessHead = itemView.findViewById(R.id.tvMessageGlobal);
        }
    }
}
