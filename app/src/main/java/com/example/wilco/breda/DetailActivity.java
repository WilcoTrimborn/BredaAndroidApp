package com.example.wilco.breda;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.example.wilco.breda.R;
import com.example.wilco.breda.domain.BredaPhoto;
import com.example.wilco.breda.dao.PictureLoader;

/**
 * Created by Wilco on 25-6-2018.
 */

public class DetailActivity extends AppCompatActivity {

    private PictureLoader pictureLoader;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bredaphoto_detail);
        Intent i = getIntent();
        this.pictureLoader = new PictureLoader();

        BredaPhoto nf = (BredaPhoto) i.getSerializableExtra("BredaPhoto");

//        TextView photoCameraName = findViewById(R.id.photoCameraNameTextView);
//        photoCameraName.setText(nf.getCameraName());

        ImageView photoImageView = findViewById(R.id.bigBredaImageView);
        this.pictureLoader.getDetailViewPicture(photoImageView, nf.getPhotoUrl());
    }

}
