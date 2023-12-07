package com.aadhil.cineworlddigital;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aadhil.cineworlddigital.adapter.CurrentMovieAdapter;
import com.aadhil.cineworlddigital.fragment.AppBar;
import com.aadhil.cineworlddigital.fragment.BottomNavigation;
import com.aadhil.cineworlddigital.model.CurrentMovie;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Set App Bar
        AppBar.setAppBar(getSupportFragmentManager(), R.id.fragmentContainerView4,
                findViewById(R.id.parentLayoutSearch));

        // Set Bottom Navigation
        BottomNavigation.setNavigationBar(getSupportFragmentManager(), R.id.fragmentContainerView5);

        // Load Spinners
        loadShowTimes();
        loadLanguage();

        // TEMP: Fake ArrayList to pass to the adapters
        ArrayList<CurrentMovie> datalist = new ArrayList<>();

        // Set current movies to recycler view
        setMoviesToRecyclerView(R.id.recyclerView3)
                .setAdapter(new CurrentMovieAdapter(this, datalist).getAdapter());

    }

    private RecyclerView setMoviesToRecyclerView(@IdRes int resId) {
        RecyclerView recyclerView = findViewById(resId);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));

        return recyclerView;
    }

    private void loadShowTimes() {
        /**
         * TEMP: temporary spinner items
         */
        String[] spinnerItems = {"Show Time [ALL]", "10.30AM", "1.15PM", "4PM", "7.30PM", "11PM"};
        Spinner spinner = findViewById(R.id.spinner);
        loadSpinnerItem(spinner, spinnerItems);
    }

    private void loadLanguage() {
        /**
         * TEMP: temporary spinner items
         */
        String[] spinnerItems = {"Language [ALL]", "English", "Hindi", "Sinhala", "Tamil"};
        Spinner spinner = findViewById(R.id.spinner2);
        loadSpinnerItem(spinner, spinnerItems);
    }

    private void loadSpinnerItem(Spinner spinner, String[] spinnerItems) {
        // Setup adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = spinnerItems[position];
                Toast.makeText(SearchActivity.this, selectedItem, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do Nothing
            }
        });
    }
}