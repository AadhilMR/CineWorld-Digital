package com.aadhil.cineworlddigital.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.aadhil.cineworlddigital.R;
import com.aadhil.cineworlddigital.SettingsActivity;

public class SettingsUser extends Fragment {
    public SettingsUser() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View fragment, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(fragment, savedInstanceState);

        // Go to SettingsHome Fragment
        RelativeLayout layoutClickable = fragment.findViewById(R.id.relativeLayout2);
        layoutClickable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsActivity activity = (SettingsActivity) getActivity();
                activity.setFragment(SettingsActivity.SETTINGS_HOME);
            }
        });
    }
}