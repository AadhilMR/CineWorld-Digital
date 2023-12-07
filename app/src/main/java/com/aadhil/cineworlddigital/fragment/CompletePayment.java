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

import com.aadhil.cineworlddigital.HomeActivity;
import com.aadhil.cineworlddigital.R;
import com.aadhil.cineworlddigital.service.ActivityNavigator;

public class CompletePayment extends Fragment {
    public CompletePayment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_complete_payment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View fragmenet, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(fragmenet, savedInstanceState);

        // Get activity navigator
        ActivityNavigator navigator = ActivityNavigator.getNavigator(getContext(),
                getActivity().findViewById(R.id.parentLayoutPayment));

        Button button = fragmenet.findViewById(R.id.button24);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * TEMP: add here the ticket downloading process
                 */
                navigator.setRedirection(new ActivityNavigator.NavigationManager() {
                    @Override
                    public void redirect() {
                        Intent i = new Intent(getContext(), HomeActivity.class);
                        startActivity(i);
                    }
                });
            }
        });
    }
}