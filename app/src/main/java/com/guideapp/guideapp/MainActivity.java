package com.guideapp.guideapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ImageView mapIcon;
    ImageView placeIcon;
    ImageView galleryIcon;
    LinearLayout map;
    LinearLayout vPlaces;
    LinearLayout gallery;

    LinearLayout colored;

    Button choosePlace;
    Button showPlaces;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapIcon = findViewById(R.id.map_icon);
        placeIcon = findViewById(R.id.place_icon);
        galleryIcon = findViewById(R.id.gallery_icon);


        map = findViewById(R.id.map_cont);
        vPlaces = findViewById(R.id.place_cont);
        gallery = findViewById(R.id.gallery_cont);

        choosePlace = findViewById(R.id.choose_place);
        showPlaces = findViewById(R.id.show_places);

        final Map <LinearLayout, ImageView> menuMp = new HashMap<LinearLayout, ImageView>() {
            {
                put(map, mapIcon);
                put(vPlaces, placeIcon);
                put(gallery, galleryIcon);
            }
        };

        colored = map;

        final Context context = this;

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout linearLayout = (LinearLayout) v;

                menuMp.get(colored).setColorFilter(ContextCompat.getColor(context, R.color.uncolored), android.graphics.PorterDuff.Mode.SRC_IN);
                colored = linearLayout;
                menuMp.get(colored).setColorFilter(ContextCompat.getColor(context, R.color.colored), android.graphics.PorterDuff.Mode.SRC_IN);

                Fragment fragment = null;

                if (linearLayout == map) {
                    fragment = new MapFragment(null);
                } else if (linearLayout == vPlaces) {
                    fragment = new VisitedPlacesFragment(placeIcon, mapIcon);
                }

                if (fragment != null) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content, fragment);
                    fragmentTransaction.commit();
                }
            }
        };

        Fragment fragment = new MapFragment(null);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content, fragment);
        fragmentTransaction.commit();

        map.setOnClickListener(onClickListener);
        vPlaces.setOnClickListener(onClickListener);
        gallery.setOnClickListener(onClickListener);

        showPlaces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ListOfPlacesFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.replace(R.id.content, fragment);
                fragmentTransaction.commit();
            }
        });

        checkLocationPermission();

        Database.load(0, 0, null);
    }

    public void checkLocationPermission() {
        final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 123;
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                String explanation = "Для корректной работы необходимо разрешить приложению использовать вашу геолокацию";
                Toast toast = Toast.makeText(this, explanation, Toast.LENGTH_LONG);
                toast.show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
        }
    }
}
