package com.guideapp.guideapp;

import com.google.android.gms.maps.GoogleMap;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Database {

    public static final String MUSEUMS_IN_CENTER_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=59.9386300,30.3141300&radius=1500&type=museum&keyword=cruise&key=AIzaSyCZ2QPsrCzN8KrTE234GujTFlaRQQjQ5oI";
    public static final String CAFES_IN_CENTER = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=59.9386300,30.3141300&radius=1500&type=cafe&keyword=cruise&key=AIzaSyCZ2QPsrCzN8KrTE234GujTFlaRQQjQ5oI";
    public static final String NEAR_BY_SEARCH_ = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=";
    private static final String API_KEY = "AIzaSyCZ2QPsrCzN8KrTE234GujTFlaRQQjQ5oI";
    public static final String MUSEUM_TYPE = "&radius=1500&type=museum&keyword=cruise";
    public static final String CAFE_TYPE = "&radius=1500&type=cafe&keyword=cruise";
    public static final String ALL_MUSEUMS_URL = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=museums+in+Sankt-Peterburg&key=AIzaSyCZ2QPsrCzN8KrTE234GujTFlaRQQjQ5oI";
    public static final String ALL_CAFES_URL = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=cafes+in+Sankt-Peterburg&key=AIzaSyCZ2QPsrCzN8KrTE234GujTFlaRQQjQ5oI";

   static double latitude;
   static double longitude;

    public static   ArrayList <Place> VISITED_PLACES = new ArrayList<>();
    public static ArrayList <Place> PLACES = new ArrayList<>();

    public static void getLatLng(double lat, double lng) {
        latitude = lat;
        longitude = lng;
    }

    public static void load(int id) {
        String url = null;

        switch (id) {
            case 0:
                //url = NEAR_BY_SEARCH_ + latitude + ',' + longitude + MUSEUM_TYPE + API_KEY;
                url = MUSEUMS_IN_CENTER_URL;
                break;
            case 1:
                url = MUSEUMS_IN_CENTER_URL;
                break;
            case 2:
                url = NEAR_BY_SEARCH_ + latitude + ',' + longitude + CAFE_TYPE + API_KEY;
                break;
            case 3:
                url = CAFES_IN_CENTER;
        }

        Request request = new Request.Builder()
                .url(url)
                .build();
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String data = response.body().string();
                try {
                    parse(data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public static void parse(String data) throws JSONException {
        JSONObject object = new JSONObject(data);
        JSONArray results = object.getJSONArray("results");

        for (int i = 0; i < results.length(); ++i) {
            JSONObject place = results.getJSONObject(i);
            JSONObject location = place.getJSONObject("geometry").getJSONObject("location");
            double latitude = location.getDouble("lat");
            double longitude = location.getDouble("lng");
            String iconPath = place.getString("icon");
            String id = place.getString("id");
            String name = place.getString("name");
            //boolean open = place.getJSONObject("opening_hours").getBoolean("open_now");
            String placeId = place.getString("place_id");
            double rating = place.getDouble("rating");
            String vicinity = place.getString("vicinity");
            String photoLink = place.getJSONArray("photos").getJSONObject(0).getString("photo_reference");
            //String type = place.getJSONArray("types").getJSONObject(0).toString();

            PLACES.add(new Place(latitude, longitude, iconPath, id, name /*open*/, placeId,
                    rating, vicinity, photoLink /*type*/));
        }
        EventBus.getDefault().post(new PlacesEvent(MapFragment.mgoogleMap));
    }

    public static class PlacesEvent {
        public PlacesEvent(GoogleMap googleMap) {
            this.googleMap = googleMap;
        }
        GoogleMap googleMap;
    }
}
