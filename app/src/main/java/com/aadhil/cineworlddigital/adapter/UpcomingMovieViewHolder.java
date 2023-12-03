package com.aadhil.cineworlddigital.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aadhil.cineworlddigital.R;

public class UpcomingMovieViewHolder extends RecyclerView.ViewHolder {
    ImageView movieImage;
    TextView movieName;
    TextView releaseDate;

    public UpcomingMovieViewHolder(@NonNull View itemView) {
        super(itemView);
        this.movieImage = itemView.findViewById(R.id.imageView3);
        this.movieName = itemView.findViewById(R.id.textView16);
        this.releaseDate = itemView.findViewById(R.id.textView17);
    }
}
