package com.anikrakib.tourday.Models.Event;

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

    public PostEvent(String title, String body, String startDate, String endDate, String location,int imageUrl) {
        Title = title;
        Body = body;
        this.startDate = startDate;
        this.endDate = endDate;
        this.location = location;
        this.imageUrl = imageUrl;
    }

    public PostEvent(String title, String body, String startDate, String endDate, String location, boolean like, int imageUrl) {
        Title = title;
        Body = body;
        this.startDate = startDate;
        this.endDate = endDate;
        this.location = location;
        this.like = like;
        this.imageUrl = imageUrl;
    }

}

