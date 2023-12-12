package com.aadhil.cineworlddigital.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.aadhil.cineworlddigital.CheckoutActivity;
import com.aadhil.cineworlddigital.MainActivity;
import com.aadhil.cineworlddigital.PaymentActivity;
import com.aadhil.cineworlddigital.R;
import com.aadhil.cineworlddigital.model.CheckoutInfo;
import com.aadhil.cineworlddigital.service.ActivityNavigator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConfirmCheckout extends Fragment {
    public ConfirmCheckout() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_confirm_checkout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View fragment, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(fragment, savedInstanceState);
        // Load checkout finalized data
        loadDetails(fragment);

        // Get activity navigator
        ActivityNavigator navigator = ActivityNavigator.getNavigator(getContext(),
                getActivity().findViewById(R.id.parentLayoutCheckout));

        // Click next to go to make payment
        Button buttonNext = fragment.findViewById(R.id.button19);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox checkBox1 = fragment.findViewById(R.id.checkBox);
                CheckBox checkBox2 = fragment.findViewById(R.id.checkBox2);

                if(!checkBox1.isChecked() || !checkBox2.isChecked()) {
                    Toast.makeText(getContext(), "Read and accept terms!", Toast.LENGTH_SHORT).show();
                } else {
                    navigator.setRedirection(new ActivityNavigator.NavigationManager() {
                        @Override
                        public void redirect() {
                            Intent i = new Intent(getContext(), PaymentActivity.class);
                            startActivity(i);
                        }
                    });
                }
            }
        });

        // Click back to go to select seat
        Button buttonPrev = fragment.findViewById(R.id.button20);
        buttonPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigator.setRedirection(new ActivityNavigator.NavigationManager() {
                    @Override
                    public void redirect() {
                        CheckoutActivity activity = (CheckoutActivity) getActivity();
                        activity.getCheckoutFragmentManager()
                                .setFragment(CheckoutActivity.CheckoutFragmentManager.FRAGMENT_SELECT_SEAT);
                    }
                });
            }
        });
    }

    private void loadDetails(View fragment) {
        final int ticketPrice = 1200;

        CheckoutInfo checkoutInfo = ((CheckoutActivity) getActivity()).getCheckoutInfo();
        String movieName = checkoutInfo.getMovieName();
        String showDateTime = "N/A";
        String userMobile = MainActivity.currentUser.getMobile();
        String userEmail = (MainActivity.currentUser.getEmail() == null
                || MainActivity.currentUser.getEmail().isEmpty())
                ? "N/A" : MainActivity.currentUser.getEmail();
        int ticketCount = checkoutInfo.getSelectedSeats().size();
        String selectedSeats = (ticketCount != 0) ? "" : "N/A";
        String subTotal = ticketCount + " x (LKR " + ticketPrice + ") = LKR " + (ticketCount*ticketPrice);
        String total = "LKR " + (ticketCount*ticketPrice);

        // Set date and time
        try {
            SimpleDateFormat sdfIn = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdfOut = new SimpleDateFormat("E, dd MMM");

            Date date = sdfIn.parse(checkoutInfo.getDate());
            String showDate = sdfOut.format(date);
            String showTime = checkoutInfo.getShowTime();
            showDateTime = showDate + " at " + showTime;
        } catch (ParseException ex) {
            ex.printStackTrace();
        }

        // Set selected seats
        for(String seat : checkoutInfo.getSelectedSeats()) {
            selectedSeats = selectedSeats + seat + ", ";
        }
        selectedSeats.substring(0, (selectedSeats.length()-2));

        TextView tvMovie = fragment.findViewById(R.id.textView46);
        tvMovie.setText(movieName);

        TextView tvDataAndTime = fragment.findViewById(R.id.textView48);
        tvDataAndTime.setText(showDateTime);

        TextView tvMobile = fragment.findViewById(R.id.textView51);
        tvMobile.setText(userMobile);

        TextView tvEmail = fragment.findViewById(R.id.textView53);
        tvEmail.setText(userEmail);

        TextView tvTcCount = fragment.findViewById(R.id.textView56);
        tvTcCount.setText(String.valueOf(ticketCount));

        TextView tvSeats = fragment.findViewById(R.id.textView58);
        tvSeats.setText(selectedSeats);

        TextView tvSub = fragment.findViewById(R.id.textView60);
        tvSub.setText(subTotal);

        TextView tvTotal = fragment.findViewById(R.id.textView62);
        tvTotal.setText(total);
    }
}