package com.aadhil.cineworlddigital;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.aadhil.cineworlddigital.fragment.AppBar;
import com.aadhil.cineworlddigital.fragment.BottomNavigation;
import com.aadhil.cineworlddigital.fragment.ConfirmCheckout;
import com.aadhil.cineworlddigital.fragment.SelectMovie;
import com.aadhil.cineworlddigital.fragment.SelectSeat;

import java.util.HashMap;

public class CheckoutActivity extends AppCompatActivity {
    private CheckoutFragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        // Set App Bar
        AppBar.setAppBar(getSupportFragmentManager(), R.id.fragmentContainerView8);

        // Set Bottom Navigation
        BottomNavigation.setNavigationBar(getSupportFragmentManager(), R.id.fragmentContainerView9);

        // By default, set the 'SelectMovie' fragment
        getCheckoutFragmentManager().setFragment(CheckoutFragmentManager.FRAGMENT_SELECT_MOVIE);
    }

    public CheckoutFragmentManager getCheckoutFragmentManager() {
        if(fragmentManager == null) {
            fragmentManager = new CheckoutFragmentManager(getSupportFragmentManager());
        }
        return fragmentManager;
    }

    public static class CheckoutFragmentManager {
        public static final int FRAGMENT_SELECT_MOVIE = 1;
        public static final int FRAGMENT_SELECT_SEAT = 2;
        public static final int FRAGMENT_CONFIRM_CHECKOUT = 3;

        private final HashMap<Integer, Class> fragmentIdentifier = new HashMap<>();

        private final FragmentManager fragmentManager;

        CheckoutFragmentManager(FragmentManager fragmentManager) {
            this.fragmentManager = fragmentManager;
            setFragmentIdentifier();
        }

        private void setFragmentIdentifier() {
            fragmentIdentifier.put(CheckoutFragmentManager.FRAGMENT_SELECT_MOVIE, SelectMovie.class);
            fragmentIdentifier.put(CheckoutFragmentManager.FRAGMENT_SELECT_SEAT, SelectSeat.class);
            fragmentIdentifier.put(CheckoutFragmentManager.FRAGMENT_CONFIRM_CHECKOUT, ConfirmCheckout.class);
        }

        private HashMap<Integer, Class> getFragmentIdentifier() {
            return fragmentIdentifier;
        }

        private void replaceFragment(int fragmentId) {
            Class fragmentClass = getFragmentIdentifier().get(fragmentId);
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView10, fragmentClass, null)
                    .commit();
        }

        public void setFragment(int fragmentId) {
            replaceFragment(fragmentId);
        }
    }
}

