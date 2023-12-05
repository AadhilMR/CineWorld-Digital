package com.aadhil.cineworlddigital;

import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.aadhil.cineworlddigital.adapter.CurrentMovieAdapter;
import com.aadhil.cineworlddigital.adapter.SliderImageAdapter;
import com.aadhil.cineworlddigital.adapter.UpcomingMovieAdapter;
import com.aadhil.cineworlddigital.fragment.AppBar;
import com.aadhil.cineworlddigital.fragment.BottomNavigation;
import com.aadhil.cineworlddigital.model.CurrentMovie;
import com.aadhil.cineworlddigital.model.SliderImage;
import com.aadhil.cineworlddigital.model.UpcomingMovie;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    private int currentPage = 0;
    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Set App Bar
        AppBar.setAppBar(getSupportFragmentManager(), R.id.fragmentContainerView3);

        // Set Bottom Navigation
        BottomNavigation.setNavigationBar(getSupportFragmentManager(), R.id.fragmentContainerView2);

        // Set Image Slider
        setupImageSlider();

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

    private void setupImageSlider() {
        ArrayList<SliderImage> images = new ArrayList<>();
        images.add(new SliderImage(R.drawable.ic_home));
        images.add(new SliderImage(R.drawable.ic_menu));
        images.add(new SliderImage(R.drawable.ic_logout));

        ViewPager viewPager = findViewById(R.id.viewPager);
        SliderImageAdapter imageAdapter = new SliderImageAdapter(this, images);
        viewPager.setAdapter(imageAdapter);

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if(currentPage == images.size()) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
                handler.postDelayed(this, 8000);
            }
        };

        handler.postDelayed(runnable, 8000);
    }
}