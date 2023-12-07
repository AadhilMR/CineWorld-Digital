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
import com.aadhil.cineworlddigital.MainActivity;
import com.aadhil.cineworlddigital.R;
import com.aadhil.cineworlddigital.service.ActivityNavigator;

public class Login extends Fragment {
    public Login() {
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
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View fragment, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(fragment, savedInstanceState);

        // Get activity navigator
        ActivityNavigator navigator = ActivityNavigator.getNavigator(getContext(),
                getActivity().findViewById(R.id.parentLayoutMain));

        // Check user validity and login
        Button button1 = fragment.findViewById(R.id.button);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * TEMP: Temporarily added for check designs
                 */
                navigator.setRedirection(new ActivityNavigator.NavigationManager() {
                    @Override
                    public void redirect() {
                        Intent intent = new Intent(fragment.getContext(), HomeActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });

        // Change the view to registration
        Button button2 = fragment.findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigator.setRedirection(new ActivityNavigator.NavigationManager() {
                    @Override
                    public void redirect() {
                        MainActivity mainActivity = (MainActivity) getActivity();
                        mainActivity.setFragment(MainActivity.REGISTER_FRAGMENT);
                    }
                });
            }
        });
    }
}