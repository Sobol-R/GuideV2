package com.guideapp.guideapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;

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

    double longitude;
    double latitude;

    private FusedLocationProviderClient fusedLocationProviderClient;

    Location currentLocation;

    private GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;

    Bitmap bitmap;

    Map <String, Place> placesMap = new HashMap<>();

    boolean clicked = false;

    FrameLayout placeHintFrame;

    Fragment fragment;

    public static GoogleMap mgoogleMap;
    int placeType;
    boolean firstPlace = false;
    double startLatitude;
    double startLongitude;

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
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();

                            Database.getLatLng(latitude, longitude);
                        } else {
                            Toast toast = Toast.makeText(getContext(), "location == 0", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }
                });

        LatLngBounds SPB = new LatLngBounds(new LatLng(59.9343, 30.3351), new LatLng(59.9343,30.3351));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SPB.getCenter(), 12));

        startLocationUpdates();

       // Database.load(placeType);

        GoogleMap.OnMarkerClickListener onMarkerClickListener = new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                clicked = true;

                Place place = placesMap.get(marker.getTitle());

                fragment = new PlaceHintFragment(place);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.place_hint_frame, fragment);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.commit();
                return false;
            }
        };

        GoogleMap.OnMapClickListener onMapClickListener = new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (clicked) {
                    closePlaceHintFragment();
                }
                sendRouteRequest(latitude, longitude, latLng.latitude, latLng.longitude, mgoogleMap);
            }
        };

        googleMap.setOnMarkerClickListener(onMarkerClickListener);
        googleMap.setOnMapClickListener(onMapClickListener);
    }

    public static void sendRouteRequest(double latitude, double longitude,
                                        double destinationLat, double destinationLng, GoogleMap googleMap) {
        RoutesUtils routesUtils = new RoutesUtils(latitude, longitude, destinationLat, destinationLng);
        routesUtils.sendRouteRequest();

        googleMap.addMarker(new MarkerOptions()
                .zIndex(1)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .position(new LatLng(destinationLat, destinationLng)));

        LatLngBounds place = new LatLngBounds(new LatLng(destinationLat, destinationLng), new LatLng(destinationLat,destinationLng));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getCenter(), 15));
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

            Polyline polyline = mgoogleMap.addPolyline(
                    new PolylineOptions()
                            .addAll(poly)
                            .width(7)
                            .color(Color.BLUE)
                            .geodesic(true)
            );

        }
    }

    public void closePlaceHintFragment() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        fragmentTransaction.commit();
    }

    public void addPlacesToMap(GoogleMap googleMap) {
        for (int i = 0; i < Database.PLACES.size(); i++) {
            Place place = Database.PLACES.get(i);

            double latitude = place.latitude;
            double longitude = place.longitude;

            if (place.choosen) {
                place.choosen = false;

                googleMap.addMarker(new MarkerOptions()
                        .zIndex(2)
                        .title(place.name)
                        .position(new LatLng(latitude, longitude)));
            } else {
                googleMap.addMarker(new MarkerOptions()
                        .zIndex(1)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                        .title(place.name)
                        .position(new LatLng(latitude, longitude)));
            }

            placesMap.put(place.name, place);

        }
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
    public void onMessageEvent(Database.PlacesEvent event) {
        addPlacesToMap(event.googleMap);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPolylineEvent(RoutesUtils.PolylineEvent event) {
        addPoly(event.points);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAddressEvent(SearchPlaceFragment.AddressEvent event) {
        sendRouteRequest(latitude, longitude, event.lat, event.lng, mgoogleMap);
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        LocationRequest locationRequest = new LocationRequest();
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
                    Log.v("GUB", "? " + location);
                    currentLocation = location;
                }
            }
        };

        fusedLocationProviderClient.requestLocationUpdates(locationRequest,
                locationCallback, null /* Looper */);
    }
}