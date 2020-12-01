package com.anikrakib.tourday.Models.Profile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EventPayment {
    @SerializedName("status")
    @Expose
    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
