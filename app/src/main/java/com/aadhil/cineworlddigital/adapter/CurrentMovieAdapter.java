package com.aadhil.cineworlddigital.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.aadhil.cineworlddigital.R;
import com.aadhil.cineworlddigital.SingleMovieActivity;
import com.aadhil.cineworlddigital.model.CurrentMovie;
import com.aadhil.cineworlddigital.service.ActivityNavigator;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CurrentMovieAdapter {
    final private AppCompatActivity activity;
    final private ArrayList<CurrentMovie> datalist;

    final private ActivityNavigator navigator;

    public CurrentMovieAdapter(AppCompatActivity activity, ArrayList<CurrentMovie> datalist, ActivityNavigator navigator) {
        this.activity = activity;
        this.datalist = datalist;
        this.navigator = navigator;
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
                String id = datalist.get(position).getDocumentId();

                Picasso.get().load(datalist.get(position).getImageId()).into(holder.movieImage);
                holder.movieName.setText(datalist.get(position).getMovieName());
                holder.duration.setText(datalist.get(position).getDuration());
                holder.language.setText(datalist.get(position).getLanguage());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        navigator.setRedirection(new ActivityNavigator.NavigationManager() {
                            @Override
                            public void redirect() {
                                Intent i = new Intent(activity, SingleMovieActivity.class);
                                i.putExtra("movieId", id);
                                activity.startActivity(i);
                            }
                        });
                    }
                });
            }

            @Override
            public int getItemCount() {
                return datalist.size();
            }
        };
    }
}
