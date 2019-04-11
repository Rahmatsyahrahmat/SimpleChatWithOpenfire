package com.rahmatsyah.simlplechatwithopenfire.holder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.rahmatsyah.simlplechatwithopenfire.R;
import com.rahmatsyah.simlplechatwithopenfire.model.FileData;
import com.rahmatsyah.simlplechatwithopenfire.model.ImageData;

public class RecieveFileHolder extends RecyclerView.ViewHolder {

    private TextView message,sender;

    public RecieveFileHolder(@NonNull View itemView) {
        super(itemView);
        message = itemView.findViewById(R.id.recicveFileBody);
        sender = itemView.findViewById(R.id.recieveFileName);
    }

    public void bind(FileData message){
        sender.setText(message.getHeding());
        this.message.setText(message.getFileName());
    }

}
