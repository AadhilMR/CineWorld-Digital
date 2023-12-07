package com.aadhil.cineworlddigital;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.aadhil.cineworlddigital.fragment.AppBar;
import com.aadhil.cineworlddigital.fragment.BottomNavigation;
import com.aadhil.cineworlddigital.fragment.PayNow;

public class PaymentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // Set App Bar
        AppBar.setAppBar(getSupportFragmentManager(), R.id.fragmentContainerView16,
                findViewById(R.id.parentLayoutPayment));

        // Set Bottom Navigation
        BottomNavigation.setNavigationBar(getSupportFragmentManager(), R.id.fragmentContainerView17,
                findViewById(R.id.parentLayoutPayment));

        // By Default, set the PayNow fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainerView18, PayNow.class, null)
                .commit();
    }
}