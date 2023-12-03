package com.aadhil.cineworlddigital;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aadhil.cineworlddigital.adapter.CurrentMovieAdapter;
import com.aadhil.cineworlddigital.adapter.UpcomingMovieAdapter;
import com.aadhil.cineworlddigital.model.CurrentMovie;
import com.aadhil.cineworlddigital.model.UpcomingMovie;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Set App Bar
        AppBar.setAppBar(getSupportFragmentManager(), R.id.fragmentContainerView3);

        // Set Bottom Navigation
        BottomNavigation.setNavigationBar(getSupportFragmentManager(), R.id.fragmentContainerView2);

        // TEMP: Fake ArrayList to pass to the adapters
        ArrayList<CurrentMovie> datalist1 = new ArrayList<>();
        ArrayList<UpcomingMovie> datalist2 = new ArrayList<>();

        // Set current movies to recycler view
        setMoviesToRecyclerView(R.id.recyclerView)
                .setAdapter(new CurrentMovieAdapter(HomeActivity.this, datalist1).getAdapter());

        // Set upcoming movies to recycler
        setMoviesToRecyclerView(R.id.recyclerView2)
                .setAdapter(new UpcomingMovieAdapter(HomeActivity.this, datalist2).getAdapter());
    }

    private RecyclerView setMoviesToRecyclerView(@IdRes int resId) {
        RecyclerView recyclerView = findViewById(resId);
        recyclerView.setLayoutManager(new LinearLayoutManager(HomeActivity.this,
                LinearLayoutManager.HORIZONTAL, false));

        return recyclerView;
    }
}