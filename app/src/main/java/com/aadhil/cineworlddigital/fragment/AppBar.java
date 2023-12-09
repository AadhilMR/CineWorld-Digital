package com.aadhil.cineworlddigital.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import androidx.annotation.IdRes;
import androidx.annotation.MenuRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.aadhil.cineworlddigital.CheckoutActivity;
import com.aadhil.cineworlddigital.FindUsActivity;
import com.aadhil.cineworlddigital.MainActivity;
import com.aadhil.cineworlddigital.R;
import com.aadhil.cineworlddigital.SearchActivity;
import com.aadhil.cineworlddigital.SettingsActivity;
import com.aadhil.cineworlddigital.service.ActivityNavigator;

import java.util.HashMap;
import java.util.Map;

public class AppBar extends Fragment {
    private static ViewGroup layout;
    private ActivityNavigator navigator;

    public AppBar() {
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
        return inflater.inflate(R.layout.fragment_app_bar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View fragment, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(fragment, savedInstanceState);

        // Get activity navigator
        navigator = ActivityNavigator.getNavigator(getContext(), AppBar.layout);

        ImageButton imageButton = fragment.findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu menu = createMenu(fragment, R.menu.main_menu);
                setMenuAction(menu, fragment).show();
            }
        });
    }

    /**
     * setRedirector() method gives the redirection process to
     * ActivityNavigator object to handle redirections.
     *
     * @param activityClass
     * @param fragment
     */
    private void setRedirector(Class activityClass, View fragment) {
        navigator.setRedirection(new ActivityNavigator.NavigationManager() {
            @Override
            public void redirect() {
                Intent intent = new Intent(fragment.getContext(), activityClass);
                startActivity(intent);
            }
        });
    }

    /**
     * createMenu() method creates a PopupMenu object from given menu as
     * a menu resource.
     *
     * @param fragment the fragment which opens the menu
     * @param menuRes a xml menu resource to inflate the design
     * @return PopupMenu Object
     *
     * By default, this method creates the menu with End Gravity
     */
    private PopupMenu createMenu(@NonNull View fragment,@MenuRes int menuRes) {
        PopupMenu menu = new PopupMenu(getContext(), fragment, Gravity.END);
        menu.inflate(menuRes);
        return menu;
    }

    /**
     * setMenuAction() method sets action to each menu item.
     *
     * @param menu the menu which is already created as a PopupMenu object
     * @return PopupMenu Object which is modified by this method
     */
    private PopupMenu setMenuAction(PopupMenu menu, View fragment) {
        Map<Integer, MenuRedirector> map = new HashMap<>();

        map.put(R.id.searchMovie, () -> {
            setRedirector(SearchActivity.class, fragment);
        });

        map.put(R.id.buyTickets, () -> {
            setRedirector(CheckoutActivity.class, fragment);
        });

        map.put(R.id.settings, () -> {
            setRedirector(SettingsActivity.class, fragment);
        });

        map.put(R.id.logout, () -> {
            MainActivity.currentUser = null;
            setRedirector(MainActivity.class, fragment);
        });

        map.put(R.id.findUs, () -> {
            setRedirector(FindUsActivity.class, fragment);
        });

        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                try {
                    MenuRedirector menuRedirector = map.get(item.getItemId());

                    if(menuRedirector == null) { throw new IllegalArgumentException("Invalid menu item"); }

                    menuRedirector.redirect();

                    return true;
                } catch (Exception ex) {
                    return false;
                }
            }
        });

        return menu;
    }

    /**
     * setNavigationBar() method set this bottom navigation bar into activity
     * which is calling the method.
     *
     * @param fragmentManager denotes supported fragment manager
     * @param containerViewId denotes fragment container resource id
     */
    public static void setAppBar(@NonNull FragmentManager fragmentManager,
                                        @IdRes int containerViewId, ViewGroup layout) {
        AppBar.layout = layout;

        fragmentManager
                .beginTransaction()
                .add(containerViewId, AppBar.class, null)
                .commit();
    }

    interface MenuRedirector {
        void redirect();
    }
}