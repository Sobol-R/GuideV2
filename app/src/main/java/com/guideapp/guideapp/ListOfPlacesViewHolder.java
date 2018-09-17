package com.guideapp.guideapp;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ListOfPlacesViewHolder extends RecyclerView.ViewHolder {

    View itemView;
    ImageView imageOfPlace;
    TextView nameOfPlace;

    public ListOfPlacesViewHolder(View itemView) {
        super(itemView);

        this.itemView = itemView;
        imageOfPlace = itemView.findViewById(R.id.image_of_list_items);
        nameOfPlace = itemView.findViewById(R.id.text_of_list_items);
    }
}
