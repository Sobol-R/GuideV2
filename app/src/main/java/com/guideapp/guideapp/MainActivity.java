package com.guideapp.guideapp;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
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

import com.mancj.slideup.SlideUp;
import com.mancj.slideup.SlideUpBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    FrameLayout fgContent;
    LinearLayout search;
    LinearLayout menu;

//    float fromX = 0;
//    float fromY = 130;
//    float toX;
//    float toY = -100;

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
                changeFragment(new SearchPlaceFragment());
                search.setVisibility(View.GONE);
                menu.setVisibility(View.GONE);
            }
        });

        changeFragment(new MapFragment(1));

        checkLocationPermission();
    }

//    private void animateDiagonalPan(View v, boolean type) {
//        AnimatorSet animSetXY = new AnimatorSet();
//
//        if (type) {
//            if (fromX == 0) {
//                fromX = (v.getId() == R.id.menu_button ? +130 : v.getX() - 60);
//            }
//            toX = (v.getId() == R.id.menu_button ? -100 : 100);
//        } else {
//            float x = fromX;
//            fromY = -100;
//            fromY = toY;
//            toY = 100;
//            fromX = toX;
//            toX = x;
//        }
//
//        ObjectAnimator y = ObjectAnimator.ofFloat(v,
//                "translationY",fromY, toY);
//
//        ObjectAnimator x = ObjectAnimator.ofFloat(v,
//                "translationX", fromX, toX);
//
//        animSetXY.playTogether(x, y);
//        animSetXY.setInterpolator(new LinearInterpolator());
//        animSetXY.setDuration(3300);
//        animSetXY.start();
//
//    }

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
