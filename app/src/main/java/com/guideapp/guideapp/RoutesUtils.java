package com.guideapp.guideapp;


import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RoutesUtils {

    double lat1;
    double lng1;
    double lat2;
    double lng2;

    public RoutesUtils(double lat1, double lng1, double lat2, double lng2) {
        this.lat1 = lat1;
        this.lng1 = lng1;
        this.lat2 = lat2;
        this.lng2 = lng2;
    }

    public void sendRouteRequest() {
        String url = "https://maps.googleapis.com/maps/api/directions/json?origin="
                + String.valueOf(lat1) + "," + String.valueOf(lng1) + "&destination=" + String.valueOf(lat2) + ","
                + String.valueOf(lng2) + "&key=AIzaSyCZ2QPsrCzN8KrTE234GujTFlaRQQjQ5oI";
        RequestUtils requestUtils = new RequestUtils(url, RequestType.ROUTES);
        requestUtils.sendRequest();
    }

    public static void parse(String data) throws JSONException {
        JSONObject object = new JSONObject(data);
        JSONArray routes = object.getJSONArray("routes");
        JSONObject route = routes.getJSONObject(0);
        JSONObject overViewPolyline = route.getJSONObject("overview_polyline");
        String points = overViewPolyline.getString("points");
        EventBus.getDefault().post(new RoutesUtils.PolylineEvent(points));
    }

    public static class PolylineEvent {
        public PolylineEvent(String points) {
            this.points = points;
        }
        String points;
    }

}
