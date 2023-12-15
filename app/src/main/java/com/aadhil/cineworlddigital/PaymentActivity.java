package com.aadhil.cineworlddigital;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.aadhil.cineworlddigital.fragment.AppBar;
import com.aadhil.cineworlddigital.fragment.BottomNavigation;
import com.aadhil.cineworlddigital.fragment.PayNow;
import com.aadhil.cineworlddigital.model.CheckoutInfo;
import com.aadhil.cineworlddigital.model.Invoice;

public class PaymentActivity extends AppCompatActivity {
    public static CheckoutInfo checkoutInfo = null;
    public static Invoice invoice = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        checkoutInfo = (CheckoutInfo) getIntent().getSerializableExtra("checkoutInfo");

        // Set App Bar
        AppBar.setAppBar(getSupportFragmentManager(), R.id.fragmentContainerView16,
                findViewById(R.id.parentLayoutPayment));

        // Set Bottom Navigation
        BottomNavigation.setNavigationBar(getSupportFragmentManager(), R.id.fragmentContainerView17);

        // By Default, set the PayNow fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainerView18, PayNow.class, null)
                .commit();
    }
}