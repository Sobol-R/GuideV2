package com.guideapp.guideapp;

import android.util.Log;

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

    public static final String MUSEUMS_IN_CENTER_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=59.9386300,30.3141300&radius=1500&type=museum&language=ru&key=AIzaSyCZ2QPsrCzN8KrTE234GujTFlaRQQjQ5oI";
    public static final String CAFES_IN_CENTER = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=59.9386300,30.3141300&radius=1500&type=cafe&keyword=cruise&key=AIzaSyCZ2QPsrCzN8KrTE234GujTFlaRQQjQ5oI";
    public static final String NEAR_BY_SEARCH_ = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=";
    private static final String API_KEY = "AIzaSyCZ2QPsrCzN8KrTE234GujTFlaRQQjQ5oI";
    public static final String MUSEUM_TYPE = "&radius=1500&type=museum&keyword=cruise";
    public static final String CAFE_TYPE = "&radius=1500&type=cafe&keyword=cruise";
    public static final String ALL_MUSEUMS_URL = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=%D0%BC%D1%83%D0%B7%D0%B5%D0%B8+%D0%B2+%D0%A1%D0%B0%D0%BD%D0%BA%D1%82+%D0%9F%D0%B5%D1%82%D0%B5%D1%80%D0%B1%D1%83%D1%80%D0%B3%D0%B5&language=ru&key=AIzaSyCZ2QPsrCzN8KrTE234GujTFlaRQQjQ5oI";
    public static final String ALL_CAFES_URL = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=cafes+in+Sankt-Peterburg&key=AIzaSyCZ2QPsrCzN8KrTE234GujTFlaRQQjQ5oI";

   static double latitude;
   static double longitude;

   public static int i = 0;

    public static   ArrayList <Place> VISITED_PLACES = new ArrayList<>();
    public static ArrayList <Place> PLACES = new ArrayList<>();
    public static ArrayList <String> PHOTOS = new ArrayList<>();
    public static ArrayList <String> PLACES_NAMES = new ArrayList<>();

    public static void getLatLng(double lat, double lng) {
        latitude = lat;
        longitude = lng;
    }

    public static void load(String url) {
//        String url = null;
//
//        switch (id) {
//            case 0:
//                //url = NEAR_BY_SEARCH_ + latitude + ',' + longitude + MUSEUM_TYPE + API_KEY;
//                url = MUSEUMS_IN_CENTER_URL;
//                break;
//            case 1:
//                url = ALL_MUSEUMS_URL;
//                break;
//            case 2:
//                url = NEAR_BY_SEARCH_ + latitude + ',' + longitude + CAFE_TYPE + API_KEY;
//                break;
//            case 3:
//                url = CAFES_IN_CENTER;
//        }

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
        JSONObject place = results.getJSONObject(0);
        String address = place.getString("formatted_address");
        double lat = place.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
        double lng = place.getJSONObject("geometry").getJSONObject("location").getDouble("lng");
        String id = place.getString("place_id");
        String name = place.getString("name");
        PLACES.add(new Place(address, lat, lng, name, id));
        Log.d("ok", "ok" + PLACES.get(i).getName());
        i++;
        if (PLACES.size() == 5) {
            EventBus.getDefault().post(new PlacesEvent1());
        }
    }

    public static class PlacesEvent1 {
        public PlacesEvent1() {
        }
    }
}
