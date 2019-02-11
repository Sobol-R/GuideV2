package com.guideapp.guideapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

public class ImageViewPagerAdapter extends FragmentStatePagerAdapter {

    List<String> references;

    public ImageViewPagerAdapter(FragmentManager fm, List<String> references) {
        super(fm);
        this.references = references;
    }

    @Override
    public Fragment getItem(int position) {
        return new PlaceImageFragment(references.get(position));
    }

    @Override
    public int getCount() {
        return references.size();
    }
}
