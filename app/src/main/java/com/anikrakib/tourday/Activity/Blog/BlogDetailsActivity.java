package com.anikrakib.tourday.Activity.Blog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.anikrakib.tourday.Activity.Profile.MyProfileActivity;
import com.anikrakib.tourday.Activity.Profile.OthersUserProfile;
import com.anikrakib.tourday.R;
import com.anikrakib.tourday.WebService.RetrofitClient;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.squareup.picasso.Picasso;
import com.tylersuehr.socialtextview.SocialTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BlogDetailsActivity extends AppCompatActivity {

    /* Initialize variable */
    Intent intent;
    KenBurnsView blogImageKenBurnsView;
    TextView blogDetailsTitleTextView,blogDetailsDescriptionTextView,blogDetailsDivisionTextView,blogDetailsDateTextView;
    SocialTextView blogAuthorName;
    ImageButton blogDetailsBackButton;
    public String AuthorUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_details);

        /* Initialize view */
        blogImageKenBurnsView = findViewById(R.id.blogDetailsImage);
        blogDetailsTitleTextView = findViewById(R.id.blogDetailsTitleTextView);
        blogAuthorName = findViewById(R.id.authorUserName);
        blogDetailsDivisionTextView = findViewById(R.id.blogDetailsLocationTextView);
        blogDetailsDescriptionTextView = findViewById(R.id.blogDetailsDescriptionTextView);
        blogDetailsDateTextView = findViewById(R.id.blogDetailsDate);
        blogDetailsBackButton = findViewById(R.id.backButtonBlogDetails);


        intent = getIntent();
        Bundle extras = intent.getExtras();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        assert extras != null;
        int blogId = extras.getInt("blogId");

        /* get post details from Api using blogId */

        getPostDetails(blogId);


        /* On click Listener */
        blogDetailsBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        blogAuthorName.setOnLinkClickListener(new SocialTextView.OnLinkClickListener() {
            @Override
            public void onLinkClicked(int i, String s) {

                SharedPreferences userPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
                String currentUserName = userPref.getString("userName","");

                if(currentUserName.equals(AuthorUserName)){
                    startActivity(new Intent(BlogDetailsActivity.this, MyProfileActivity.class));
                } else {
                    Intent intent = new Intent(BlogDetailsActivity.this, OthersUserProfile.class);
                    intent.putExtra("userName",AuthorUserName);
                    startActivity(intent);
                }
            }
        });

    }

    /* This Method get specific Blog details from server using blogId
       API->(GET) https://www.tourday.team/api/blog/details/post_id
       Response:
           {
                "id": 1,
                "slug": "Author Name",
                "email": "asif.qubit@gmail.com",
                "title": "Post title",
                "date": "2020-10-02",
                "description": "Post Description",
                "image": "/media/blog_pics/about-2.jpg",
                "division": "Rajshahi",
            }
     */
    public void getPostDetails(int postId){
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .getPostDetails(postId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    try {
                        JSONObject jsonObject =new JSONObject( response.body().string());

                        int id  = jsonObject.getInt("id");
                        String slug  = jsonObject.getString("slug");
                        AuthorUserName=slug;
                        String date  = jsonObject.getString("date");
                        String division  = jsonObject.getString("division");
                        String description  = jsonObject.getString("description");
                        String title  = jsonObject.getString("title");
                        String image  = jsonObject.getString("image");
                        Picasso.get().load("https://www.tourday.team/"+image).into(blogImageKenBurnsView);
                        blogAuthorName.setLinkText("@"+slug);
                        blogDetailsTitleTextView.setText(title);
                        blogDetailsDivisionTextView.setText(division);
                        blogDetailsDateTextView.setText(date);


                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            blogDetailsDescriptionTextView.setText(Html.fromHtml(description, Html.FROM_HTML_MODE_COMPACT));
                        } else {
                            blogDetailsDescriptionTextView.setText(Html.fromHtml(description));
                        }


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }else{
                    Toast.makeText(getApplicationContext(),postId+" Try Again",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Fail!",Toast.LENGTH_LONG).show();

            }
        });
    }
}