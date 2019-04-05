package com.guideapp.guideapp;

import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nullable;

public class Place implements com.google.android.gms.location.places.Place {

    public Place (String address, double latitude, double longitude, String name, String placeId) {
        this.address = address;
        this.placeId = placeId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
    }

    String placeId;
    double latitude;
    double longitude;
    String name;
    String address;

    @Override
    public String getId() {
        return placeId;
    }

    @Override
    public List<Integer> getPlaceTypes() {
        return null;
    }

    @Nullable
    @Override
    public CharSequence getAddress() {
        return address;
    }

    @Override
    public Locale getLocale() {
        return null;
    }

    @Override
    public CharSequence getName() {
        return name;
    }

    @Override
    public LatLng getLatLng() {
        return new LatLng(latitude, longitude);
    }

    @Nullable
    @Override
    public LatLngBounds getViewport() {
        return null;
    }

    @Nullable
    @Override
    public Uri getWebsiteUri() {
        return null;
    }

    @Nullable
    @Override
    public CharSequence getPhoneNumber() {
        return null;
    }

    @Override
    public float getRating() {
        return 0;
    }

    @Override
    public int getPriceLevel() {
        return 0;
    }

    @Nullable
    @Override
    public CharSequence getAttributions() {
        return null;
    }

    @Override
    public com.google.android.gms.location.places.Place freeze() {
        return null;
    }

    @Override
    public boolean isDataValid() {
        return false;
    }
}
