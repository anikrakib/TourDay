package com.anikrakib.tourday.Models.Event;

import java.util.ArrayList;
import java.util.List;

public class YourEventModel {
    private int eventId;
    private String title;
    private String location;
    private String date;
    private String details;
    private String pay1;
    private String pay1Method;
    private String pay2;
    private String pay2Method;
    private String eventImageUrl;
    private int capacity;
    private int cost;
    private int hostId;
    private int totalGoing;
    private int totalPending;
    private ArrayList<Integer> totalPendingUserList;

    public YourEventModel(int eventId, String title, String location, String date, String details, String pay1, String pay1Method, String pay2, String pay2Method, String eventImageUrl, int capacity, int cost, int hostId, int totalGoing, int totalPending, ArrayList<Integer> totalPendingUserList) {
        this.eventId = eventId;
        this.title = title;
        this.location = location;
        this.date = date;
        this.details = details;
        this.pay1 = pay1;
        this.pay1Method = pay1Method;
        this.pay2 = pay2;
        this.pay2Method = pay2Method;
        this.eventImageUrl = eventImageUrl;
        this.capacity = capacity;
        this.cost = cost;
        this.hostId = hostId;
        this.totalGoing = totalGoing;
        this.totalPending = totalPending;
        this.totalPendingUserList = totalPendingUserList;
    }

    public ArrayList<Integer> getTotalPendingUserList() {
        return totalPendingUserList;
    }

    public int getEventId() {
        return eventId;
    }

    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return location;
    }

    public String getDate() {
        return date;
    }

    public String getDetails() {
        return details;
    }

    public String getPay1() {
        return pay1;
    }

    public String getPay1Method() {
        return pay1Method;
    }

    public String getPay2() {
        return pay2;
    }

    public String getPay2Method() {
        return pay2Method;
    }

    public String getEventImageUrl() {
        return eventImageUrl;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getCost() {
        return cost;
    }

    public int getHostId() {
        return hostId;
    }

    public int getTotalGoing() {
        return totalGoing;
    }

    public int getTotalPending() {
        return totalPending;
    }
}
