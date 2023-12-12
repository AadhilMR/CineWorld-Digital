package com.aadhil.cineworlddigital;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aadhil.cineworlddigital.adapter.CurrentMovieAdapter;
import com.aadhil.cineworlddigital.fragment.AppBar;
import com.aadhil.cineworlddigital.fragment.BottomNavigation;
import com.aadhil.cineworlddigital.model.CurrentMovie;
import com.aadhil.cineworlddigital.service.ActivityNavigator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class SearchActivity extends AppCompatActivity {
    public static final int NO_KEYWORD = 0;
    public static final int TYPE_NAME = 1;
    public static final int TYPE_DATE = 2;
    public static final int TYPE_LANG = 3;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseStorage storage = FirebaseStorage.getInstance();

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
        loadDates();
        loadLanguage();

        searchMovie("", SearchActivity.NO_KEYWORD);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        // Search by Keyword onType
        EditText search = findViewById(R.id.editTextText3);
        searchMovie(search.getText().toString(), SearchActivity.TYPE_NAME);
        return false;
    }

    private RecyclerView setMoviesToRecyclerView(@IdRes int resId) {
        RecyclerView recyclerView = findViewById(resId);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

        return recyclerView;
    }

    private void loadDates() {
        final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");
        final Calendar calendar = Calendar.getInstance();

        String[] spinnerItems = new String[6];
        spinnerItems[0] = "Date [ALL]";

        String today = sdf.format(calendar.getTime());
        spinnerItems[1] = today;

        for(int i=2; i<6; i++) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            String newDay = sdf.format(calendar.getTime());
            spinnerItems[i] = newDay;
        }

        Spinner spinner = findViewById(R.id.spinner);
        loadSpinnerItem(spinner, spinnerItems, SearchActivity.TYPE_DATE);
    }

    private void loadLanguage() {
        String[] spinnerItems = {"Language [ALL]", "English", "Hindi", "Sinhala", "Tamil"};
        Spinner spinner = findViewById(R.id.spinner2);
        loadSpinnerItem(spinner, spinnerItems, SearchActivity.TYPE_LANG);
    }

    private void loadSpinnerItem(Spinner spinner, String[] spinnerItems, int spinnerType) {
        // Setup adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = spinnerItems[position];
                searchMovie(selectedItem, spinnerType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do Nothing
            }
        });
    }

    private String getDurationHMFormat(int durationAsInt) {
        int hour = durationAsInt/60;
        int mins = durationAsInt%60;
        return hour + "h " + mins + "m";
    }

    private void searchMovie(String keyword, int searchType) {
        ActivityNavigator navigator = ActivityNavigator.getNavigator(this, findViewById(R.id.parentLayoutSearch));
        ArrayList<CurrentMovie> datalist = new ArrayList<>();

        RecyclerView.Adapter adapter = new CurrentMovieAdapter(SearchActivity.this, datalist, navigator).getAdapter();

        // Set current movies to recycler view
        setMoviesToRecyclerView(R.id.recyclerView3)
                .setAdapter(adapter);

        // Get movies from Firestore

        Task<QuerySnapshot> snapshots = db.collection("movies")
                .whereEqualTo("current", true).get();

        if(searchType == SearchActivity.TYPE_NAME && !keyword.isEmpty()) {
            snapshots = db.collection("movies").whereEqualTo("current", true)
                    .whereGreaterThanOrEqualTo("name", keyword)
                    .whereLessThan("name", keyword + "\uf8ff").get();
        } else if(searchType == SearchActivity.TYPE_LANG && !keyword.equals("Language [ALL]")) {
            snapshots = db.collection("movies").whereEqualTo("current", true)
                    .whereEqualTo("language", keyword).get();
        }
        snapshots.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for(DocumentSnapshot item : task.getResult().getDocuments()) {
                        // Create new Current Movie
                        CurrentMovie movie = new CurrentMovie();
                        movie.setDocumentId(item.getId())
                                .setMovieName(item.get("name").toString())
                                .setDuration(getDurationHMFormat(Integer.parseInt(item.get("duration").toString())))
                                .setLanguage(item.get("language").toString());

                        // Get Poster
                        storage.getReference("movie-image/" + item.get("imageId"))
                                .getDownloadUrl()
                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    movie.setImageId(uri);
                                    datalist.add(movie);
                                    // Update adapter
                                    adapter.notifyDataSetChanged();
                                    TextView searchCount = findViewById(R.id.textView21);
                                    searchCount.setText(String.valueOf(datalist.size()));
                                }
                            });
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(SearchActivity.this.getClass().getSimpleName(), e.getMessage());
            }
        });
    }
}