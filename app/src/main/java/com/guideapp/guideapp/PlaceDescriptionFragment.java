package com.guideapp.guideapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.guideapp.guideapp.ttsapi.SendTTSRequest;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class PlaceDescriptionFragment extends Fragment {

    TextView name;
    ViewPager viewPager;
    CircleIndicator indicator;
    List <String> photoReferences;
    String description;
    com.google.android.gms.location.places.Place place;
    RelativeLayout content;
    TextView openDescription;
    LinearLayout playAudio;

    public PlaceDescriptionFragment(com.google.android.gms.location.places.Place place) {
        this.place = place;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_place_description, container, false);

        fragmentView.bringToFront();

        openDescription = fragmentView.findViewById(R.id.open_description);
        viewPager = fragmentView.findViewById(R.id.view_pager);
        indicator = fragmentView.findViewById(R.id.indicator);
        name = fragmentView.findViewById(R.id.name);
        content = fragmentView.findViewById(R.id.description_content);
        playAudio = fragmentView.findViewById(R.id.play_audio);

        name.setText(place.getName());
        final Animation topSlideAnim = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_top);
        content.setAnimation(topSlideAnim);
        topSlideAnim.start();

        content.setOnTouchListener(new TouchListener(getContext(), this));

        openDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ReadDescriptionDialog(place.getName().toString(), description).show(getFragmentManager(), "description");
            }
        });

        playAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SendTTSRequest(description).getToken();
            }
        });

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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWikipediaEvent(Parse.WikipediaEvent event) {
        description = event.text;
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
