package com.guideapp.guideapp;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.maps.model.LatLng;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class ExcursionPreviewFragment extends Fragment {

    String previewImgUrl;
    ImageView previewImg;
    TextView title;
    TextView description;
    LinearLayout start;
    String[] names = {"Эрмитаж", "Русский музей", "Летний сад", "Спас на Крови", "Михайловский замок"};

    public ExcursionPreviewFragment(String previewImgUrl) {
        this.previewImgUrl = previewImgUrl;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_city_preview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        previewImg = view.findViewById(R.id.preview_img);
        title = view.findViewById(R.id.preview_title);
        description = view.findViewById(R.id.preview_description);
        start = view.findViewById(R.id.start_excursion);

        Animation animation3 = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_top);

        Glide
                .with(this)
                .load(previewImgUrl)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        previewImg.startAnimation(animation3);
                        return false;
                    }
                })
                .into(previewImg);

        start.setOnClickListener(v -> {
            closeFragment();
            openFragment(new PlacesHintsFragment());
            for (String name : names) {
                Database.load("https://maps.googleapis.com/maps/api/place/textsearch/json?input=" + name + "&language=ru&key=AIzaSyCZ2QPsrCzN8KrTE234GujTFlaRQQjQ5oI");
            }
        });
    }

    private void closeFragment() {
        getFragmentManager().popBackStack();
    }

    private void openFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.place_hint_frame, fragment, "hints");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
    }
}
