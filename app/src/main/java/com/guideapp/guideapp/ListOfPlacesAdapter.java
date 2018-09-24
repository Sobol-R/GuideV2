package com.guideapp.guideapp;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.bumptech.glide.Glide;

import static com.bumptech.glide.request.RequestOptions.centerCropTransform;
import static com.bumptech.glide.request.RequestOptions.fitCenterTransform;

public class ListOfPlacesAdapter extends RecyclerView.Adapter <ListOfPlacesViewHolder> {

    MainActivity mainActivity;
    RecyclerView recyclerView;

    String photoRequestBegin;
    String photoRequestEnd;
    FrameLayout fgContent;

    public ListOfPlacesAdapter(MainActivity mainActivity, FrameLayout fgContent) {
        this.mainActivity = mainActivity;
        this.fgContent = fgContent;
    }

    @NonNull
    @Override
    public ListOfPlacesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        View view = layoutInflater.inflate(R.layout.list_of_places_item_layout, parent, false);
        ListOfPlacesViewHolder vH = new ListOfPlacesViewHolder(view);
        return vH;
    }

    @Override
    public void onBindViewHolder(@NonNull ListOfPlacesViewHolder holder, final int position) {
        recyclerView = mainActivity.findViewById(R.id.list_of_places_recycler_view);
        final Place place = Database.PLACES.get(position);

        holder.nameOfPlace.setText(place.name);

        photoRequestBegin = "https://maps.googleapis.com/maps/api/place/photo?maxheight=1000&photoreference=";
        photoRequestEnd = "&key=AIzaSyCZ2QPsrCzN8KrTE234GujTFlaRQQjQ5oI";

        Glide
                .with(mainActivity)
                .load(photoRequestBegin + place.photoLink + photoRequestEnd)
                .apply(fitCenterTransform())
                .apply(centerCropTransform())
                .into(holder.imageOfPlace);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(place);
            }
        });

    }

    public void onItemClick(Place place) {
        place.choosen = true;
        fgContent.setVisibility(View.GONE);
        Fragment fragment = new MapFragment(0);
        FragmentManager fragmentManager = mainActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.replace(R.id.bg_content, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public int getItemCount() {
        return Database.PLACES.size();
    }
}
