package com.aadhil.cineworlddigital;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigation.setNavigationBar(getSupportFragmentManager(), R.id.fragmentContainerView2);
    }
}