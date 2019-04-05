package com.guideapp.guideapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.protobuf.DescriptorProtos;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bumptech.glide.Glide.with;

public class MapFragment extends SupportMapFragment implements OnMapReadyCallback {

    LocationManager locationManager;

    public static double longitude;
    public static double latitude;

    private FusedLocationProviderClient fusedLocationProviderClient;

    Location currentLocation;

    private GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;

    Bitmap bitmap;

    Map <String, Place> placesMap = new HashMap<>();

    boolean clicked = false;

    FrameLayout placeHintFrame;

    public static GoogleMap mgoogleMap;
    int placeType;
    boolean firstPlace = false;
    double startLatitude;
    double startLongitude;

    boolean cameraMoved = false;
    boolean onMapClicked = false;

    public static int cnt = 0;

    com.google.android.gms.location.places.Place place;

    Polyline currentPolyline;

    public static List<LatLng> tests = new ArrayList<>();

    public MapFragment(int placeType) {
        this.placeType = placeType;
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        getMapAsync(this);

        View fragmentView = super.onCreateView(layoutInflater, viewGroup, bundle);

        mGeoDataClient = Places.getGeoDataClient(getContext(), null);
        mPlaceDetectionClient = Places.getPlaceDetectionClient(getContext(), null);

        placeHintFrame = fragmentView.findViewById(R.id.place_hint_frame);

        tests.add(new LatLng(59.941208, 30.315487));
        tests.add(new LatLng(59.942660, 30.316990));
        tests.add(new LatLng(59.938268, 30.320013));
        tests.add(new LatLng(59.938310, 30.315000));
        tests.add(new LatLng(59.93858475, 30.32888895));

        return fragmentView;
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        mgoogleMap = googleMap;

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
        } else {
            Toast toast = Toast.makeText(getContext(),
                    "Разрешите приложению использовать вашу геопозицию", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }

        locationManager = (LocationManager) getActivity().getSystemService(getContext().LOCATION_SERVICE);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(getActivity(), location -> {
                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        moveCamera(latitude, longitude);
                        Database.getLatLng(latitude, longitude);
                    } else {
                        Toast toast = Toast.makeText(getContext(), "location == 0", Toast.LENGTH_LONG);
                        toast.show();
                    }
                });

        startLocationUpdates();

        GoogleMap.OnMarkerClickListener onMarkerClickListener = marker -> {
            clicked = true;

            if (Database.PLACES.size() != 0) {
                for (Place p : Database.PLACES) {
                    if (marker.getPosition().latitude == p.latitude &&
                            marker.getPosition().longitude == p.longitude) {
                        place = p;
                    }
                }
            }

            openFragment(new PlaceHintFragment(place, latitude, longitude));
            return false;
        };

        googleMap.setOnMarkerClickListener(onMarkerClickListener);
    }

    public static void test(ArrayList<LatLng> tests) {
        List <LatLng> coors = new ArrayList<>();
        coors.add(new LatLng(latitude, longitude));
        for (int i = 0; i <= tests.size(); i++) {
            Test res = new Test(null, 999999999);
            for (int j = 0; j < tests.size(); j++) {
                if (i == 0) {
                    double cur = getDistance(latitude, longitude, tests.get(j).latitude, tests.get(j).longitude);
                    if (cur < res.min) res = new Test(new LatLng(tests.get(j).latitude, tests.get(j).longitude), cur);
                } else {
                    if (i != j) {
                        double cur = getDistance(tests.get(i-1).latitude, tests.get(i-1).longitude, tests.get(j).latitude, tests.get(j).longitude);
                        if (cur < res.min) res = new Test(new LatLng(tests.get(j).latitude, tests.get(j).longitude), cur);
                    }
                }
            }
            coors.add(res.latLng);
        }
        for (int i = 1; i < coors.size(); i++) {
            sendRouteRequest(coors.get(i-1).latitude, coors.get(i-1).longitude,
                    coors.get(i).latitude, coors.get(i).longitude);
        }
    }

    public static double getDistance(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371; // Radius of the earth in km
        double dLat = deg2rad(lat2-lat1);  // deg2rad below
        double dLon = deg2rad(lon2-lon1);
        double a =
                Math.sin(dLat/2) * Math.sin(dLat/2) +
                        Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) *
                                Math.sin(dLon/2) * Math.sin(dLon/2)
                ;
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = R * c * 1000; // Distance in km
        return d;
    }

    public static double deg2rad(double deg) {
        return deg * (Math.PI/180);
    }

    private void openFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.place_hint_frame, fragment, "place_hint");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
    }

    public static void sendRouteRequest(double latitude, double longitude,
                                        double destinationLat, double destinationLng) {
        RoutesUtils routesUtils = new RoutesUtils(latitude, longitude,
                destinationLat, destinationLng);
        routesUtils.sendRouteRequest();
        setMarker(latitude, longitude);
    }

    private void makeExcursionRoute() {
        for (int i = 1; i < Database.PLACES.size(); i++) {
            Place place1 = Database.PLACES.get(i - 1);
            Place place2 = Database.PLACES.get(i);
            RoutesUtils routesUtils = new RoutesUtils(place1.latitude, place1.longitude,
                    place2.latitude, place2.longitude);
            routesUtils.sendRouteRequest();
        }
    }

    public static void setMarker(double latitude, double longitude) {
        mgoogleMap.addMarker(new MarkerOptions()
                .zIndex(1)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
                .position(new LatLng(latitude ,longitude)));
        moveCamera(latitude, longitude);
    }

    public static void moveCamera(double latitude, double longitude) {
        LatLng latLng = new LatLng(latitude, longitude);
        mgoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                new CameraPosition.Builder()
                        .target(latLng)
                        .tilt(90)
                        .zoom(17)
                        .build()), 2000, null);
    }

    private void addPoly(String encoded) {
        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);

            currentPolyline = mgoogleMap.addPolyline(
                    new PolylineOptions()
                            .addAll(poly)
                            .width(15)
                            .color(Color.parseColor("#4B02F7"))
                            .geodesic(true)
            );

        }
        moveCamera(latitude, longitude);
    }

    public void addPlacesToMap() {
        for (int i = 0; i < Database.PLACES.size(); i++) {
            Place place = Database.PLACES.get(i);

            double latitude = place.latitude;
            double longitude = place.longitude;

            setMarker(latitude, longitude);

            placesMap.put(place.name, place);

        }
    }

    public void setPlacesLatLng() {
        ArrayList <LatLng> placesLatLng = new ArrayList<>();
        for (Place place : Database.PLACES) {
            placesLatLng.add(place.getLatLng());
        }
        test(placesLatLng);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(PlacesHintsFragment.PlacesEvent event) {
        Log.d("ok", "onMessMap: ");
        setPlacesLatLng();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPolylineEvent(RoutesUtils.PolylineEvent event) {
        addPoly(event.points);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAddressEvent(MainActivity.AddressEvent event) {
        this.place = event.place;
        setMarker(place.getLatLng().latitude, place.getLatLng().longitude);
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        final LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(2000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationCallback locationCallback = new LocationCallback() {

            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                }
                if (!cameraMoved) {
                    moveCamera(latitude, longitude);
                    cameraMoved = true;
                }
            }
        };

        fusedLocationProviderClient.requestLocationUpdates(locationRequest,
                locationCallback, null /* Looper */);
    }

    public static class Test {
        LatLng latLng;
        double min;
        public Test(LatLng latLng, double min) {
            this.latLng = latLng;
            this.min = min;
        }
    }
}