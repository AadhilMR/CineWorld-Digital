package com.aadhil.cineworlddigital.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.aadhil.cineworlddigital.R;
import com.aadhil.cineworlddigital.SettingsActivity;
import com.aadhil.cineworlddigital.service.ActivityNavigator;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class SettingsHome extends Fragment {
    public SettingsHome() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View fragment, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(fragment, savedInstanceState);

        // Get activity navigator
        ActivityNavigator navigator = ActivityNavigator.getNavigator(getContext(),
                fragment.findViewById(R.id.parentLayoutSettings));

        // Go to Change User Credentials Fragment
        RelativeLayout layoutClickable = fragment.findViewById(R.id.relativeLayout);
        layoutClickable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigator.setRedirection(new ActivityNavigator.NavigationManager() {
                    @Override
                    public void redirect() {
                        SettingsActivity activity = (SettingsActivity) getActivity();
                        activity.setFragment(SettingsActivity.SETTINGS_USER);
                    }
                });
            }
        });

        // Show Card Details
        RelativeLayout layoutClickable2 = fragment.findViewById(R.id.relativeLayout2);
        layoutClickable2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * DO: Authenticate the user first
                 */
                showCardDetails();
            }
        });

        // Go to Change Authentication Fragment
        RelativeLayout layoutClickable3 = fragment.findViewById(R.id.relativeLayout4);
        layoutClickable3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigator.setRedirection(new ActivityNavigator.NavigationManager() {
                    @Override
                    public void redirect() {
                        SettingsActivity activity = (SettingsActivity) getActivity();
                        activity.setFragment(SettingsActivity.SETTINGS_AUTH);
                    }
                });
            }
        });

        // Switch for Save or Not Card Details
        SwitchMaterial switch1 = fragment.findViewById(R.id.switch1);
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isChecked()) {
                    switch1.setThumbTintList(ContextCompat
                            .getColorStateList(getContext(), R.color.primary_theme));
                    switch1.setTrackTintList(ContextCompat
                            .getColorStateList(getContext(), R.color.light_yellow));
                    switch1.setText("Will Save at Next Time");
                } else {
                    switch1.setThumbTintList(ContextCompat
                            .getColorStateList(getContext(), R.color.primary_background));
                    switch1.setTrackTintList(ContextCompat
                            .getColorStateList(getContext(), R.color.light_ash));
                    switch1.setText("");
                }
            }
        });

        // Switch for Use or not Biometric Fingerprint Authentication
        SwitchMaterial switch2 = fragment.findViewById(R.id.switch2);
        switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isChecked()) {
                    switch2.setThumbTintList(ContextCompat
                            .getColorStateList(getContext(), R.color.primary_theme));
                    switch2.setTrackTintList(ContextCompat
                            .getColorStateList(getContext(), R.color.light_yellow));
                    switch2.setText("Fingerprint Authentication is ON");
                } else {
                    switch2.setThumbTintList(ContextCompat
                            .getColorStateList(getContext(), R.color.primary_background));
                    switch2.setTrackTintList(ContextCompat
                            .getColorStateList(getContext(), R.color.light_ash));
                    switch2.setText("Fingerprint Authentication is OFF");
                }
            }
        });
    }

    private void showCardDetails() {
        //String details = "\nName on Card: M R A AHAMED\n\nCard Number: 4512623542745689\n\nExpire Date: 12/28\n\nCVV: 435";
        String details = "This app did not save your card details.";

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("Your Card Details")
                .setMessage(details)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }
}