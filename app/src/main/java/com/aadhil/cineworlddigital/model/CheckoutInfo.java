package com.aadhil.cineworlddigital.model;

import java.util.ArrayList;

public class CheckoutInfo {
    private String movieName;
    private String date;
    private String showTime;
    private ArrayList<String> seats;
    private ArrayList<String> selectedSeats;

    public CheckoutInfo() {}

    public CheckoutInfo(String movieName, String date, String showTime) {
        this.movieName = movieName;
        this.date = date;
        this.showTime = showTime;
    }

    public String getMovieName() {
        return movieName;
    }

    public CheckoutInfo setMovieName(String movieName) {
        this.movieName = movieName;
        return this;
    }

    public String getDate() {
        return date;
    }

    public CheckoutInfo setDate(String date) {
        this.date = date;
        return this;
    }

    public String getShowTime() {
        return showTime;
    }

    public CheckoutInfo setShowTime(String showTime) {
        this.showTime = showTime;
        return this;
    }

    public ArrayList<String> getSeats() {
        return seats;
    }

    public CheckoutInfo setSeats(ArrayList<String> seats) {
        this.seats = seats;
        return this;
    }

    public ArrayList<String> getSelectedSeats() {
        return selectedSeats;
    }

    public CheckoutInfo setSelectedSeats(ArrayList<String> selectedSeats) {
        this.selectedSeats = selectedSeats;
        return this;
    }
}
