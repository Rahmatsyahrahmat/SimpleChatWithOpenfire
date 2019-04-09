package com.rahmatsyah.simlplechatwithopenfire.holder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rahmatsyah.simlplechatwithopenfire.R;
import com.rahmatsyah.simlplechatwithopenfire.model.ImageData;
import com.rahmatsyah.simlplechatwithopenfire.model.MessageData;

public class RecieveImageHolder extends RecyclerView.ViewHolder {

    private TextView sender;
    private ImageView image;

    public RecieveImageHolder(@NonNull View itemView) {
        super(itemView);
        sender = itemView.findViewById(R.id.recieveImageName);
        image = itemView.findViewById(R.id.recieveImageBody);
    }
    public void bind(ImageData message){
        sender.setText(message.getHeding());
        image.setImageBitmap(message.getBitmap());
    }
}
