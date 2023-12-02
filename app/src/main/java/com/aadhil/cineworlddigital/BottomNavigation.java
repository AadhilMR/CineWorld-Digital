package com.aadhil.cineworlddigital;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class BottomNavigation extends Fragment {
    private static final String ACTIVITY_HOME = "com.aadhil.cineworlddigital.HomeActivity";
    private static final String ACTIVITY_SEARCH = "com.aadhil.cineworlddigital.SearchActivity";
    private static final String ACTIVITY_SETTINGS = "com.aadhil.cineworlddigital.SettingsActivity";

    public BottomNavigation() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bottom_navigation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View fragment, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(fragment, savedInstanceState);

        setSelectedIcon(fragment);

        Button buttonHome = fragment.findViewById(R.id.button5);
        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!getActivity().getClass().getName().equals(BottomNavigation.ACTIVITY_HOME)) {
                    Intent intent = new Intent(fragment.getContext(), HomeActivity.class);
                    startActivity(intent);
                }
            }
        });

        Button buttonSearch = fragment.findViewById(R.id.button6);
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!getActivity().getClass().getName().equals(BottomNavigation.ACTIVITY_SEARCH)) {
                    // Intent intent = new Intent(fragment.getContext(), );
                    // startActivity(intent);
                }
            }
        });

        Button buttonSetting = fragment.findViewById(R.id.button7);
        buttonSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!getActivity().getClass().getName().equals(BottomNavigation.ACTIVITY_SETTINGS)) {
                    // Intent intent = new Intent(fragment.getContext(), );
                    // startActivity(intent);
                }
            }
        });
    }

    /**
     * setNavigationBar() method set this bottom navigation bar into activity
     * which is calling the method.
     *
     * @param fragmentManager denotes supported fragment manager
     * @param containerViewId denotes fragment container resource id
     */
    public static void setNavigationBar(@NonNull FragmentManager fragmentManager,
                                 @NonNull @IdRes int containerViewId) {
        fragmentManager
                .beginTransaction()
                .add(containerViewId, BottomNavigation.class, null)
                .commit();
    }

    private void setSelectedIcon(View fragment) {

        switch (getActivity().getClass().getName()) {
            case BottomNavigation.ACTIVITY_HOME:
                Button buttonHome = fragment.findViewById(R.id.button5);
                buttonHome.setBackgroundColor(getActivity().getColor(R.color.primary_theme));
                break;

            case BottomNavigation.ACTIVITY_SEARCH:
                Button buttonSearch = fragment.findViewById(R.id.button6);
                buttonSearch.setBackgroundColor(getActivity().getColor(R.color.primary_theme));
                break;

            case BottomNavigation.ACTIVITY_SETTINGS:
                Button buttonSetting = fragment.findViewById(R.id.button7);
                buttonSetting.setBackgroundColor(getActivity().getColor(R.color.primary_theme));
                break;
        }
    }
}