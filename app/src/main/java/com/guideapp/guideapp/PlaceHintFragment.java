package com.guideapp.guideapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.api.internal.LifecycleFragment;
import com.google.android.gms.location.places.*;

import java.util.List;

public class PlaceHintFragment extends Fragment {

    RelativeLayout placeHint;
    LinearLayout makeRoute;

    TextView placeName;
    TextView placeAddress;
    TextView placeRating;
    TextView placeType;
    TextView placeOpen;

    com.google.android.gms.location.places.Place place;

    double userLat;
    double userLng;

    String photoUrl;

    public PlaceHintFragment(com.google.android.gms.location.places.Place place, double userLat, double userLng) {
        this.place = place;
        this.userLat = userLat;
        this.userLng = userLng;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_place_hint, container, false);

        placeHint = fragmentView.findViewById(R.id.place_hint);
        makeRoute = fragmentView.findViewById(R.id.make_route);

        placeName = fragmentView.findViewById(R.id.place_name);
        placeAddress = fragmentView.findViewById(R.id.place_address);
        placeRating = fragmentView.findViewById(R.id.place_rating);
        placeType = fragmentView.findViewById(R.id.place_type);
        placeOpen = fragmentView.findViewById(R.id.place_open);

        placeName.setText(String.valueOf(place.getName()));
        placeAddress.setText(String.valueOf(place.getAddress()));
        placeRating.setText(String.valueOf("Рейтинг: " + place.getRating()));
        placeType.setText(String.valueOf("Тип: " + place.getPlaceTypes().get(0)));

        Animation topSlideAnim = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_top);
        Animation bottomSlideAnim = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_bottom);

        placeHint.setAnimation(topSlideAnim);
        makeRoute.setAnimation(topSlideAnim);

        photoUrl = setPhotoUrl();
        Log.d("pr", photoUrl);

        makeRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RoutesUtils routesUtils = new RoutesUtils(userLat, userLng,
                        place.getLatLng().latitude, place.getLatLng().longitude);
                routesUtils.sendRouteRequest();
                getFragmentManager().popBackStack();
            }
        });

        fragmentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment();
            }
        });

        return fragmentView;
    }

    private void openFragment() {
        new RequestUtils(setPhotoUrl(), RequestType.PHOTOS).sendRequest();
        new RequestUtils(setWikiUrl(), RequestType.WIKIPEDIA).sendRequest();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.place_hint_frame, new PlaceDescriptionFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
    }

    public String setPhotoUrl() {
        return "https://maps.googleapis.com/maps/api/place/details/json?placeid="
                + place.getId() +
                "&fields=photo&key=AIzaSyCZ2QPsrCzN8KrTE234GujTFlaRQQjQ5oI";
    }

    private String setWikiUrl() {
        return "https://ru.wikipedia.org/api/rest_v1/page/summary/" + place.getName().toString().replace(' ', '_');
    }
}
