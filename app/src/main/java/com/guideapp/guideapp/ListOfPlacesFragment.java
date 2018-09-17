package com.guideapp.guideapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ListOfPlacesFragment extends Fragment {

    public ListOfPlacesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_list_of_places, container, false);

        ListOfPlacesAdapter listOfPlacesAdapter = new ListOfPlacesAdapter((MainActivity) getActivity());
        RecyclerView recyclerView = fragmentView.findViewById(R.id.list_of_places_recycler_view);

        recyclerView.setAdapter(listOfPlacesAdapter);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));

        return fragmentView;
    }
}
