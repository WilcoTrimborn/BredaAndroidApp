package com.example.wilco.breda.domain;

import java.io.Serializable;

/**
 * Created by Wilco on 25-6-2018.
 */

//Via Java Serialization you can stream your Java object to a sequence of byte and restore these objects from this stream of bytes.
public class BredaPhoto implements Serializable {

    private String photoID;
    private String location;
    private String artist;
    private String description;
    private String material;
    private String underground;
    private String photourl;

    public BredaPhoto(String photoID, String location, String artist, String description, String material, String underground, String photourl) {
        this.photoID = photoID;
        this.location = location;
        this.artist = artist;
        this.description = description;
        this.material = material;
        this.underground = underground;
        this.photourl = photourl;
    }

    public String getPhotoID() {
        return photoID;
    }

    public String getLocation() {
        return location;
    }

    public String getArtist() {
        return artist;
    }

    public String getDescription() {
        return description;
    }

    public String getMaterial() {
        return material;
    }

    public String getUnderground() {
        return underground;
    }

    public String getPhotoUrl() {
        return photourl;
    }
}
