package com.guideapp.guideapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.FloatRange;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

public class ChoosePlaceTypeFragment extends Fragment {

    FrameLayout fgContent;

    public ChoosePlaceTypeFragment(FrameLayout fgContent) {
        this.fgContent = fgContent;
    }

    Button nearMuseums;
    Button allMuseums;
    Button nearCafes;
    Button allCafes;

    View fragmentView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_choose_place_type, container, false);

        nearMuseums = fragmentView.findViewById(R.id.near_museums);
        allMuseums = fragmentView.findViewById(R.id.all_museums);
        nearCafes = fragmentView.findViewById(R.id.near_cafes);
        allCafes = fragmentView.findViewById(R.id.all_cafes);

        nearMuseums.setOnClickListener(getOnClickListener(0));
        allMuseums.setOnClickListener(getOnClickListener(1));
        nearCafes.setOnClickListener(getOnClickListener(2));
        allCafes.setOnClickListener(getOnClickListener(3));

        return fragmentView;
    }
   private View.OnClickListener getOnClickListener(final int id) {
       View.OnClickListener onClickListener = new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Database.PLACES.clear();

               fgContent.setVisibility(View.GONE);
               openMapFragment(id);
           }
       };

       return onClickListener;
   }

   private void openMapFragment(int id) {
       Fragment fragment = new MapFragment(id);
       FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
       FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
       fragmentTransaction.replace(R.id.bg_content, fragment);
       fragmentTransaction.commit();
   }
}
