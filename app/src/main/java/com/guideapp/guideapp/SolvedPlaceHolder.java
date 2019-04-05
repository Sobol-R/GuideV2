package com.guideapp.guideapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class SolvedPlaceHolder extends RecyclerView.ViewHolder {

    View itemView;
    ImageView imageView;
    TextView textView;

    public SolvedPlaceHolder(@NonNull View itemView) {
        super(itemView);
        this.itemView = itemView;
        imageView = itemView.findViewById(R.id.solved_img);
        textView = itemView.findViewById(R.id.solved_name);
    }
}
