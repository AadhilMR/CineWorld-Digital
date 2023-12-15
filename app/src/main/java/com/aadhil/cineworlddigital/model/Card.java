package com.aadhil.cineworlddigital.model;

public class Card {
    private String nameOnCard;
    private String cardNumber;
    private String cvv;
    private String expireDate;

    public Card(String nameOnCard, String cardNumber, String cvv, String expireDate) {
        this.nameOnCard = nameOnCard;
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.expireDate = expireDate;
    }

    public String getNameOnCard() {
        return nameOnCard;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getCvv() {
        return cvv;
    }

    public String getExpireDate() {
        return expireDate;
    }
}
