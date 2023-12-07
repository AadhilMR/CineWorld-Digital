package com.aadhil.cineworlddigital;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.aadhil.cineworlddigital.fragment.AppBar;
import com.aadhil.cineworlddigital.fragment.BottomNavigation;
import com.aadhil.cineworlddigital.fragment.SettingsAuth;
import com.aadhil.cineworlddigital.fragment.SettingsHome;
import com.aadhil.cineworlddigital.fragment.SettingsUser;

import java.util.HashMap;

public class SettingsActivity extends AppCompatActivity {
    public static final int SETTINGS_HOME = 1;
    public static final int SETTINGS_USER = 2;
    public static final int SETTINGS_AUTH = 3;

    private final HashMap<Integer, FragmentService> fragmentMapper = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Set App Bar
        AppBar.setAppBar(getSupportFragmentManager(), R.id.fragmentContainerView11,
                findViewById(R.id.parentLayoutSettings));

        // Set Bottom Navigation
        BottomNavigation.setNavigationBar(getSupportFragmentManager(), R.id.fragmentContainerView12,
                findViewById(R.id.parentLayoutSettings));

        // Set fragment services
        setFragmentService();

        // By default, set the 'SettingHome' fragment
        setFragment(SettingsActivity.SETTINGS_HOME);
    }

    private void setFragmentService() {
        fragmentMapper.put(SettingsActivity.SETTINGS_HOME, new FragmentService() {
            @Override
            public FragmentAnimator getAnimator() throws IllegalArgumentException {
                FragmentAnimator animator = new FragmentAnimator(
                        FragmentAnimator.ENTER_FROM_LEFT, FragmentAnimator.EXIT_TO_RIGHT
                );
                return animator;
            }

            @Override
            public Class getFragmentClass() {
                return SettingsHome.class;
            }
        });
        fragmentMapper.put(SettingsActivity.SETTINGS_USER, new FragmentService() {
            @Override
            public FragmentAnimator getAnimator() throws IllegalArgumentException {
                FragmentAnimator animator = new FragmentAnimator(
                        FragmentAnimator.ENTER_FROM_RIGHT, FragmentAnimator.EXIT_TO_LEFT
                );
                return animator;
            }

            @Override
            public Class getFragmentClass() {
                return SettingsUser.class;
            }
        });
        fragmentMapper.put(SettingsActivity.SETTINGS_AUTH, new FragmentService() {
            @Override
            public FragmentAnimator getAnimator() throws IllegalArgumentException{
                FragmentAnimator animator = new FragmentAnimator(
                        FragmentAnimator.ENTER_FROM_RIGHT, FragmentAnimator.EXIT_TO_RIGHT
                );
                return animator;
            }

            @Override
            public Class getFragmentClass() {
                return SettingsAuth.class;
            }
        });
    }

    private HashMap<Integer, FragmentService> getFragmentMapper() {
        return fragmentMapper;
    }

    public void setFragment(int fragmentId) {
        try {
            FragmentService service = getFragmentMapper().get(fragmentId);

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(service.getAnimator().getEnterAnimId(), service.getAnimator().getExitAnimId());
            fragmentTransaction.replace(R.id.fragmentContainerView14, service.getFragmentClass(), null).commit();
        } catch (IllegalArgumentException exception) {
            exception.printStackTrace();
        }
    }
}

class FragmentAnimator {
    public static final int ENTER_FROM_LEFT = 1;
    public static final int ENTER_FROM_RIGHT = 2;
    public static final int EXIT_TO_LEFT = 3;
    public static final int EXIT_TO_RIGHT = 4;

    private int enterAnimId;
    private int exitAnimId;

    public FragmentAnimator(int enterAnimId, int exitAnimId) throws IllegalArgumentException {
        if(enterAnimId == 1) {
            this.enterAnimId = R.anim.enter_from_left;
        } else if(enterAnimId == 2) {
            this.enterAnimId = R.anim.enter_from_right;
        } else {
            throw new IllegalArgumentException("Wrong parameter is passed");
        }

        if(exitAnimId == 3) {
            this.exitAnimId = R.anim.exit_to_left;
        } else if(exitAnimId == 4) {
            this.exitAnimId = R.anim.exit_to_right;
        } else {
            throw new IllegalArgumentException("Wrong parameter is passed");
        }
    }

    public int getEnterAnimId() {
        return enterAnimId;
    }

    public int getExitAnimId() {
        return exitAnimId;
    }
}

interface FragmentService {
     FragmentAnimator getAnimator();

     Class getFragmentClass();
}