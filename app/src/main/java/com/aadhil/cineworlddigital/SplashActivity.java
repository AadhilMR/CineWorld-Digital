package com.aadhil.cineworlddigital;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;

import com.aadhil.cineworlddigital.model.User;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        View scroller = findViewById(R.id.view9);

        Animation loadingAnimation = AnimationUtils.loadAnimation(this, R.anim.splash_loader);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                scroller.setVisibility(View.VISIBLE);
                scroller.startAnimation(loadingAnimation);
            }
        }, 500);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i;
                // Check for signed in user
                SharedPreferences preferences = getSharedPreferences("user_preferences", Context.MODE_PRIVATE);

                long currentTimestamp = System.currentTimeMillis();
                long lastSignedIn = preferences.getLong("lastSigned", -1);

                if(lastSignedIn!=-1 && ((currentTimestamp - lastSignedIn) <= (30L*24L*60L*60L*1000L))) {
                    // User has currently signed in

                    // Get the current user details
                    MainActivity.currentUser = new User(
                            preferences.getString("first_name", null),
                            preferences.getString("last_name", null),
                            preferences.getString("mobile", null),
                            preferences.getString("email", null),
                            preferences.getString("password", null)
                    );

                    if(MainActivity.currentUser.getMobile() == null) {
                        i = new Intent(SplashActivity.this, MainActivity.class);
                    } else {
//                        SharedPreferences.Editor editor = preferences.edit();
//                        editor.putLong("lastSigned", currentTimestamp);
//                        editor.apply();
//                        i = new Intent(SplashActivity.this, HomeActivity.class);
                        i = new Intent(SplashActivity.this, MainActivity.class);
                    }
                } else {
                    i = new Intent(SplashActivity.this, MainActivity.class);
                }

                scroller.setVisibility(View.GONE);
                startActivity(i);
            }
        }, 4000);
    }
}