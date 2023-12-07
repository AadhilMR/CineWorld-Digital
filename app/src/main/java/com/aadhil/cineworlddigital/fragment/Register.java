package com.aadhil.cineworlddigital.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.aadhil.cineworlddigital.MainActivity;
import com.aadhil.cineworlddigital.R;
import com.aadhil.cineworlddigital.service.ActivityNavigator;

public class Register extends Fragment {
    public Register() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View fragment, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(fragment, savedInstanceState);

        // Get activity navigator
        ActivityNavigator navigator = ActivityNavigator.getNavigator(getContext(),
                getActivity().findViewById(R.id.parentLayoutMain));

        // Check validity and register the user, and then redirect to login
        Button button1 = fragment.findViewById(R.id.button3);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * TEMP: temporary set redirection
                 */
                redirectToLogin(navigator);
            }
        });

        // Change the view to login
        Button button2 = fragment.findViewById(R.id.button4);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectToLogin(navigator);
            }
        });
    }

    private void redirectToLogin(ActivityNavigator navigator) {
        navigator.setRedirection(new ActivityNavigator.NavigationManager() {
            @Override
            public void redirect() {
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.setFragment(MainActivity.LOGIN_FRAGMENT);
            }
        });
    }
}