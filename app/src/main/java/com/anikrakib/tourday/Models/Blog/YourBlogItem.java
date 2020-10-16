package com.anikrakib.tourday.Models.Blog;

public class YourBlogItem {
    private String yourBlogImageUrl;
    private String yourBlogTitle;
    private String yourBlogDescription;
    private String yourBlogDivision;
    private String yourBlogDate;
    private String yourBlogAuthorName;
    private String yourBlogId;

    public YourBlogItem(String yourBlogImageUrl, String yourBlogTitle, String yourBlogDescription, String yourBlogDivision, String yourBlogDate, String yourBlogAuthorName, String yourBlogId) {
        this.yourBlogImageUrl = yourBlogImageUrl;
        this.yourBlogTitle = yourBlogTitle;
        this.yourBlogDescription = yourBlogDescription;
        this.yourBlogDivision = yourBlogDivision;
        this.yourBlogDate = yourBlogDate;
        this.yourBlogAuthorName = yourBlogAuthorName;
        this.yourBlogId = yourBlogId;
    }

    public String getYourBlogImageUrl() {
        return yourBlogImageUrl;
    }

    public String getYourBlogTitle() {
        return yourBlogTitle;
    }

    public String getYourBlogDescription() {
        return yourBlogDescription;
    }

    public String getYourBlogDivision() {
        return yourBlogDivision;
    }

    public String getYourBlogDate() {
        return yourBlogDate;
    }

    public String getYourBlogAuthorName() {
        return yourBlogAuthorName;
    }

    public String getYourBlogId() {
        return yourBlogId;
    }
}
