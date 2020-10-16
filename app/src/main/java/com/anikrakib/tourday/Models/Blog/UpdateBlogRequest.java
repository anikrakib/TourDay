package com.anikrakib.tourday.Models.Blog;

import com.google.gson.annotations.SerializedName;

import okhttp3.MultipartBody;

public class UpdateBlogRequest {
    @SerializedName("image")
    private MultipartBody.Part blogImageUrl;
    @SerializedName("title")
    private String blogTitle;
    @SerializedName("description")
    private String blogDescription;
    @SerializedName("division")
    private String blogDivision;

    public UpdateBlogRequest(MultipartBody.Part blogImageUrl, String blogTitle, String blogDescription, String blogDivision) {
        this.blogImageUrl = blogImageUrl;
        this.blogTitle = blogTitle;
        this.blogDescription = blogDescription;
        this.blogDivision = blogDivision;
    }

    public MultipartBody.Part getBlogImageUrl() {
        return blogImageUrl;
    }

    public String getBlogTitle() {
        return blogTitle;
    }

    public String getBlogDescription() {
        return blogDescription;
    }

    public String getBlogDivision() {
        return blogDivision;
    }

    public void setBlogImageUrl(MultipartBody.Part blogImageUrl) {
        this.blogImageUrl = blogImageUrl;
    }

    public void setBlogTitle(String blogTitle) {
        this.blogTitle = blogTitle;
    }

    public void setBlogDescription(String blogDescription) {
        this.blogDescription = blogDescription;
    }

    public void setBlogDivision(String blogDivision) {
        this.blogDivision = blogDivision;
    }
}
