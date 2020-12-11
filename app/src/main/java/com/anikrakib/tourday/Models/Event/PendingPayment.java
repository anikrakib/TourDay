package com.anikrakib.tourday.Models.Event;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PendingPayment {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("tr")
    @Expose
    private String tr;
    @SerializedName("method")
    @Expose
    private String method;
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("event")
    @Expose
    private Integer event;
    @SerializedName("user")
    @Expose
    private Integer user;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTr() {
        return tr;
    }

    public void setTr(String tr) {
        this.tr = tr;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Integer getEvent() {
        return event;
    }

    public void setEvent(Integer event) {
        this.event = event;
    }

    public Integer getUser() {
        return user;
    }

    public void setUser(Integer user) {
        this.user = user;
    }
}
