package com.aadhil.cineworlddigital.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.aadhil.cineworlddigital.R;
import com.aadhil.cineworlddigital.service.ActivityNavigator;

public class PayNow extends Fragment {
    public PayNow() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pay_now, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View fragment, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(fragment, savedInstanceState);

        // Get activity navigator
        ActivityNavigator navigator = ActivityNavigator.getNavigator(getContext(),
                getActivity().findViewById(R.id.parentLayoutPayment));

        // Click to pay
        Button button = fragment.findViewById(R.id.button25);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigator.setRedirection(new ActivityNavigator.NavigationManager() {
                    @Override
                    public void redirect() {
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragmentContainerView18, CompletePayment.class, null)
                                .commit();
                    }
                });
            }
        });
    }
}