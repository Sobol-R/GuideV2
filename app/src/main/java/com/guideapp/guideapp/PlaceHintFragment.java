package com.guideapp.guideapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PlaceHintFragment extends Fragment {

    TextView placeName;
    TextView placeRating;
    TextView placeType;
    TextView placeOpen;

    Place thisPlace;

    public PlaceHintFragment(Place place) {
        thisPlace = place;
    }

    RelativeLayout placeHint;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_place_hint, container, false);

        placeHint = fragmentView.findViewById(R.id.place_hint);

        placeName = fragmentView.findViewById(R.id.place_name);
        placeRating = fragmentView.findViewById(R.id.place_rating);
        placeType = fragmentView.findViewById(R.id.place_type);
        placeOpen = fragmentView.findViewById(R.id.place_open);

        placeName.setText(thisPlace.name);
        placeRating.setText(String.format("Рейтинг: %s", thisPlace.rating));

        final Fragment fragment = this;

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFragment(fragment);
                openPlaceDescriptionFragment();
            }
        };

        fragmentView.setOnClickListener(onClickListener);

        return fragmentView;
    }

    public void closeFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        fragmentTransaction.commit();
    }

    public void openPlaceDescriptionFragment() {
        Fragment fragment = new PlaceDescriptionFragment(thisPlace);
        if (fragment != null) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.bg_content, fragment);
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTransaction.commit();
        }
    }
}
