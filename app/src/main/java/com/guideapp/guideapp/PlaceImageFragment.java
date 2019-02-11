package com.guideapp.guideapp;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class PlaceImageFragment extends Fragment {

    String reference;
    ImageView imageView;
    String url;

    public PlaceImageFragment(String reference) {
        this.reference = reference;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_place_image, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageView = view.findViewById(R.id.place_fragment_image);
        url = setUrl();

        Glide.with(this)
                .load(url)
                .into(imageView);
    }

    private String setUrl() {
        return
                "https://maps.googleapis.com/maps/api/place/photo?maxwidth=1200&photoreference="
                + reference
                + "&key=AIzaSyCZ2QPsrCzN8KrTE234GujTFlaRQQjQ5oI";
    }
}
