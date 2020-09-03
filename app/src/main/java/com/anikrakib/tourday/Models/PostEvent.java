package com.anikrakib.tourday.Models;

public class PostEvent {

    public String Title;
    public String Body;
    public String startDate;
    public String endDate;
    public String startTime;
    public String endTime;
    public String location;
    public boolean like ;
    public int imageUrl ;


    public PostEvent() {
        // Default constructor required
    }

    public PostEvent(String title, String body, String startDate, String endDate, String startTime, String endTime, String location, boolean like, int imageUrl) {
        Title = title;
        Body = body;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.like = like;
        this.imageUrl = imageUrl;
    }

}

