package com.guideapp.guideapp;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.DragAndDropPermissions;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.*;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.mancj.slideup.SlideUp;
import com.mancj.slideup.SlideUpBuilder;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    FrameLayout fgContent;
    LinearLayout search;
    LinearLayout menu;

    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fgContent = findViewById(R.id.fg_content);

        search = findViewById(R.id.search_button);
        menu = findViewById(R.id.menu_button);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //changeFragment(new SearchPlaceFragment());
                try {
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                            .build(MainActivity.this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
            }
        });

        changeFragment(new MapFragment(1));

        checkLocationPermission();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                String url = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=" +
                        place.getAddress().toString().replace(' ', '+') +
                        "&key=AIzaSyCZ2QPsrCzN8KrTE234GujTFlaRQQjQ5oI";
                RequestUtils requestUtils = new RequestUtils(url, "address");
                requestUtils.sendRequest();
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    public static void parse(String data) throws JSONException {
        JSONObject object = new JSONObject(data);
        JSONArray results = object.getJSONArray("results");
        JSONObject result = results.getJSONObject(0);
        double latitude = result.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
        double longitude = result.getJSONObject("geometry").getJSONObject("location").getDouble("lng");
        EventBus.getDefault().post(new SearchPlaceFragment.AddressEvent(latitude, longitude));
    }

    private void changeFragment(final Fragment fragment) {
        final FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                List<Fragment> fragments = fragmentManager.getFragments();
                int i = fragments.size() - 1;
                Fragment currentFragment = fragments.get(i);
                if (currentFragment instanceof MapFragment) {
                    search.setVisibility(View.VISIBLE);
                    menu.setVisibility(View.VISIBLE);
                }
            }
        });

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.bg_content, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
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
