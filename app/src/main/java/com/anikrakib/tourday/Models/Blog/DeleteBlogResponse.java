package com.anikrakib.tourday.Models.Blog;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeleteBlogResponse {
    @SerializedName("success")
    @Expose
    String success;

    public String getSuccess() {
        return success;
    }
}
