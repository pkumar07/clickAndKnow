package com.example.poorn.clickandknow;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by poorn on 11/8/2017.
 */

public class UpdateData {
    public int position;
    public String caption;
    public ArrayList<String> tags;
    public Bitmap ObjectImage;

    public UpdateData(){
        position = 0;
        caption = new String();
        tags = new ArrayList<>();
        ObjectImage = null;
    }
}
