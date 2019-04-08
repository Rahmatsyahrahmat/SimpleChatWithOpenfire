package com.rahmatsyah.simlplechatwithopenfire.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rahmatsyah.simlplechatwithopenfire.R;
import com.rahmatsyah.simlplechatwithopenfire.holder.RecieveMessageHolder;
import com.rahmatsyah.simlplechatwithopenfire.holder.SendMessageHolder;
import com.rahmatsyah.simlplechatwithopenfire.model.Message;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_MESSAGE_SEND = 1;
    private static final int VIEW_TYPE_MESSAGE_RECIEVE = 2;

    private Context context;
    private ArrayList<Message> messages;

    public MessageAdapter(Context context, ArrayList<Message> messages){
        this.context = context;
        this.messages = messages;

    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        if (message.getSender().getName().equals("Rahmatsyah")){
            return VIEW_TYPE_MESSAGE_SEND;
        }
        else {
            return VIEW_TYPE_MESSAGE_RECIEVE;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;

        if (i==VIEW_TYPE_MESSAGE_SEND){
            view = LayoutInflater.from(context).inflate(R.layout.view_chat_send_global,viewGroup,false);
            return new SendMessageHolder(view);
        }else if (i==VIEW_TYPE_MESSAGE_RECIEVE){
            view = LayoutInflater.from(context).inflate(R.layout.view_chat_recieve_global,viewGroup,false);
            return new RecieveMessageHolder(view);
        }

        return null;
    }



    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        Message message = messages.get(i);

        switch (viewHolder.getItemViewType()){
            case VIEW_TYPE_MESSAGE_SEND:
                ((SendMessageHolder) viewHolder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_RECIEVE:
                ((RecieveMessageHolder)viewHolder).bind(message);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
    public void addItem(Message message){
        messages.add(message);
        notifyItemInserted(messages.size());
    }
}
