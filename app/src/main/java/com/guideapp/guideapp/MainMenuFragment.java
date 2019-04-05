package com.guideapp.guideapp;

import android.animation.Animator;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class MainMenuFragment extends Fragment {

    ImageView close;
    FrameLayout cont;
    LinearLayout excursion;
    LinearLayout placesPhotos;

    public MainMenuFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        close = view.findViewById(R.id.close_main_menu);
        cont = view.findViewById(R.id.main_menu_cont);
        excursion = view.findViewById(R.id.open_excursion_menu);
        placesPhotos = view.findViewById(R.id.places_photos);

        final View.OnClickListener closeFragment = v -> closeFragment();

        close.setOnClickListener(closeFragment);
        cont.setOnClickListener(closeFragment);
        excursion.setOnClickListener(v -> loadExcursion());
        placesPhotos.setOnClickListener(v -> openFragment(new SolvedExcursionsFragment()));

    }

    private void loadExcursion() {
//        closeFragment();
//        MapFragment.test();
        openFragment(new ExcursionsFragment());
    }

    private void closeFragment() {
        getFragmentManager().popBackStack();
    }

    private void openFragment(Fragment fragment) {
        closeFragment();
        final FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fg_content, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
    }
}
