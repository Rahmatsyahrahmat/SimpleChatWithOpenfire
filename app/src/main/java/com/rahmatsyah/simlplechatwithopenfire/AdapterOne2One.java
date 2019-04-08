package com.rahmatsyah.simlplechatwithopenfire;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rahmatsyah.simlplechatwithopenfire.model.MessageData;

import java.util.ArrayList;

public class AdapterOne2One extends RecyclerView.Adapter<AdapterOne2One.ViewHolder> {

    private ArrayList<MessageData> items;

    public AdapterOne2One(ArrayList<MessageData> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_layout_one, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        MessageData data = items.get(i);
        viewHolder.tvHeading.setText(data.getHeding());
        viewHolder.tvMessage.setText(data.getMessage());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvHeading, tvMessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHeading = itemView.findViewById(R.id.tvHeading);
            tvMessage = itemView.findViewById(R.id.tvMessage);
        }
    }
}
