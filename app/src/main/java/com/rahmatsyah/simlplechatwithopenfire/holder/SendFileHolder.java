package com.rahmatsyah.simlplechatwithopenfire.holder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.rahmatsyah.simlplechatwithopenfire.R;
import com.rahmatsyah.simlplechatwithopenfire.model.FileData;

public class SendFileHolder extends RecyclerView.ViewHolder {

    private TextView message;

    public SendFileHolder(@NonNull View itemView) {
        super(itemView);
        message = itemView.findViewById(R.id.sendFileBody);
    }
    public void bind(FileData message){
        this.message.setText(message.getFileName());
    }
}
