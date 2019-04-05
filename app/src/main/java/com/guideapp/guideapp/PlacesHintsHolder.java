package com.guideapp.guideapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PlacesHintsHolder extends RecyclerView.ViewHolder {

    View itemView;
    RelativeLayout placeHint;
    LinearLayout makeRoute;
    TextView placeName;
    TextView placeAddress;
    TextView placeRating;

    public PlacesHintsHolder(@NonNull View itemView) {
        super(itemView);
        this.itemView = itemView;
        placeHint = itemView.findViewById(R.id.place_hint);
        makeRoute = itemView.findViewById(R.id.make_route);
        placeName = itemView.findViewById(R.id.place_name);
        placeAddress = itemView.findViewById(R.id.place_address);
        placeRating = itemView.findViewById(R.id.place_rating);
    }
}
