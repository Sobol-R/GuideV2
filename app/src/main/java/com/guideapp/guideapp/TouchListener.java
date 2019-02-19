package com.guideapp.guideapp;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.io.FileReader;


public class TouchListener implements View.OnTouchListener {

    float dY;
    float startPos;
    Context context;
    Fragment fragment;
    float moving = 0;

    public TouchListener(Context context, Fragment fragment) {
        this.context = context;
        this.fragment = fragment;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN :
                dY = v.getY() - event.getRawY();
                startPos = v.getY();

            case MotionEvent.ACTION_MOVE :
                if (event.getRawY() + dY > 0 || fragment instanceof PlaceHintFragment) {
                    moving = startPos - v.getY();
                    Log.d("move", String.valueOf(moving));
                    v.animate()
                            .alpha(1 - (Math.abs(moving) / (v.getHeight()/100) / 100))
                            .y(event.getRawY() + dY)
                            .setDuration(0)
                            .start();
                }
                break;

            case MotionEvent.ACTION_UP :
                if (startPos < v.getY()) {
                    fragment.getFragmentManager().popBackStack();
                }
                else if (startPos > v.getY() && fragment instanceof PlaceHintFragment) {
                    ((PlaceHintFragment) fragment).openFragment();
                }

                default: return false;
        }

        return true;
    }

    public float getDp(float px){
        return px / ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

}
