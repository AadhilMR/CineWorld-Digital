package com.aadhil.cineworlddigital.model;

public class Invoice {
    private String transactionId;
    private String movieId;
    private String userId;
    private String datetime;
    private Double total;

    public Invoice(String transactionId, String movieId, String userId, String datetime, Double total) {
        this.transactionId = transactionId;
        this.movieId = movieId;
        this.userId = userId;
        this.datetime = datetime;
        this.total = total;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getMovieId() {
        return movieId;
    }

    public String getUserId() {
        return userId;
    }

    public String getDatetime() {
        return datetime;
    }

    public Double getTotal() {
        return total;
    }
}
