package com.guideapp.guideapp;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class PlacesHintsAdapter extends RecyclerView.Adapter<PlacesHintsHolder> {

    MainActivity mainActivity;
    ArrayList <Place> places;

    public PlacesHintsAdapter(MainActivity mainActivity, ArrayList<Place> places) {
        this.mainActivity = mainActivity;
        this.places = places;
    }

    @NonNull
    @Override
    public PlacesHintsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        View view = layoutInflater.inflate(R.layout.fragment_place_hint, viewGroup, false);
        PlacesHintsHolder vH = new PlacesHintsHolder(view);
        return vH;
    }

    @Override
    public void onBindViewHolder(@NonNull PlacesHintsHolder placesHintsHolder, int i) {
        Place place = places.get(i);
        placesHintsHolder.placeName.setText(place.name);
        placesHintsHolder.placeAddress.setText(place.address);
        placesHintsHolder.itemView.setOnClickListener(v -> openFragment(place));
    }

    public void openFragment(Place place) {
        new RequestUtils(setPhotoUrl(place), RequestType.PHOTOS).sendRequest();
        new RequestUtils(setWikiUrl(place), RequestType.WIKIPEDIA).sendRequest();
        FragmentManager fragmentManager = mainActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.place_hint_frame, new PlaceDescriptionFragment(place), "desc");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
    }

    public String setPhotoUrl(Place place) {
        return "https://maps.googleapis.com/maps/api/place/details/json?placeid="
                + place.getId() +
                "&fields=photo&key=AIzaSyCZ2QPsrCzN8KrTE234GujTFlaRQQjQ5oI";
    }

    private String setWikiUrl(Place place) {
        return "https://ru.wikipedia.org/api/rest_v1/page/summary/" + place.getName().toString().replace(' ', '_');
    }

    @Override
    public int getItemCount() {
        return places.size();
    }
}
