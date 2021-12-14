package com.example.myapp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;

public class TrackerCategory {
    String category;
   int img;

    public TrackerCategory() {
    }

    public TrackerCategory(String category, int img) {

        this.category = category;
        this.img = img;
    }

    public String getCategory() {
        return category;
    }

    public int getImg() {
        return img;
    }
}
