package com.scenekey.models;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by mindiii-rahul on 1/5/17.
 */

public class ImagesUplode implements Serializable {
    Bitmap bitmap;

    public ImagesUplode(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
