package com.guideapp.guideapp;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PhotoUtils {

    String data;
    List <String> photoReferences = new ArrayList<>();

    public PhotoUtils(String data) {
        this.data = data;
    }

    public void parse() throws JSONException {
        JSONObject object = new JSONObject(data);
        JSONObject result = object.getJSONObject("result");
        JSONArray photos = result.getJSONArray("photos");
        for (int i = 0; i < photos.length(); i++) {
            JSONObject photo = photos.getJSONObject(i);
            String reference = photo.getString("photo_reference");
            photoReferences.add(reference);
        }
        EventBus.getDefault().post(new PhotoEvent(photoReferences));
    }

    public static class PhotoEvent {
        public PhotoEvent(List <String> references) {
            this.references = references;
        }
        List <String> references;
    }
}
