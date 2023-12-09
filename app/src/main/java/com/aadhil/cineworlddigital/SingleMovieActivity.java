package com.aadhil.cineworlddigital;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.aadhil.cineworlddigital.fragment.AppBar;
import com.aadhil.cineworlddigital.fragment.BottomNavigation;
import com.aadhil.cineworlddigital.service.ActivityNavigator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class SingleMovieActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getIntent() == null || !getIntent().hasExtra("movieId")) {
            // No movie data is received
            Intent i = new Intent(this, HomeActivity.class);
            startActivity(i);
        } else {
            setContentView(R.layout.activity_single_movie);

            // Set App Bar
            AppBar.setAppBar(getSupportFragmentManager(), R.id.fragmentContainerView6,
                    findViewById(R.id.parentLayoutSingleMovie));

            // Set Bottom Navigation
            BottomNavigation.setNavigationBar(getSupportFragmentManager(), R.id.fragmentContainerView7);

            getMovieDetails(getIntent().getStringExtra("movieId"));

            // Go to Checkout
            Button button = findViewById(R.id.button8);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityNavigator navigator = ActivityNavigator.getNavigator(SingleMovieActivity.this,
                            findViewById(R.id.parentLayoutSingleMovie));

                    navigator.setRedirection(new ActivityNavigator.NavigationManager() {
                        @Override
                        public void redirect() {
                            Intent i = new Intent(SingleMovieActivity.this, CheckoutActivity.class);
                            startActivity(i);
                        }
                    });
                }
            });
        }
    }

    private void loadTrailer(String stringUrl, String title) {
        WebView webview = findViewById(R.id.webView);
        String video = "<html style=\"padding: 0; margin: 0;\"><body style=\"padding: 0; margin: 0; top: 0; left: 0;\"><iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/"+ stringUrl +"\" title=\""+ title +"\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe></body></html>";
        webview.loadData(video, "text/html", "utf-8");
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebChromeClient(new WebChromeClient());

        // VideoView videoView = findViewById(R.id.videoView);
        // Uri uri = Uri.parse(stringUri);
        // videoView.setVideoURI(uri);
        // MediaController mediaController = new MediaController(this);
        // videoView.setMediaController(mediaController);
        // videoView.start();
    }

    private void loadShowTimes(String... showTimes) {
        ConstraintLayout layout = findViewById(R.id.constraintLayout);

        int previousViewId = 0;

        for (int i=0; i<showTimes.length; i++) {
            LayoutInflater inflater = LayoutInflater.from(this);
            ConstraintLayout showTimeConstraintLayout = (ConstraintLayout) inflater.inflate(R.layout.show_time, null);
            showTimeConstraintLayout.setId(View.generateViewId());
            TextView textView = showTimeConstraintLayout.findViewById(R.id.textView22);
            textView.setText(showTimes[i]);
            layout.addView(showTimeConstraintLayout);
            previousViewId = setConstraint(layout, showTimeConstraintLayout.getId(), previousViewId, i);
        }
    }

    private int setConstraint(ConstraintLayout layout, @IdRes int viewId, @IdRes int prevViweId, int position) {
        int startId, startSide, margin;

        if(position == 0) {
            startId = R.id.constraintLayout;
            startSide = ConstraintSet.START;
            margin = 0;
        } else {
            startId = prevViweId;
            startSide = ConstraintSet.END;
            margin = 10;
        }

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(layout);
        constraintSet.connect(viewId, ConstraintSet.START, startId, startSide, margin);
        constraintSet.connect(viewId, ConstraintSet.TOP, R.id.constraintLayout, ConstraintSet.TOP);
        constraintSet.applyTo(layout);
        return viewId;
    }

    private String getDurationHMFormat(int durationAsInt) {
        int hour = durationAsInt/60;
        int mins = durationAsInt%60;
        return  hour + "h " + mins + "m";
    }

    private void getMovieDetails(String docId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        HashMap<String, String> map = new HashMap<>();

        db.collection("movies").document(docId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot item = task.getResult();
                map.put("name", item.get("name").toString());
                map.put("duration", getDurationHMFormat(Integer.parseInt(item.get("duration").toString())));
                map.put("language", item.get("language").toString());
                map.put("releaseDate", item.get("releaseDate").toString());
                map.put("videoId", item.get("videoId").toString());
                map.put("desc", item.get("description").toString());

                db.collection("movieDates").whereEqualTo("movieId", item.getId())
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()) {
                                db.collection("/movieDates/"+ task.getResult().getDocuments().get(0).getId() +"/showTimes")
                                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if(task.isSuccessful()) {
                                                map.put("showTime", task.getResult().getDocuments().get(0).get("showtime").toString());

                                                setMovieToUi(map);
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e(SingleMovieActivity.this.getClass().getSimpleName(), e.getMessage());
                                        }
                                    });
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(SingleMovieActivity.this.getClass().getSimpleName(), e.getMessage());
                        }
                    });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(SingleMovieActivity.this.getClass().getSimpleName(), e.getMessage());
            }
        });
    }

    private void setMovieToUi(HashMap<String, String> map) {
        TextView movieName = findViewById(R.id.textView18);
        TextView duration = findViewById(R.id.textView23);
        TextView releaseDate = findViewById(R.id.textView26);
        TextView language = findViewById(R.id.textView24);
        TextView desc = findViewById(R.id.textView27);

        movieName.setText(map.get("name"));
        duration.setText(map.get("duration"));
        releaseDate.setText(map.get("releaseDate"));
        language.setText(map.get("language"));
        desc.setText(map.get("desc"));

        loadTrailer(map.get("videoId"), map.get("name"));
        loadShowTimes(map.get("showTime"));
    }
}