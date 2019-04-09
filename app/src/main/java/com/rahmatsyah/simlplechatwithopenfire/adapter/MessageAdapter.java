package com.rahmatsyah.simlplechatwithopenfire.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rahmatsyah.simlplechatwithopenfire.R;
import com.rahmatsyah.simlplechatwithopenfire.holder.RecieveImageHolder;
import com.rahmatsyah.simlplechatwithopenfire.holder.RecieveMessageHolder;
import com.rahmatsyah.simlplechatwithopenfire.holder.SendImageHolder;
import com.rahmatsyah.simlplechatwithopenfire.holder.SendMessageHolder;
import com.rahmatsyah.simlplechatwithopenfire.model.ChatData;
import com.rahmatsyah.simlplechatwithopenfire.model.ImageData;
import com.rahmatsyah.simlplechatwithopenfire.model.MessageData;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_MESSAGE_SEND = 1;
    private static final int VIEW_TYPE_MESSAGE_RECIEVE = 2;

    private static final int VIEW_TYPE_IMAGE_SEND = 3;
    private static final int VIEW_TYPE_IMAGE_RECIEVE = 4;

    private Context context;
    private ArrayList<ChatData> messages;

    public MessageAdapter(Context context, ArrayList<ChatData> messages){
        this.context = context;
        this.messages = messages;

    }

    @Override
    public int getItemViewType(int position) {
        ChatData chat = messages.get(position);
        String SENDER = "rahmat";
        if (chat.getImageData()!=null){
            if (chat.getImageData().getHeding().equals(SENDER)){
                return VIEW_TYPE_IMAGE_SEND;
            }
            else {
                return VIEW_TYPE_IMAGE_RECIEVE;
            }
        }
        else {
            if (chat.getMessageData().getHeding().equals(SENDER)){
                return VIEW_TYPE_MESSAGE_SEND;
            }
            else {
                return VIEW_TYPE_MESSAGE_RECIEVE;
            }
        }

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;

        if (i==VIEW_TYPE_MESSAGE_SEND){
            view = LayoutInflater.from(context).inflate(R.layout.view_chat_send_message,viewGroup,false);
            return new SendMessageHolder(view);
        }else if (i==VIEW_TYPE_MESSAGE_RECIEVE){
            view = LayoutInflater.from(context).inflate(R.layout.view_chat_recieve_message,viewGroup,false);
            return new RecieveMessageHolder(view);
        }else if (i==VIEW_TYPE_IMAGE_SEND){
            view = LayoutInflater.from(context).inflate(R.layout.view_chat_send_image,viewGroup,false);
            return new SendImageHolder(view);

        }else  if (i==VIEW_TYPE_IMAGE_RECIEVE){
            view = LayoutInflater.from(context).inflate(R.layout.view_chat_recieve_image,viewGroup,false);
            return new RecieveImageHolder(view);
        }

        return null;
    }



    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ChatData chatData = messages.get(i);

        switch (viewHolder.getItemViewType()){
            case VIEW_TYPE_MESSAGE_SEND:
                ((SendMessageHolder) viewHolder).bind(chatData.getMessageData());
                break;
            case VIEW_TYPE_MESSAGE_RECIEVE:
                ((RecieveMessageHolder)viewHolder).bind(chatData.getMessageData());
                break;
            case VIEW_TYPE_IMAGE_SEND:
                ((SendImageHolder)viewHolder).bind(chatData.getImageData());
                break;
            case VIEW_TYPE_IMAGE_RECIEVE:
                ((RecieveImageHolder)viewHolder).bind(chatData.getImageData());
                break;
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
    public void addItem(MessageData message){
        messages.add(new ChatData(message));
        notifyItemInserted(messages.size());
    }
    public void addItem(ImageData imageData){
        messages.add(new ChatData(imageData));
        notifyItemInserted(messages.size());
    }
}
