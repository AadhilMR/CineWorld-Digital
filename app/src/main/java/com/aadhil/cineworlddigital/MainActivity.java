package com.aadhil.cineworlddigital;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    public static final int LOGIN_FRAGMENT = 1;
    public static final int REGISTER_FRAGMENT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setFragment(MainActivity.LOGIN_FRAGMENT);
    }

    public void setFragment(int fragmentType) {
        Class fragmentClass = null;

        if(fragmentType == MainActivity.LOGIN_FRAGMENT) {
            fragmentClass = Login.class;
        } else if(fragmentType == MainActivity.REGISTER_FRAGMENT) {
            fragmentClass = Register.class;
        }
        FragmentContainerView containerView = findViewById(R.id.fragmentContainerView);

        try {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragmentContainerView, fragmentClass, null)
                    .commit();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }
}