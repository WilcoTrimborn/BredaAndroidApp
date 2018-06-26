package com.example.wilco.breda.applogic;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wilco.breda.R;
import com.example.wilco.breda.dao.PictureLoader;
import com.example.wilco.breda.domain.BredaPhoto;

import java.util.ArrayList;

/**
 * Created by Wilco on 25-6-2018.
 */

public class BredaPhotoAdapter extends BaseAdapter {

    //LayoutInflater is used to let the custom view in a ListView work properly.
    //It creates a new View (or Layout) object from one of the xml layouts.
    private LayoutInflater layoutInflater;
    private ArrayList BredaPhotoArray;
    private PictureLoader pictureLoader;

    public BredaPhotoAdapter(LayoutInflater layoutInflater,ArrayList BredaPhotoArray) {
        this.layoutInflater = layoutInflater;
        this.BredaPhotoArray = BredaPhotoArray;
        this.pictureLoader = new PictureLoader();
    }

    @Override
    public int getCount() {
        int arraySize = 0;
        if (this.BredaPhotoArray != null) {
            arraySize = this.BredaPhotoArray.size();
        }
        return arraySize;
    }

    @Override
    public Object getItem(int position) {
        return (BredaPhoto) BredaPhotoArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = this.layoutInflater.inflate(R.layout.activity_listview_item, null);
            viewHolder = new ViewHolder();
            viewHolder.idTextView = convertView.findViewById(R.id.photoIdTextView);
            viewHolder.photoImageView = convertView.findViewById(R.id.photoImageView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        BredaPhoto nf = (BredaPhoto) BredaPhotoArray.get(position);
        viewHolder.idTextView.setText(nf.getPhotoID());
        this.pictureLoader.getListViewPhoto(viewHolder.photoImageView, nf.getPhotoUrl());
        return convertView;
    }

    private static class ViewHolder {
        ImageView photoImageView;
        TextView idTextView;
    }
}
