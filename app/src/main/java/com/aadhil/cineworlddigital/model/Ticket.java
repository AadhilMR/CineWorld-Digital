package com.aadhil.cineworlddigital.model;

public class Ticket {
    private String movieName;
    private String seatNo;
    private String showDate;
    private String showTime;
    private String price;
    private String issuedDateTime;
    private String refNo;

    public Ticket() {}

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(String seatNo) {
        this.seatNo = seatNo;
    }

    public String getShowDate() {
        return showDate;
    }

    public void setShowDate(String showDate) {
        this.showDate = showDate;
    }

    public String getShowTime() {
        return showTime;
    }

    public void setShowTime(String showTime) {
        this.showTime = showTime;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getIssuedDateTime() {
        return issuedDateTime;
    }

    public void setIssuedDateTime(String issuedDateTime) {
        this.issuedDateTime = issuedDateTime;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }
}
