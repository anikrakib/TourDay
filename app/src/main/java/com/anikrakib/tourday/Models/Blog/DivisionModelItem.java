package com.anikrakib.tourday.Models.Blog;

public class DivisionModelItem {
    int imageUrl;
    String districtName;

    public int getImageUrl() {
        return imageUrl;
    }

    public String getDistrictName() {
        return districtName;
    }

    public DivisionModelItem(int imageUrl, String districtName) {
        this.imageUrl = imageUrl;
        this.districtName = districtName;
    }
}
