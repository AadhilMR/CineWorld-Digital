package com.aadhil.cineworlddigital.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.aadhil.cineworlddigital.CheckoutActivity;
import com.aadhil.cineworlddigital.PaymentActivity;
import com.aadhil.cineworlddigital.R;
import com.aadhil.cineworlddigital.service.ActivityNavigator;

public class ConfirmCheckout extends Fragment {
    public ConfirmCheckout() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_confirm_checkout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View fragment, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(fragment, savedInstanceState);

        // Get activity navigator
        ActivityNavigator navigator = ActivityNavigator.getNavigator(getContext(),
                getActivity().findViewById(R.id.parentLayoutCheckout));

        // Click next to go to make payment
        Button buttonNext = fragment.findViewById(R.id.button19);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigator.setRedirection(new ActivityNavigator.NavigationManager() {
                    @Override
                    public void redirect() {
                        Intent i = new Intent(getContext(), PaymentActivity.class);
                        startActivity(i);
                    }
                });
            }
        });

        // Click back to go to select seat
        Button buttonPrev = fragment.findViewById(R.id.button20);
        buttonPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigator.setRedirection(new ActivityNavigator.NavigationManager() {
                    @Override
                    public void redirect() {
                        CheckoutActivity activity = (CheckoutActivity) getActivity();
                        activity.getCheckoutFragmentManager()
                                .setFragment(CheckoutActivity.CheckoutFragmentManager.FRAGMENT_SELECT_SEAT);
                    }
                });
            }
        });
    }
}