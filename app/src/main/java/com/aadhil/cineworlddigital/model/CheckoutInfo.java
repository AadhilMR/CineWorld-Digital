package com.aadhil.cineworlddigital.model;

import java.io.Serializable;
import java.util.ArrayList;

public class CheckoutInfo implements Serializable {
    private String movieId;
    private String movieName;
    private String date;
    private String showTime;
    private ArrayList<String> seats;
    private ArrayList<String> selectedSeats;
    private Double price;

    public CheckoutInfo() {}

    public String getMovieId() {
        return movieId;
    }

    public CheckoutInfo setMovieId(String movieId) {
        this.movieId = movieId;
        return this;
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

    public Double getPrice() {
        return price;
    }

    public CheckoutInfo setPrice(Double price) {
        this.price = price;
        return this;
    }
}
