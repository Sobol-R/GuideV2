package com.guideapp.guideapp;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mancj.slideup.SlideUp;
import com.mancj.slideup.SlideUpBuilder;

import static com.bumptech.glide.request.RequestOptions.centerCropTransform;
import static com.bumptech.glide.request.RequestOptions.fitCenterTransform;

public class PlaceDescriptionFragment extends Fragment {

    Place thisPlace;

    public PlaceDescriptionFragment(Place place) {
        thisPlace = place;
    }

    ImageView placePhoto;
    TextView name;
    TextView rating;
    TextView open;
    TextView type;
    TextView vicinity;
    RelativeLayout descriptionContent;

    String photoRequestBegin;
    String photoRequestEnd;

    Boolean isSlided = false;

    float dX;
    float dY;

    SlideUp slideUp;

    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_place_description, container, false);

        placePhoto = fragmentView.findViewById(R.id.place_photo);
        name = fragmentView.findViewById(R.id.name);
        rating = fragmentView.findViewById(R.id.rating);
        type = fragmentView.findViewById(R.id.type);
        open = fragmentView.findViewById(R.id.open);
        vicinity = fragmentView.findViewById(R.id.vicinity);
        descriptionContent = fragmentView.findViewById(R.id.description_content);

        photoRequestBegin = "https://maps.googleapis.com/maps/api/place/photo?maxheight=1000&photoreference=";
        photoRequestEnd = "&key=AIzaSyCZ2QPsrCzN8KrTE234GujTFlaRQQjQ5oI";

        Glide
                .with(getActivity())
                .load(photoRequestBegin + thisPlace.photoLink + photoRequestEnd)
                .apply(fitCenterTransform())
                .apply(centerCropTransform())
                .into(placePhoto);

        name.setText(thisPlace.name);
        rating.setText(String.format("Рейтинг: %s", thisPlace.rating));
        type.setText(String.format("Тип: %s","Музей"));
        open.setText(String.format("Открыто: %s", "null"));
        vicinity.setText(String.format("Адрес: %s", thisPlace.vicinity));

        slideUp = new SlideUpBuilder(placePhoto)
                .withStartState(SlideUp.State.SHOWED)
                .withStartGravity(Gravity.TOP)
                .withStartGravity(500)
                .build();

        descriptionContent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        break;

                    case MotionEvent.ACTION_MOVE:
                        slideUp.hide();

                        break;


                    default:
                        return false;
                }
                return true;
            }
        });

        return fragmentView;
    }
}
