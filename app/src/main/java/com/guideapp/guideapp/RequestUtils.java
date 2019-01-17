package com.guideapp.guideapp;

import org.json.JSONException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RequestUtils {

    String url;
    String type;

    public RequestUtils(String url, String type) {
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
                try {
                    switch (type) {
                        case "route" : RoutesUtils.parse(data);
                        case "address" : SearchPlaceFragment.parse(data);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
