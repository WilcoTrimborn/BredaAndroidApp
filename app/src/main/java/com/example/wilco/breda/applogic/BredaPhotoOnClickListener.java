package com.example.wilco.breda.applogic;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import com.example.wilco.breda.DetailActivity;
import com.example.wilco.breda.domain.BredaPhoto;

/**
 * Created by Wilco on 25-6-2018.
 */

public class BredaPhotoOnClickListener implements AdapterView.OnItemClickListener {

    private Context context;

    public BredaPhotoOnClickListener(Context context) {
        this.context = context;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
      BredaPhoto nf = (BredaPhoto) parent.getItemAtPosition(position);
      Intent intent = new Intent(context, DetailActivity.class);
      intent.putExtra("BredaPhoto", nf);
      context.startActivity(intent);
    }
}
