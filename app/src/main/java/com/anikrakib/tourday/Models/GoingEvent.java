package com.anikrakib.tourday.Models;

public class GoingEvent {
    private String categoryName;
    private  int image;


    public String getCategoryName() {
        return categoryName;
    }


    public GoingEvent(String categoryName) {
        this.categoryName = categoryName;
    }
}
