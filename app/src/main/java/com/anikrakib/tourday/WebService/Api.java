package com.anikrakib.tourday.WebService;


import com.anikrakib.tourday.Models.BlogDetailsResponse;
import com.anikrakib.tourday.Models.DeleteBlogResponse;
import com.anikrakib.tourday.Models.Token;
import com.anikrakib.tourday.Models.UpdateBlogRequest;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {

    @FormUrlEncoded
    @POST("auth/signup/")
    Call<ResponseBody> createUser(
            @Field("username") String username,
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("auth/login/")
    Call<Token> logInUsingUserName(
            @Field("username") String username,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("auth/login/")
    Call<Token> logInUsingEmail(
            @Field("email") String email,
            @Field("password") String password
    );

    @GET("profile/")
    Call<ResponseBody> userProfile(@Header("Authorization") String authToken);

    @GET("get_posts/{username}")
    Call<ResponseBody> getPhoto(@Path("username") String userName);


    @FormUrlEncoded
    @POST("profile/")
    Call<ResponseBody> updateProfileName(@Header("Authorization") String authToken,
                                         @Field("name") String name);

    @FormUrlEncoded
    @POST("profile/")
    Call<ResponseBody> updateEmail(@Header("Authorization") String authToken,
                                   @Field("email") String email);

    @FormUrlEncoded
    @POST("profile/")
    Call<ResponseBody> updateLocation(@Header("Authorization") String authToken,
                                      @Field("city") String location);

    @FormUrlEncoded
    @POST("profile/")
    Call<ResponseBody> updateBio(@Header("Authorization") String authToken,
                                 @Field("bio") String location);

    @Multipart
    @POST("profile/")
    Call<ResponseBody> updateImage(@Header("Authorization") String authToken,
                                   @Part MultipartBody.Part image);

    @Multipart
    @POST("post/")
    Call<ResponseBody> createPost(@Header("Authorization") String authToken,
                                  @Part("post") RequestBody postDescription,
                                  @Part("location") RequestBody postLocation,
                                  @Part("date") RequestBody postDate,
                                  @Part MultipartBody.Part postImage);

    @FormUrlEncoded
    @POST("post_delete/")
    Call<ResponseBody> deletePost(@Header("Authorization") String authToken,
                                 @Field("id") String postId);

    @FormUrlEncoded
    @POST("post/like/")
    Call<ResponseBody> selfLike(@Header("Authorization") String authToken,
                                  @Field("post_id") String postId);

    @Multipart
    @POST("blog/addpost/")
    Call<ResponseBody> createBlog(@Header("Authorization") String authToken,
                                  @Part("title") RequestBody blogTitle,
                                  @Part("description") RequestBody blogDescription,
                                  @Part MultipartBody.Part blogImage,
                                  @Part("division") RequestBody blogDivision);

    @DELETE("blog/delete/{post_id}")
    Call<DeleteBlogResponse> deleteBlog(@Header("Authorization") String authToken,
                                        @Path("post_id") String postId);

    @Multipart
    @PUT("blog/edit/{post_id}")
    Call<UpdateBlogRequest> updateBlog(@Header("Authorization") String authToken,
                                       @Path("post_id") String postId, @PartMap Map<String, RequestBody> map, @Part MultipartBody.Part image);

    @GET("blog/details/{post_id}")
    Call<ResponseBody> getPostDetails(@Path("post_id") int postId);

    @GET("user/{username}")
    Call<ResponseBody> otherUserProfileInformation(@Header("Authorization") String authToken,@Path("username") String userName);
}
