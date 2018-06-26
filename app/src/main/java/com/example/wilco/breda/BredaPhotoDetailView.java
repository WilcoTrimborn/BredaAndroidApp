package com.example.wilco.breda;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.example.wilco.breda.dao.PictureLoader;
import com.example.wilco.breda.domain.BredaPhoto;

/**
 * Created by Wilco on 25-6-2018.
 */

public class BredaPhotoDetailView extends AppCompatActivity {

    private PictureLoader pictureLoader;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bredaphoto_detail);
        Intent i = getIntent();
        this.pictureLoader = new PictureLoader();

        BredaPhoto nf = (BredaPhoto) i.getSerializableExtra("BredaPhoto");

//        TextView photocameraName = findViewById(R.id.photoCameraNameTextView);
//        photocameraName.setText(nf.getCameraName());

        ImageView photoImageView = findViewById(R.id.bigBredaImageView);
        this.pictureLoader.getDetailViewPicture(photoImageView,nf.getPhotoUrl());
    }

}
