package com.aadhil.cineworlddigital;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class HomeActivity extends AppCompatActivity {
    private int currentPage = 0;
    private final Handler handler = new Handler();

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseStorage storage = FirebaseStorage.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Set App Bar
        AppBar.setAppBar(getSupportFragmentManager(), R.id.fragmentContainerView3,
                findViewById(R.id.parentLayoutHome));

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
        storage.getReference("/slider-image/")
            .listAll()
            .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                @Override
                public void onSuccess(ListResult listResult) {

                    // Image list
                    ArrayList<SliderImage> images = new ArrayList<>();

                    int totalImages = listResult.getItems().size();
                    AtomicInteger loadedImagesCount = new AtomicInteger(0);

                    for(StorageReference image : listResult.getItems()) {
                        image.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                try {
                                    images.add(new SliderImage(uri));

                                    if(loadedImagesCount.incrementAndGet() == totalImages) {
                                        showImageSlider(images);
                                    }
                                } catch (Exception e) {
                                    Log.e(HomeActivity.this.getClass().getSimpleName(), e.getMessage());
                                }
                            }
                        });
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(HomeActivity.this.getClass().getSimpleName(), e.getMessage());
                }
            });
    }

    private void showImageSlider(ArrayList<SliderImage> images) {
        ViewPager viewPager = findViewById(R.id.viewPager);
        SliderImageAdapter imageAdapter = new SliderImageAdapter(HomeActivity.this, images);
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