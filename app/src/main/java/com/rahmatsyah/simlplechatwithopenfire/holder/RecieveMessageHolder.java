package com.rahmatsyah.simlplechatwithopenfire.holder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.rahmatsyah.simlplechatwithopenfire.R;
import com.rahmatsyah.simlplechatwithopenfire.model.MessageData;

public class RecieveMessageHolder extends RecyclerView.ViewHolder {
    private TextView sender, message;

    public RecieveMessageHolder(@NonNull View itemView) {
        super(itemView);
        sender = itemView.findViewById(R.id.recieveMessageName);
        message = itemView.findViewById(R.id.recieveMessageBody);
    }

    public void bind(MessageData message){
        sender.setText(message.getHeding());
        this.message.setText(message.getMessage());
    }

}
