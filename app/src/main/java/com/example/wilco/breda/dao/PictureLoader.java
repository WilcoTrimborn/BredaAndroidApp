package com.example.wilco.breda.dao;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by Wilco on 25-6-2018.
 */

public class PictureLoader {

    public void getListViewPhoto(ImageView photo, String URL) {
        Picasso.get().load(URL).resize(1000, 250).into(photo);
    }

    public void getDetailViewPicture(ImageView photo, String URL) {
        Picasso.get().load(URL).resize(1400, 2180).into(photo);
    }

}
