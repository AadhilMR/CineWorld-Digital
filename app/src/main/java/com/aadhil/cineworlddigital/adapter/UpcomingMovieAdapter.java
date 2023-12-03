package com.aadhil.cineworlddigital.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.aadhil.cineworlddigital.R;
import com.aadhil.cineworlddigital.model.UpcomingMovie;

import java.util.ArrayList;

public class UpcomingMovieAdapter {
    AppCompatActivity activity;
    ArrayList<UpcomingMovie> datalist;

    public UpcomingMovieAdapter(AppCompatActivity activity, ArrayList<UpcomingMovie> datalist) {
        this.activity = activity;
        this.datalist = datalist;
    }

    public RecyclerView.Adapter getAdapter() {
        return createAdapter();
    }

    private RecyclerView.Adapter createAdapter() {
        RecyclerView.Adapter adapter = new RecyclerView.Adapter<UpcomingMovieViewHolder>() {
            @NonNull
            @Override
            public UpcomingMovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater inflater = LayoutInflater.from(activity);
                View upcomingMovieView = inflater.inflate(R.layout.upcoming_movie_item, parent, false);
                return new UpcomingMovieViewHolder(upcomingMovieView);
            }

            @Override
            public void onBindViewHolder(@NonNull UpcomingMovieViewHolder holder, int position) {
                // holder.movieImage.setImageResource(R.drawable.something);
                holder.movieName.setText(datalist.get(position).getMovieName());
                holder.releaseDate.setText(datalist.get(position).getReleaseDate());
            }

            @Override
            public int getItemCount() {
                return datalist.size();
            }
        };

        return adapter;
    }
}
