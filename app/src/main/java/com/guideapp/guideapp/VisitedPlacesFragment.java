package com.guideapp.guideapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class VisitedPlacesFragment extends Fragment {

    public VisitedPlacesFragment (ImageView vPlacesIcon, ImageView mapIcon) {
        thisVPlacesIcon = vPlacesIcon;
        thisMapIcon = mapIcon;
    }

    RelativeLayout nullVPlaces;
    Button goToMapButton;
    ImageView thisVPlacesIcon;
    ImageView thisMapIcon;

    android.support.v7.app.ActionBar actionBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View fragmentView = inflater.inflate(R.layout.fragment_visited_places, container, false);

        nullVPlaces = fragmentView.findViewById(R.id.null_vplaces);

        goToMapButton = fragmentView.findViewById(R.id.go_to_map);

        if (Database.VISITED_PLACES.isEmpty()) {
            nullVPlaces.setVisibility(View.VISIBLE);
        }

        goToMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thisVPlacesIcon.setColorFilter(ContextCompat.getColor(getActivity(), R.color.uncolored), android.graphics.PorterDuff.Mode.SRC_IN);
                thisMapIcon.setColorFilter(ContextCompat.getColor(getActivity(), R.color.colored), android.graphics.PorterDuff.Mode.SRC_IN);

                Fragment fragment = new MapFragment(0);
                if (fragment != null) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.bg_content, fragment);
                    fragmentTransaction.commit();
                }
            }
        });

        return fragmentView;
    }

}
