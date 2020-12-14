package com.anikrakib.tourday.Models.Shop;

public class CategoryListItem {
    int imageId;
    String categoryName;

    public CategoryListItem(int imageId, String categoryName) {
        this.imageId = imageId;
        this.categoryName = categoryName;
    }

    public int getImageId() {
        return imageId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
