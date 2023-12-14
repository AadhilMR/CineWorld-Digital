package com.aadhil.cineworlddigital.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.aadhil.cineworlddigital.CheckoutActivity;
import com.aadhil.cineworlddigital.HomeActivity;
import com.aadhil.cineworlddigital.R;
import com.aadhil.cineworlddigital.model.CheckoutInfo;
import com.aadhil.cineworlddigital.service.ActivityNavigator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SelectMovie extends Fragment {
    private final HashMap<String, Button> showTimeButtons = new HashMap<>();
    private final HashMap<String, Button> dateButtons = new HashMap<>();

    private View fragment = null;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private Button lastClickedDateButton = null;
    private Button lastClickedMovieButton = null;
    private Button lastClickedShowtimeButton = null;

    private HashMap<String, ArrayList<String>> seats = new HashMap<>();

    public SelectMovie() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_select_movie, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View fragment, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(fragment, savedInstanceState);

        this.fragment = fragment;

        // Set buttons
        setButtonData(fragment);

        // Load dates
        setDateButtonSelection();

        // Get activity navigator
        ActivityNavigator navigator = ActivityNavigator.getNavigator(getContext(),
                getActivity().findViewById(R.id.parentLayoutCheckout));

        // Click next to go to select seat
        Button buttonNext = fragment.findViewById(R.id.button19);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckoutActivity activity = (CheckoutActivity) getActivity();
                CheckoutInfo checkoutInfo = activity.getCheckoutInfo();

                if(checkoutInfo.getDate() == null || checkoutInfo.getDate().isEmpty()) {
                    Toast.makeText(getContext(), "Select a Date", Toast.LENGTH_LONG).show();
                } else if(checkoutInfo.getMovieName() == null || checkoutInfo.getMovieName().isEmpty()) {
                    Toast.makeText(getContext(), "Select a Movie", Toast.LENGTH_LONG).show();
                } else if(checkoutInfo.getShowTime() == null || checkoutInfo.getShowTime().isEmpty()) {
                    Toast.makeText(getContext(), "Select a ShowTime", Toast.LENGTH_LONG).show();
                } else {
                    navigator.setRedirection(new ActivityNavigator.NavigationManager() {
                        @Override
                        public void redirect() {
                            activity.getCheckoutFragmentManager()
                                    .setFragment(CheckoutActivity.CheckoutFragmentManager.FRAGMENT_SELECT_SEAT);
                        }
                    });
                }
            }
        });

        // Click cancel to go to home
        Button buttonCancel = fragment.findViewById(R.id.button20);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigator.setRedirection(new ActivityNavigator.NavigationManager() {
                    @Override
                    public void redirect() {
                        Intent i = new Intent(getContext(), HomeActivity.class);
                        startActivity(i);
                    }
                });
            }
        });
    }

    private View getFragment() {
        return this.fragment;
    }

    private void setButtonData(View fragment) {
        // Set showtime buttons to map
        showTimeButtons.put("10.30AM", fragment.findViewById(R.id.button14));
        showTimeButtons.put("1.15PM", fragment.findViewById(R.id.button15));
        showTimeButtons.put("4PM", fragment.findViewById(R.id.button16));
        showTimeButtons.put("7PM", fragment.findViewById(R.id.button17));
        showTimeButtons.put("11PM", fragment.findViewById(R.id.button18));

        // Set date buttons to map
        final SimpleDateFormat sdf = new SimpleDateFormat("E, dd MMM");
        final Calendar calendar = Calendar.getInstance();

        dateButtons.put("btnDay1", fragment.findViewById(R.id.button11));
        dateButtons.put("btnDay2", fragment.findViewById(R.id.button12));
        dateButtons.put("btnDay3", fragment.findViewById(R.id.button13));
        dateButtons.put("btnDay4", fragment.findViewById(R.id.button27));
        dateButtons.put("btnDay5", fragment.findViewById(R.id.button28));

        for(int i=2; i<6; i++) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            String newDay = sdf.format(calendar.getTime());
            dateButtons.get("btnDay" + i).setText(newDay);
        }
    }

    private void setDateButtonSelection() {
        for(Map.Entry<String, Button> button : dateButtons.entrySet()) {
            Button currentButton = (Button) button.getValue();
            currentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Convert date into mm/dd format
                    SimpleDateFormat sdfIn = new SimpleDateFormat("E, dd MMM", Locale.ENGLISH);
                    SimpleDateFormat sdfOut = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

                    try {
                        String date;
                        if(currentButton.getText().toString().equals("Today")) {
                            date = sdfOut.format(Calendar.getInstance().getTime());
                        } else {
                            Date parsedDate = sdfIn.parse(currentButton.getText().toString());

                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(parsedDate);
                            calendar.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));

                            date = sdfOut.format(calendar.getTime());
                        }

                        // Search and load movie list
                        getMovieList(date);

                        // Set checkoutInfo
                        CheckoutActivity activity = (CheckoutActivity) getActivity();
                        activity.getCheckoutInfo().setDate(date);

                        // Set selected button
                        if(!currentButton.equals(lastClickedDateButton)) {
                            if(lastClickedDateButton != null) {
                                lastClickedDateButton.setTextColor(getActivity().getColor(R.color.primary_foreground));
                                lastClickedDateButton.setBackgroundColor(getActivity().getColor(R.color.transparent));
                            }

                            currentButton.setTextColor(getActivity().getColor(R.color.primary_theme));
                            currentButton.setBackgroundColor(getActivity().getColor(R.color.primary_foreground));

                            lastClickedDateButton = currentButton;
                        }
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                    }
                }
            });
        }
    }

    private void setShowTimeButtonSelection() {
        for(Map.Entry<String, Button> button : showTimeButtons.entrySet()) {
            Button currentButton = (Button) button.getValue();
            currentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Set checkoutInfo
                    CheckoutActivity activity = (CheckoutActivity) getActivity();
                    activity.getCheckoutInfo()
                            .setShowTime(currentButton.getText().toString())
                            .setSeats(seats.get(currentButton.getText().toString()));


                    // Set selected button
                    if(!currentButton.equals(lastClickedShowtimeButton)) {
                        if(lastClickedShowtimeButton != null) {
                            lastClickedShowtimeButton.setTextColor(getActivity().getColor(R.color.primary_foreground));
                            lastClickedShowtimeButton.setBackgroundColor(getActivity().getColor(R.color.transparent));
                        }

                        currentButton.setTextColor(getActivity().getColor(R.color.primary_theme));
                        currentButton.setBackgroundColor(getActivity().getColor(R.color.primary_foreground));

                        lastClickedShowtimeButton = currentButton;
                    }
                }
            });
        }
    }

    private void getMovieList(String date) {
        LinearLayout buttonContainer = getFragment().findViewById(R.id.linearLayout4);
        buttonContainer.removeAllViews();
        db.collection("movieDates").whereEqualTo("date", date).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for(DocumentSnapshot movieDate : task.getResult().getDocuments()) {
                        String movieDateId = movieDate.getId();
                        String movieId = movieDate.get("movieId").toString();

                        db.document("/movies/" + movieId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()) {
                                    // Create Button
                                    LayoutInflater inflater = LayoutInflater.from(getContext());
                                    ConstraintLayout layout = (ConstraintLayout) inflater.inflate(R.layout.movie_button, null);
                                    Button button = layout.findViewById(R.id.button9);
                                    ((ViewGroup) button.getParent()).removeView(button);

                                    button.setText(task.getResult().get("name").toString());
                                    button.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            // Set checkoutInfo
                                            CheckoutActivity activity = (CheckoutActivity) getActivity();
                                            activity.getCheckoutInfo()
                                                    .setMovieName(button.getText().toString())
                                                    .setMovieId(movieId);

                                            // Set selected button
                                            if(!button.equals(lastClickedMovieButton)) {
                                                if(lastClickedMovieButton != null) {
                                                    lastClickedMovieButton.setTextColor(getActivity().getColor(R.color.primary_foreground));
                                                    lastClickedMovieButton.setBackgroundColor(getActivity().getColor(R.color.transparent));
                                                }

                                                button.setTextColor(getActivity().getColor(R.color.primary_theme));
                                                button.setBackgroundColor(getActivity().getColor(R.color.primary_foreground));

                                                lastClickedMovieButton = button;
                                            }

                                            // Set currently showing buttons invisible
                                            setShowTimeInvisible();

                                            // Set showtime buttons
                                            db.collection("/movieDates/" + movieDateId + "/showTimes").get()
                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if(task.isSuccessful()) {
                                                            for(DocumentSnapshot showTime : task.getResult().getDocuments()) {
                                                                ArrayList<String> seatsOfCurrentShowtime = (ArrayList<String>) showTime.get("seat");
                                                                String currentShowTime = showTime.get("showtime").toString();
                                                                seats.put(currentShowTime, seatsOfCurrentShowtime);
                                                                setShowTimeVisible(currentShowTime);
                                                            }
                                                            setShowTimeButtonSelection();
                                                        }
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.e(getActivity().getClass().getSimpleName(), e.getMessage());
                                                    }
                                                });
                                        }
                                    });

                                    // Create Space
                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT
                                    );
                                    params.height = 10;
                                    Space space = new Space(getContext());
                                    space.setLayoutParams(params);

                                    // Add the button and space
                                    buttonContainer.addView(button);
                                    buttonContainer.addView(space);
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(getActivity().getClass().getSimpleName(), e.getMessage());
                            }
                        });
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(getActivity().getClass().getSimpleName(), e.getMessage());
            }
        });
    }

    private void setShowTimeVisible(String showTime) {
        Button button = showTimeButtons.get(showTime);
        button.setVisibility(View.VISIBLE);
    }

    private void setShowTimeInvisible() {
        for(Map.Entry<String, Button> showTimeButton : showTimeButtons.entrySet()) {
            Button button = (Button) showTimeButton.getValue();
            button.setVisibility(View.GONE);
        }
    }
}
