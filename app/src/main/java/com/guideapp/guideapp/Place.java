package com.guideapp.guideapp;

import java.io.Serializable;

public class Place implements Serializable {

    public Place (double latitude, double longitude, String iconPath, String id, String name
                      /*boolean open*/, String placeId, double rating, String vicinity, String photoLink
                  /*String type*/) {
        this.placeId = placeId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.iconPath = iconPath;
        this.name = name;
        this.rating = rating;
        this.vicinity = vicinity;
        this.id = id;
        this.photoLink = photoLink;
       // this.type = type;
        //this.open = open;
    }

    String placeId;
    double latitude;
    double longitude;
    String iconPath;
    String name;
    double rating;
    String vicinity;
    String id;
    String photoLink;
    boolean choosen = false;
    //String type;
    // boolean open;
}
