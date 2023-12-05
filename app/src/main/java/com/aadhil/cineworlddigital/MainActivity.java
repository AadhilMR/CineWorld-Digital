package com.aadhil.cineworlddigital;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.aadhil.cineworlddigital.fragment.Login;
import com.aadhil.cineworlddigital.fragment.Register;

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