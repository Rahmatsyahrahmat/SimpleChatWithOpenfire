package com.rahmatsyah.simlplechatwithopenfire.holder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.rahmatsyah.simlplechatwithopenfire.R;
import com.rahmatsyah.simlplechatwithopenfire.model.MessageData;

public class SendMessageHolder extends RecyclerView.ViewHolder {

    private TextView message;

    public SendMessageHolder(@NonNull View itemView) {
        super(itemView);
        message = itemView.findViewById(R.id.sendMessageBody);
    }
    public void bind(MessageData message){
        this.message.setText(message.getMessage());
    }
}
