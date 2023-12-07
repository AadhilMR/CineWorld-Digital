package com.aadhil.cineworlddigital;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.aadhil.cineworlddigital.fragment.AppBar;
import com.aadhil.cineworlddigital.fragment.BottomNavigation;
import com.aadhil.cineworlddigital.service.ActivityNavigator;

public class SingleMovieActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_movie);

        // Set App Bar
        AppBar.setAppBar(getSupportFragmentManager(), R.id.fragmentContainerView6,
                findViewById(R.id.parentLayoutSingleMovie));

        // Set Bottom Navigation
        BottomNavigation.setNavigationBar(getSupportFragmentManager(), R.id.fragmentContainerView7,
                findViewById(R.id.parentLayoutSingleMovie));

        // Load trailer
        String uri = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4";
        loadTrailer(uri);

        // Load Show Times
        loadShowTimes("10.30AM", "7.15PM");

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

    private void loadTrailer(String stringUri) {
        VideoView videoView = findViewById(R.id.videoView);
        Uri uri = Uri.parse(stringUri);
        videoView.setVideoURI(uri);
        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        videoView.start();
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
}