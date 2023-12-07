package com.aadhil.cineworlddigital.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.aadhil.cineworlddigital.CheckoutActivity;
import com.aadhil.cineworlddigital.R;
import com.aadhil.cineworlddigital.model.TableMargin;
import com.aadhil.cineworlddigital.service.ActivityNavigator;

public class SelectSeat extends Fragment {
    public SelectSeat() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_select_seat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View fragment, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(fragment, savedInstanceState);

        // Get activity navigator
        ActivityNavigator navigator = ActivityNavigator.getNavigator(getContext(),
                getActivity().findViewById(R.id.parentLayoutCheckout));

        // Load seats
        loadSeatView(fragment);

        Button buttonNext = fragment.findViewById(R.id.button19);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigator.setRedirection(new ActivityNavigator.NavigationManager() {
                    @Override
                    public void redirect() {
                        CheckoutActivity activity = (CheckoutActivity) getActivity();
                        activity.getCheckoutFragmentManager()
                                .setFragment(CheckoutActivity.CheckoutFragmentManager.FRAGMENT_CONFIRM_CHECKOUT);
                    }
                });
            }
        });

        Button buttonPrev = fragment.findViewById(R.id.button20);
        buttonPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigator.setRedirection(new ActivityNavigator.NavigationManager() {
                    @Override
                    public void redirect() {
                        CheckoutActivity activity = (CheckoutActivity) getActivity();
                        activity.getCheckoutFragmentManager()
                                .setFragment(CheckoutActivity.CheckoutFragmentManager.FRAGMENT_SELECT_MOVIE);
                    }
                });
            }
        });
    }

    private void loadSeatView(View fragment) {
        TableLayout tableLayout = fragment.findViewById(R.id.tableLayout);

        int rows = 10;
        int seatsPerRow = 20;
        char[] rowLetter = {'Z','A','B','C','D','E','F','G','H','I','J','K','L'};

        for (int row = 1; row <= rows; row++) {
            TableRow tableRow = new TableRow(getContext());
            TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT
            );
            tableRow.setLayoutParams(tableRowParams);

            for (int seat = 1; seat <= seatsPerRow; seat++) {
                String seatId = rowLetter[row] + String.valueOf(seat);

                Button seatButton = getSeatButton();
                seatButton.setTag(seatId);
                seatButton.setText(seatId);

                TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);

                TableMargin tableMargin = getMargins(row, seat);
                layoutParams.setMargins(
                        tableMargin.getLeft(),
                        tableMargin.getTop(),
                        tableMargin.getRight(),
                        tableMargin.getBottom()
                );

                seatButton.setLayoutParams(layoutParams);

                seatButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String selectedSeatId = (String) view.getTag();
                        System.out.println(selectedSeatId);
                    }
                });

                tableRow.addView(seatButton);
            }

            tableLayout.addView(tableRow);
        }
    }

    private Button getSeatButton() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        ConstraintLayout constraintLayout = (ConstraintLayout) inflater.inflate(R.layout.seat_button, null);
        Button button = constraintLayout.findViewById(R.id.button9);
        ((ViewGroup) button.getParent()).removeView(button);
        return button;
    }

    private TableMargin getMargins(int row, int seat) {
        TableMargin tableMargin = new TableMargin(8, 5, 8, 5);

        if(row == 5) {
            tableMargin.setBottom(15);
        } else if(row == 6) {
            tableMargin.setTop(15);
        }

        if(seat == 10) {
            tableMargin.setRight(35);
        } else if(seat == 11) {
            tableMargin.setLeft(35);
        }
        return tableMargin;
    }
}