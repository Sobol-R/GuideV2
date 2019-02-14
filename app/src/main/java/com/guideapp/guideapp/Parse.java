package com.guideapp.guideapp;

import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Parse {

    public static void parseRouteRequest(String data) throws JSONException {
        JSONObject object = new JSONObject(data);
        JSONArray routes = object.getJSONArray("routes");
        JSONObject route = routes.getJSONObject(0);
        JSONObject overViewPolyline = route.getJSONObject("overview_polyline");
        String points = overViewPolyline.getString("points");
        EventBus.getDefault().post(new RoutesUtils.PolylineEvent(points));
    }

    public static void parsePhotoRequest(String data) throws JSONException {
        List<String> photoReferences = new ArrayList<>();
        JSONObject object = new JSONObject(data);
        JSONObject result = object.getJSONObject("result");
        JSONArray photos = result.getJSONArray("photos");
        for (int i = 0; i < photos.length(); i++) {
            JSONObject photo = photos.getJSONObject(i);
            String reference = photo.getString("photo_reference");
            photoReferences.add(reference);
        }
        EventBus.getDefault().post(new PhotoUtils.PhotoEvent(photoReferences));
    }

    public static void parseWikipedia(String data) throws JSONException {
        JSONObject object = new JSONObject(data);
        String text = object.getString("extract");
        EventBus.getDefault().post(new WikipediaEvent(text));
    }

    public static class WikipediaEvent {
        public WikipediaEvent(String text) {
            this.text = text;
        }
        String text;
    }

}
