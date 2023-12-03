package com.aadhil.cineworlddigital.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aadhil.cineworlddigital.R;

public class CurrentMovieViewHolder extends RecyclerView.ViewHolder {
    ImageView movieImage;
    TextView movieName;
    TextView duration;
    TextView language;

    public CurrentMovieViewHolder(@NonNull View itemView) {
        super(itemView);
        this.movieImage = itemView.findViewById(R.id.imageView2);
        this.movieName = itemView.findViewById(R.id.textView13);
        this.duration = itemView.findViewById(R.id.textView14);
        this.language = itemView.findViewById(R.id.textView15);
    }
}
