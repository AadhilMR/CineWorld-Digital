package com.aadhil.cineworlddigital.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.aadhil.cineworlddigital.R;
import com.aadhil.cineworlddigital.model.CurrentMovie;

import java.util.ArrayList;

public class CurrentMovieAdapter {
    final private AppCompatActivity activity;
    final private ArrayList<CurrentMovie> datalist;

    public CurrentMovieAdapter(AppCompatActivity activity, ArrayList<CurrentMovie> datalist) {
        this.activity = activity;
        this.datalist = datalist;
    }

    public RecyclerView.Adapter getAdapter() {
        return createAdapter();
    }

    private RecyclerView.Adapter createAdapter() {
        return new RecyclerView.Adapter<CurrentMovieViewHolder>() {
            @NonNull
            @Override
            public CurrentMovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater inflater = LayoutInflater.from(activity);
                View currentMovieView = inflater.inflate(R.layout.current_movie_item, parent, false);
                return new CurrentMovieViewHolder(currentMovieView);
            }

            @Override
            public void onBindViewHolder(@NonNull CurrentMovieViewHolder holder, int position) {
                // holder.movieImage.setImageResource(R.drawable.something);
                holder.movieName.setText(datalist.get(position).getMovieName());
                holder.duration.setText(datalist.get(position).getDuration());
                holder.language.setText(datalist.get(position).getLanguage());
            }

            @Override
            public int getItemCount() {
                return datalist.size();
            }
        };
    }
}
