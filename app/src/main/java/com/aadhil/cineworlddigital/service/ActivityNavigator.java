package com.aadhil.cineworlddigital.service;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.aadhil.cineworlddigital.R;

public class ActivityNavigator {

    private static ActivityNavigator navigator;
    private Context context;
    private ViewGroup layout;

    public ActivityNavigator(Context context, ViewGroup layout) {
        this.context = context;
        this.layout = layout;
    }

    private View setupProgress() {
        LayoutInflater inflater = LayoutInflater.from(this.context);
        View view = inflater.inflate(R.layout.layout_progress, null);

        ProgressBar progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        ViewGroup layout = this.layout;
        layout.addView(view);
        return view;
    }

    private void removeProgress(View view) {
        try {
            this.layout.removeView(view);
        } catch (Exception e) {
            // Do Nothing
        }
    }

    public void setRedirection(NavigationManager manager) {
        // Show Progress
        View view = setupProgress();

        new Thread(new Runnable() {
            @Override
            public void run() {
                manager.redirect();

                if(context instanceof AppCompatActivity) {
                    AppCompatActivity activity = (AppCompatActivity) context;
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            removeProgress(view);
                        }
                    });
                } else {
                    removeProgress(view);
                }

            }
        }).start();
    }

    public static ActivityNavigator getNavigator(Context context, ViewGroup layout) {
        if(navigator == null) {
            navigator = new ActivityNavigator(context, layout);
        } else if(layout != null) {
            navigator.context = context;
            navigator.layout = layout;
        }
        return navigator;
    }

    public interface NavigationManager {
        void redirect();
    }
}