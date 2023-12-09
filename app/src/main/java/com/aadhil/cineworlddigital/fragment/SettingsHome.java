package com.aadhil.cineworlddigital.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.aadhil.cineworlddigital.MainActivity;
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

        // Get User Details
        showUserDetails(fragment);

        // Go to Change User Credentials Fragment
        RelativeLayout layoutClickable = fragment.findViewById(R.id.relativeLayout);
        layoutClickable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsActivity activity = (SettingsActivity) getActivity();
                activity.setFragment(SettingsActivity.SETTINGS_USER);
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
                SettingsActivity activity = (SettingsActivity) getActivity();
                activity.setFragment(SettingsActivity.SETTINGS_AUTH);
            }
        });

        // Switch for Save or Not Card Details
        SwitchMaterial switch1 = fragment.findViewById(R.id.switch1);

        if(getActivity().getSharedPreferences("user_preferences", Context.MODE_PRIVATE).getBoolean("allowSaveCard", false)) {
            switch1.setChecked(true);
            switch1.setThumbTintList(ContextCompat
                    .getColorStateList(getContext(), R.color.primary_theme));
            switch1.setTrackTintList(ContextCompat
                    .getColorStateList(getContext(), R.color.light_yellow));
        } else {
            switch1.setChecked(false);
            switch1.setThumbTintList(ContextCompat
                    .getColorStateList(getContext(), R.color.primary_background));
            switch1.setTrackTintList(ContextCompat
                    .getColorStateList(getContext(), R.color.light_ash));
        }

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isChecked()) {
                    // Change Design
                    switch1.setThumbTintList(ContextCompat
                            .getColorStateList(getContext(), R.color.primary_theme));
                    switch1.setTrackTintList(ContextCompat
                            .getColorStateList(getContext(), R.color.light_yellow));
                    switch1.setText("Will Save at Next Time");

                    // Change Settings
                    SharedPreferences.Editor editor = getActivity().getSharedPreferences("user_preferences", Context.MODE_PRIVATE).edit();
                    editor.putBoolean("allowSaveCard", true);
                    editor.apply();
                } else {
                    // Change Design
                    switch1.setThumbTintList(ContextCompat
                            .getColorStateList(getContext(), R.color.primary_background));
                    switch1.setTrackTintList(ContextCompat
                            .getColorStateList(getContext(), R.color.light_ash));
                    switch1.setText("");

                    // Change Settings
                    SharedPreferences.Editor editor = getActivity().getSharedPreferences("user_preferences", Context.MODE_PRIVATE).edit();
                    editor.putBoolean("allowSaveCard", false);
                    editor.remove("nameOnCard")
                            .remove("cardNumber")
                            .remove("expireDate")
                            .remove("cvv").commit();
                    editor.apply();
                }
            }
        });

        // Switch for Use or not Biometric Fingerprint Authentication
        SwitchMaterial switch2 = fragment.findViewById(R.id.switch2);

        if(getActivity().getSharedPreferences("user_preferences", Context.MODE_PRIVATE).getBoolean("allowBiometricLogin", false)) {
            switch2.setChecked(true);
            switch2.setThumbTintList(ContextCompat
                    .getColorStateList(getContext(), R.color.primary_theme));
            switch2.setTrackTintList(ContextCompat
                    .getColorStateList(getContext(), R.color.light_yellow));
        } else {
            switch2.setChecked(false);
            switch2.setThumbTintList(ContextCompat
                    .getColorStateList(getContext(), R.color.primary_background));
            switch2.setTrackTintList(ContextCompat
                    .getColorStateList(getContext(), R.color.light_ash));
        }

        switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isChecked()) {
                    // Change Design
                    switch2.setThumbTintList(ContextCompat
                            .getColorStateList(getContext(), R.color.primary_theme));
                    switch2.setTrackTintList(ContextCompat
                            .getColorStateList(getContext(), R.color.light_yellow));

                    // Change Settings
                    SharedPreferences.Editor editor = getActivity().getSharedPreferences("user_preferences", Context.MODE_PRIVATE).edit();
                    editor.putBoolean("allowBiometricLogin", true);
                    editor.apply();
                } else {
                    // Change Design
                    switch2.setThumbTintList(ContextCompat
                            .getColorStateList(getContext(), R.color.primary_background));
                    switch2.setTrackTintList(ContextCompat
                            .getColorStateList(getContext(), R.color.light_ash));

                    // Change Settings
                    SharedPreferences.Editor editor = getActivity().getSharedPreferences("user_preferences", Context.MODE_PRIVATE).edit();
                    editor.putBoolean("allowBiometricLogin", false);
                    editor.apply();
                }
            }
        });
    }

    private void showCardDetails() {
        SharedPreferences preferences = getActivity().getSharedPreferences("user_preferences", Context.MODE_PRIVATE);

        Boolean allowSaveCard = preferences.getBoolean("allowSaveCard", false);
        String nameOnCard = preferences.getString("nameOnCard", null);
        String cardNumber = preferences.getString("cardNumber", null);
        String expireDate = preferences.getString("expireDate", null);
        String cvv = preferences.getString("cvv", null);

        String details = "This app did not save your card details.";

        if(allowSaveCard && nameOnCard != null && cardNumber != null && expireDate != null && cvv != null) {
            details = "\nName on Card: "+ nameOnCard +"\n\nCard Number: "+ cardNumber +"\n\nExpire Date: "+ expireDate +"\n\nCVV: "+ cvv;
        }

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

    private void showUserDetails(View fragment) {
        TextView fname = fragment.findViewById(R.id.textView65);
        TextView lname = fragment.findViewById(R.id.textView67);
        TextView mobile = fragment.findViewById(R.id.textView69);
        TextView email = fragment.findViewById(R.id.textView71);

        fname.setText(MainActivity.currentUser.getFname());
        lname.setText(MainActivity.currentUser.getLname());
        mobile.setText(MainActivity.currentUser.getMobile());
        email.setText(MainActivity.currentUser.getEmail());
    }
}