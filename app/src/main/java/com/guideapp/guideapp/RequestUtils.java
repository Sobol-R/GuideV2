package com.guideapp.guideapp;

import android.util.Log;

import org.json.JSONException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RequestUtils {

    String url;
    RequestType type;

    public RequestUtils(String url, RequestType type) {
        this.url = url;
        this.type = type;
    }

    public void sendRequest() {
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
                Log.d("pr", data);
                try {
                    switch (type) {
                        case ROUTES : RoutesUtils.parse(data);
                        case PHOTOS : new PhotoUtils(data).parse();
                        case WIKIPEDIA : Parse.parseWikipedia(data);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
