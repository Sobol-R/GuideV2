package com.guideapp.guideapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

public class SolvedPlacesAdapter extends RecyclerView.Adapter<SolvedPlaceHolder> {

    MainActivity mainActivity;

    public SolvedPlacesAdapter(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public SolvedPlaceHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        View view = layoutInflater.inflate(R.layout.solved_place_item, viewGroup, false);
        SolvedPlaceHolder vH = new SolvedPlaceHolder(view);
        return vH;
    }

    @Override
    public void onBindViewHolder(@NonNull SolvedPlaceHolder solvedPlaceHolder, int i) {
        Glide
                .with(mainActivity)
                .load(setUrl(i))
                .into(solvedPlaceHolder.imageView);

        solvedPlaceHolder.textView.setText(Database.PLACES_NAMES.get(i));
    }

    private String setUrl(int i) {
        return
                "https://maps.googleapis.com/maps/api/place/photo?maxwidth=1200&photoreference="
                        + Database.PHOTOS.get(i)
                        + "&key=AIzaSyCZ2QPsrCzN8KrTE234GujTFlaRQQjQ5oI";
    }

    @Override
    public int getItemCount() {
        return Database.PHOTOS.size();
    }
}
