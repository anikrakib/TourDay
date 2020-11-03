package com.anikrakib.tourday.Models.Event;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllEventResult {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("details")
    @Expose
    private String details;
    @SerializedName("pay1")
    @Expose
    private String pay1;
    @SerializedName("pay1_method")
    @Expose
    private String pay1Method;
    @SerializedName("pay2")
    @Expose
    private String pay2;
    @SerializedName("pay2_method")
    @Expose
    private String pay2Method;
    @SerializedName("capacity")
    @Expose
    private Integer capacity;
    @SerializedName("cost")
    @Expose
    private Integer cost;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("host")
    @Expose
    private Integer host;
    @SerializedName("going")
    @Expose
    private List<Integer> going = null;
    @SerializedName("pending")
    @Expose
    private List<Integer> pending = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getPay1() {
        return pay1;
    }

    public void setPay1(String pay1) {
        this.pay1 = pay1;
    }

    public String getPay1Method() {
        return pay1Method;
    }

    public void setPay1Method(String pay1Method) {
        this.pay1Method = pay1Method;
    }

    public String getPay2() {
        return pay2;
    }

    public void setPay2(String pay2) {
        this.pay2 = pay2;
    }

    public String getPay2Method() {
        return pay2Method;
    }

    public void setPay2Method(String pay2Method) {
        this.pay2Method = pay2Method;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getHost() {
        return host;
    }

    public void setHost(Integer host) {
        this.host = host;
    }

    public List<Integer> getGoing() {
        return going;
    }

    public void setGoing(List<Integer> going) {
        this.going = going;
    }

    public List<Integer> getPending() {
        return pending;
    }

    public void setPending(List<Integer> pending) {
        this.pending = pending;
    }


}
