package com.guideapp.guideapp;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mancj.slideup.SlideUp;
import com.mancj.slideup.SlideUpBuilder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import me.relex.circleindicator.CircleIndicator;

import static com.bumptech.glide.request.RequestOptions.centerCropTransform;
import static com.bumptech.glide.request.RequestOptions.fitCenterTransform;

public class PlaceDescriptionFragment extends Fragment {

    ViewPager viewPager;
    CircleIndicator indicator;
    List <String> photoReferences;

    public PlaceDescriptionFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_place_description, container, false);

        viewPager = fragmentView.findViewById(R.id.view_pager);
        indicator = fragmentView.findViewById(R.id.indicator);

        return fragmentView;
    }

    private void setAdapter() {
        final ImageViewPagerAdapter adapter = new ImageViewPagerAdapter(getFragmentManager(), photoReferences);
        Log.d("pr", photoReferences.toString());
        viewPager.setAdapter(adapter);
        indicator.setViewPager(viewPager);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPhotoEvent(PhotoUtils.PhotoEvent event) {
        this.photoReferences = event.references;
        setAdapter();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
