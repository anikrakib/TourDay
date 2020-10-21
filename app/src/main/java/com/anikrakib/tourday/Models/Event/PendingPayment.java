package com.anikrakib.tourday.Models.Event;

public class PendingPayment {
    private String imageUrl;
    private String userName;
    private String email;
    private int id;

    public PendingPayment(String imageUrl, String userName, String email) {
        this.imageUrl = imageUrl;
        this.userName = userName;
        this.email = email;
    }

    public PendingPayment(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }
}
