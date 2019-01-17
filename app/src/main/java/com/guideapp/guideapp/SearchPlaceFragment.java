package com.guideapp.guideapp;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SearchPlaceFragment extends Fragment {

    public SearchPlaceFragment() {
        // Required empty public constructor
    }

    EditText searchText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_search_place, container, false);
        searchText = fragmentView.findViewById(R.id.search_text);

        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_bottom);
        searchText.setAnimation(animation);
        animation.start();

        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String url = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=" +
                            parseString(searchText.getText().toString()) +
                            "&key=AIzaSyCZ2QPsrCzN8KrTE234GujTFlaRQQjQ5oI";
                    RequestUtils requestUtils = new RequestUtils(url, "address");
                    requestUtils.sendRequest();

                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.bg_content, new MapFragment(1));
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fragmentTransaction.commit();
                    return true;
                }
                return false;
            }
        });

        return fragmentView;
    }



    public static void parse(String data) throws JSONException {
        JSONObject object = new JSONObject(data);
        JSONArray results = object.getJSONArray("results");
        JSONObject result = results.getJSONObject(0);
        double latitude = result.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
        double longitude = result.getJSONObject("geometry").getJSONObject("location").getDouble("lng");
        EventBus.getDefault().post(new AddressEvent(latitude, longitude));
    }

    private String parseString(String request) {
        return request.replace(' ', '+');
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                AndroidUtils.showKeyboard(getContext(), searchText);
            }
        }, 50);
    }

    public static class AddressEvent {
        public AddressEvent(double lat, double lng) {
            this.lat = lat;
            this.lng = lng;
        }
        double lat;
        double lng;
    }
}
