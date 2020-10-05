package com.anikrakib.tourday.Models;

public class DistrictModelItem {
    int imageUrl;
    String districtName;

    public int getImageUrl() {
        return imageUrl;
    }

    public String getDistrictName() {
        return districtName;
    }

    public DistrictModelItem(int imageUrl, String districtName) {
        this.imageUrl = imageUrl;
        this.districtName = districtName;
    }
}
