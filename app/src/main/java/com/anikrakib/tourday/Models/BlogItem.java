package com.anikrakib.tourday.Models;

public class BlogItem {
    private String blogImageUrl;
    private String blogTitle;
    private String blogDescription;
    private String blogDivision;
    private String blogDate;
    private String blogAuthorName;
    private String blogId;

    public BlogItem(String blogImageUrl, String blogTitle, String blogDivision, String blogDate) {
        this.blogImageUrl = blogImageUrl;
        this.blogTitle = blogTitle;
        this.blogDivision = blogDivision;
        this.blogDate = blogDate;
    }

    public BlogItem(String blogImageUrl, String blogTitle, String blogDescription, String blogDivision, String blogDate, String blogAuthorName, String blogId) {
        this.blogImageUrl = blogImageUrl;
        this.blogTitle = blogTitle;
        this.blogDescription = blogDescription;
        this.blogDivision = blogDivision;
        this.blogDate = blogDate;
        this.blogAuthorName = blogAuthorName;
        this.blogId = blogId;
    }

    public String getBlogImageUrl() {
        return blogImageUrl;
    }

    public String getBlogTitle() {
        return blogTitle;
    }

    public String getBlogDivision() {
        return blogDivision;
    }

    public String getBlogAuthorName() {
        return blogAuthorName;
    }

    public String getBlogId() {
        return blogId;
    }

    public String getBlogLocation() {
        return blogDivision;
    }

    public String getBlogDescription() {
        return blogDescription;
    }

    public String getBlogDate() {
        return blogDate;
    }
}
