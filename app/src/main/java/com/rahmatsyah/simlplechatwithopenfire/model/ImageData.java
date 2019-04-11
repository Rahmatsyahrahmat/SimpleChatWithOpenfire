package com.rahmatsyah.simlplechatwithopenfire.model;

import android.graphics.Bitmap;

public class ImageData {
    private String heding;
    private Bitmap bitmap;

    public ImageData(String heding, Bitmap bitmap) {
        this.heding = heding;
        this.bitmap = bitmap;
    }

    public String getHeding() {
        return heding;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
