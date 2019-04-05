package com.guideapp.guideapp;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ExcursionsFragment extends Fragment {

    String url = "https://for-travels.ru/wp-content/uploads/2017/06/dostoprimechatelnosti-spb-1.jpg";
    ImageView img;
    CardView excursCont;

    public ExcursionsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_excursions_, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        img = view.findViewById(R.id.exurs_img);
        excursCont = view.findViewById(R.id.excursion_cont);

        Glide.with(this)
                .load(url)
                .into(img);

        excursCont.setOnClickListener(v ->
                openFragment(
                        new ExcursionPreviewFragment(
                                "https://english.spbstu.ru/upload/medialibrary/42f/107.jpg")
                )
        );
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
