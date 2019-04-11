package com.rahmatsyah.simlplechatwithopenfire.holder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.rahmatsyah.simlplechatwithopenfire.R;
import com.rahmatsyah.simlplechatwithopenfire.model.ImageData;

public class SendImageHolder extends RecyclerView.ViewHolder {
    private ImageView imageView;
    public SendImageHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.sendImageBody);
    }
    public void bind(ImageData message){
        imageView.setImageBitmap(message.getBitmap());
    }
}
