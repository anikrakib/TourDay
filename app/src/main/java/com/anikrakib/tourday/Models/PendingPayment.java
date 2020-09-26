package com.anikrakib.tourday.Models;

public class PendingPayment {
    private String imageUrl;
    private String userName;
    private String email;

    public String getImageUrl() {
        return imageUrl;
    }

    public PendingPayment(String userName, String email) {
        this.userName = userName;
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }
}
