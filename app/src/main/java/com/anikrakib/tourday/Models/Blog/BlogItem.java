package com.anikrakib.tourday.Models.Blog;

public class BlogItem {
    private final String blogImageUrl;
    private final String blogTitle;
    private final String blogDescription;
    private final String blogDivision;
    private final String blogDate;
    private final String blogAuthorName;
    private final String blogId;
    private boolean search;

    public BlogItem(String blogImageUrl, String blogTitle, String blogDescription, String blogDivision, String blogDate, String blogAuthorName, String blogId) {
        this.blogImageUrl = blogImageUrl;
        this.blogTitle = blogTitle;
        this.blogDescription = blogDescription;
        this.blogDivision = blogDivision;
        this.blogDate = blogDate;
        this.blogAuthorName = blogAuthorName;
        this.blogId = blogId;
    }

    public boolean isSearch() {
        return search;
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
