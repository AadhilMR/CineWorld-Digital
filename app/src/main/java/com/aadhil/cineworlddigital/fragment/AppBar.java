package com.aadhil.cineworlddigital.fragment;

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

import com.aadhil.cineworlddigital.R;

import java.util.HashMap;
import java.util.Map;

public class AppBar extends Fragment {

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

        ImageButton imageButton = fragment.findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu menu = createMenu(fragment, R.menu.main_menu);
                setMenuAction(menu).show();
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
    private PopupMenu setMenuAction(PopupMenu menu) {
        Map<Integer, MenuRedirector> map = new HashMap<>();

        map.put(R.id.searchMovie, () -> {
            System.out.println("Search Movie");
        });

        map.put(R.id.buyTickets, () -> {
            System.out.println("Buy Tickets");
        });

        map.put(R.id.settings, () -> {
            System.out.println("Settings");
        });

        map.put(R.id.logout, () -> {
            System.out.println("Logout");
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
                                        @IdRes int containerViewId) {
        fragmentManager
                .beginTransaction()
                .add(containerViewId, AppBar.class, null)
                .commit();
    }

    interface MenuRedirector {
        void redirect();
    }
}