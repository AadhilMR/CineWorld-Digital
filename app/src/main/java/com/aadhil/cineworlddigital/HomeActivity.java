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
import com.aadhil.cineworlddigital.service.ActivityNavigator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
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

        // Set Image Slider, Current Movies & Upcoming Movies
        setupImageSlider();
        setCurrentMovies();
        setUpcomingMovies();

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

    private String getDurationHMFormat(int durationAsInt) {
        int hour = durationAsInt/60;
        int mins = durationAsInt%60;
        String durationInHMFormat = hour + "h " + mins + "m";
        return durationInHMFormat;
    }

    private void setCurrentMovies() {
        ActivityNavigator navigator = ActivityNavigator.getNavigator(this, findViewById(R.id.parentLayoutHome));
        ArrayList<CurrentMovie> datalist = new ArrayList<>();

        RecyclerView.Adapter adapter = new CurrentMovieAdapter(HomeActivity.this, datalist, navigator).getAdapter();

        // Set current movies to recycler view
        setMoviesToRecyclerView(R.id.recyclerView)
                .setAdapter(adapter);

        // Get movies from Firestore
        db.collection("movies").whereEqualTo("current", true).get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()) {
                        for(DocumentSnapshot item : task.getResult().getDocuments()) {
                            // Create new Current Movie
                            CurrentMovie movie = new CurrentMovie();
                            movie.setMovieName(item.get("name").toString())
                                .setDuration(getDurationHMFormat(Integer.parseInt(item.get("duration").toString())))
                                .setLanguage(item.get("language").toString());

                            // Get Poster
                            storage.getReference("movie-image/" + item.get("imageId"))
                                .getDownloadUrl()
                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        movie.setImageId(uri);
                                        datalist.add(movie);
                                        // Update adapter
                                        adapter.notifyDataSetChanged();
                                    }
                                });
                        }
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(HomeActivity.this.getClass().getSimpleName(), e.getMessage());
                }
            });
    }

    private void setUpcomingMovies() {
        ArrayList<UpcomingMovie> datalist = new ArrayList<>();

        RecyclerView.Adapter adapter = new UpcomingMovieAdapter(HomeActivity.this, datalist).getAdapter();

        // Set current movies to recycler view
        setMoviesToRecyclerView(R.id.recyclerView2)
                .setAdapter(adapter);

        // Get movies from Firestore
        db.collection("movies").whereEqualTo("current", false).get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()) {
                        for(DocumentSnapshot item : task.getResult().getDocuments()) {
                            // Create new Current Movie
                            UpcomingMovie movie = new UpcomingMovie();
                            movie.setMovieName(item.get("name").toString())
                                            .setReleaseDate("Release on " + item.get("releaseDate"));

                            // Get Poster
                            storage.getReference("movie-image/" + item.get("imageId"))
                                .getDownloadUrl()
                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        movie.setImageId(uri);
                                        datalist.add(movie);
                                        // Update adapter
                                        adapter.notifyDataSetChanged();
                                    }
                                });
                        }
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(HomeActivity.this.getClass().getSimpleName(), e.getMessage());
                }
            });
    }
}