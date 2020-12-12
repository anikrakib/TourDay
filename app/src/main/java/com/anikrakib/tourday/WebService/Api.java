package com.anikrakib.tourday.WebService;


import com.anikrakib.tourday.Models.Blog.AllBlogResponse;
import com.anikrakib.tourday.Models.Blog.DeleteBlogResponse;
import com.anikrakib.tourday.Models.Event.AllEventResponse;
import com.anikrakib.tourday.Models.Event.AllEventResult;
import com.anikrakib.tourday.Models.Event.DeleteEventResponse;
import com.anikrakib.tourday.Models.Event.GoingUser;
import com.anikrakib.tourday.Models.Event.PendingPayment;
import com.anikrakib.tourday.Models.Shop.ProductResponse;
import com.anikrakib.tourday.Models.Profile.EventPayment;
import com.anikrakib.tourday.Models.Profile.Token;
import com.anikrakib.tourday.Models.Blog.UpdateBlogRequest;
import com.anikrakib.tourday.Models.Shop.SearchResponse;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
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
    Call<ResponseBody> userProfile(
            @Header("Authorization") String authToken);

    @FormUrlEncoded
    @POST("forget_password/")
    Call<ResponseBody> forgetPassword(
            @Field("username_email") String userOrEmail);

    @FormUrlEncoded
    @POST("reset_password/{username}/")
    Call<ResponseBody> resetPassword(
            @Path("username") String slug,
            @Field("code") String code,
            @Field("password1") String newPassword,
            @Field("password2") String confirmNewPassword);

    @POST("delete_account/")
    Call<ResponseBody> deleteAccount(
            @Header("Authorization") String authToken);

    @GET("user/{user_id}")
    Call<ResponseBody> getUserInfoByUserId(
            @Path("user_id") int userId);

    @GET("get_posts/{username}")
    Call<ResponseBody> getPhoto(
            @Path("username") String userName);

    @GET("get_posts/{username}")
    Call<ResponseBody> getPhotoNext(
            @Path("username") String userName,
            @Query("limit") String limit,
            @Query("offset") String offSet);

    @FormUrlEncoded
    @POST("profile/")
    Call<ResponseBody> updateProfileName(
            @Header("Authorization") String authToken,
            @Field("name") String name);

    @FormUrlEncoded
    @POST("profile/")
    Call<ResponseBody> updateEmail(
            @Header("Authorization") String authToken,
            @Field("email") String email);

    @FormUrlEncoded
    @POST("profile/")
    Call<ResponseBody> updateFacebookLink(
            @Header("Authorization") String authToken,
            @Field("fb") String facebookLink);

    @FormUrlEncoded
    @POST("profile/")
    Call<ResponseBody> updateInstagramLink(
            @Header("Authorization") String authToken,
            @Field("insta") String instagramLink);

    @FormUrlEncoded
    @POST("profile/")
    Call<ResponseBody> updateLocation(
            @Header("Authorization") String authToken,
            @Field("city") String location);

    @FormUrlEncoded
    @POST("profile/")
    Call<ResponseBody> updateBio(
            @Header("Authorization") String authToken,
            @Field("bio") String location);

    @Multipart
    @POST("profile/")
    Call<ResponseBody> updateImage(
            @Header("Authorization") String authToken,
            @Part MultipartBody.Part image);

    @Multipart
    @POST("post/")
    Call<ResponseBody> createPost(
            @Header("Authorization") String authToken,
            @Part("post") RequestBody postDescription,
            @Part("location") RequestBody postLocation,
            @Part("date") RequestBody postDate,
            @Part MultipartBody.Part postImage);

    @FormUrlEncoded
    @POST("post_delete/")
    Call<ResponseBody> deletePost(
            @Header("Authorization") String authToken,
            @Field("id") String postId);

    @FormUrlEncoded
    @POST("post/like/")
    Call<ResponseBody> selfLike(
            @Header("Authorization") String authToken,
            @Field("post_id") int postId);

    @GET("blog/division/{Division_Name}")
    Call<ResponseBody> getDivisionBlog(
            @Path("Division_Name") String divisionName);

    @GET("blog/allpost/")
    Call<AllBlogResponse> getAllBlogPost(
            @Query("page") int pageNumber);

    @Multipart
    @POST("blog/addpost/")
    Call<ResponseBody> createBlog(
            @Header("Authorization") String authToken,
            @Part("title") RequestBody blogTitle,
            @Part("description") RequestBody blogDescription,
            @Part MultipartBody.Part blogImage,
            @Part("division") RequestBody blogDivision);

    @DELETE("blog/delete/{post_id}")
    Call<DeleteBlogResponse> deleteBlog(
            @Header("Authorization") String authToken,
            @Path("post_id") String postId);

    @Multipart
    @PUT("blog/edit/{post_id}")
    Call<UpdateBlogRequest> updateBlog(
            @Header("Authorization") String authToken,
            @Path("post_id") String postId,
            @PartMap Map<String, RequestBody> map,
            @Part MultipartBody.Part image);

    @GET("blog/details/{post_id}")
    Call<ResponseBody> getPostDetails(@Path("post_id") int postId);

    @GET("blog/query?")
    Call<AllBlogResponse> getAllSearchBlog(
            @Query("search") String key,
            @Query("page") int page);

    @GET("user/{username}")
    Call<ResponseBody> otherUserProfileInformation(
            @Path("username") String userName);

    @GET("event/{event_id}")
    Call<AllEventResult> getEventDetails(
            @Path("event_id") int eventId);

    @GET("get_events/{username}")
    Call<ResponseBody> getYourEvent(
            @Header("Authorization") String authToken,
            @Path("username") String userName);

    @GET("all-events/")
    Call<AllEventResponse> getAllEvent(
            @Query("limit") int limit,
            @Query("offset") int offSet);

    @GET("going_users/{event_id}")
    Call<List<GoingUser>> getAllGoingUser(
            @Path("event_id") int userName);

    @GET("event/transactions/{event_id}")
    Call<List<PendingPayment>> getAllPendingUser(
            @Header("Authorization") String authToken,
            @Path("event_id") int userName);

    @GET("going-events/{username}")
    Call<AllEventResponse> getAllGoingEvent(
            @Path("username") String userName,
            @Query("limit") int limit,
            @Query("offset") int offSet);

    @FormUrlEncoded
    @POST("event-pay/{event_id}")
    Call<EventPayment> eventPayment(
            @Header("Authorization") String authToken,
            @Path("event_id") int postId,
            @Field("method") String paymentMethod,
            @Field("tr") String transactionID);

    @FormUrlEncoded
    @POST("create_event/")
    Call<ResponseBody> createEvent(
            @Header("Authorization") String authToken,
            @Field("title") String blogTitle,
            @Field("location") String blogLocation,
            @Field("date") String blogDate,
            @Field("details") String blogDetails,
            @Field("pay1_method") String pay1Method,
            @Field("pay1") String pay1,
            @Field("pay2_method") String pay2Method,
            @Field("pay2") String pay2,
            @Field("capacity") String blogCapacity,
            @Field("cost") String blogCost);

    @POST("event/delete/{event_id}")
    Call<DeleteEventResponse> deleteEvent(
            @Header("Authorization") String authToken,
            @Path("event_id") int eventId);

    @GET("search/user/{search_keyword}")
    Call<SearchResponse> getAllSearchUser(
            @Path("search_keyword") String searchKeyword,
            @Query("limit") int limit,
            @Query("offset") int offSet);

    @GET("search/event/{query}")
    Call<AllEventResponse> getAllSearchEvent(
            @Path("query") String searchKeyword,
            @Query("limit") int limit,
            @Query("offset") int offSet);

    @GET("search/product/{product_keyword}")
    Call<ProductResponse> getAllSearchProduct(
            @Path("product_keyword") String searchKeyword,
            @Query("limit") int limit,
            @Query("offset") int offSet);

    @FormUrlEncoded
    @POST("event/transactions/action/{event_id}")
    Call<ResponseBody> eventPaymentAction(
            @Path("event_id") Integer eventId,
            @Header("Authorization") String authToken,
            @Field("user_id") Integer blogTitle,
            @Field("tr_id") String transactionID,
            @Field("action") int action);

}
