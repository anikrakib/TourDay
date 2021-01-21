package com.anikrakib.tourday.Activity.Blog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.anikrakib.tourday.Activity.Profile.MyProfileActivity;
import com.anikrakib.tourday.Activity.Profile.OthersUserProfile;
import com.anikrakib.tourday.R;
import com.anikrakib.tourday.Utils.Loader;
import com.anikrakib.tourday.Utils.Share;
import com.anikrakib.tourday.WebService.RetrofitClient;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;
import com.tylersuehr.socialtextview.SocialTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BlogDetailsActivity extends AppCompatActivity {

    /* Initialize variable */
    Intent intent;
    KenBurnsView blogImageKenBurnsView;
    TextView blogDetailsTitleTextView,blogDetailsDescriptionTextView,blogDetailsDivisionTextView,blogDetailsDateTextView,authorFullName,authorBio,authorFaceBookLink,authorInstagramLink,authorBangladeshLink;
    SocialTextView blogAuthorName;
    ImageButton blogDetailsBackButton,shareBlog;
    ImageView authorFacebookProfile,authorInstagramProfile,authorBdProfile;
    public String AuthorUserName;
    CircleImageView authorImage;
    Dialog myDialog,blogLoader;
    ConstraintLayout constraintLayout;

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
        authorFullName = findViewById(R.id.authorFullName);
        authorBio = findViewById(R.id.authorBio);
        authorFaceBookLink = findViewById(R.id.authorFacebookLinkTextView);
        authorInstagramLink = findViewById(R.id.authorInstagramLinkTextView);
        authorBangladeshLink = findViewById(R.id.authorBangladeshTextView);
        authorImage = findViewById(R.id.authorImage);
        authorFacebookProfile = findViewById(R.id.authorFacebookLinkImageView);
        authorInstagramProfile = findViewById(R.id.authorInstagramLinkImageView);
        authorBdProfile = findViewById(R.id.authorBangladeshImageView);
        constraintLayout = findViewById(R.id.constraintLayout);
        shareBlog = findViewById(R.id.shareEventImageButton);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        if (Build.VERSION.SDK_INT >= 23) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        myDialog = new Dialog(this);
        intent = getIntent();
        Bundle extras = intent.getExtras();
        assert extras != null;
        int blogId = extras.getInt("blogId");

        Loader.start(BlogDetailsActivity.this);

        /* get post details from Api using blogId */
        getPostDetails(blogId);


        /* On click Listener */
        blogDetailsBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        authorBdProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBdMap(AuthorUserName);
            }
        });

        authorFacebookProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(authorFaceBookLink.getText().toString().isEmpty()){
                    Snackbar snackbar = Snackbar
                            .make(constraintLayout, AuthorUserName+" has no Facebook Account!!", Snackbar.LENGTH_LONG)
                            .setAction("Ok", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                }
                            });
                    snackbar.setActionTextColor(Color.GREEN);
                    snackbar.show();

                }else{
                    if (isFacebookAppInstalled()) {
                        Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                        String facebookUrl = getFacebookPageURL(getApplicationContext());
                        facebookIntent.setData(Uri.parse(facebookUrl));
                        startActivity(facebookIntent);

                    } else {
                        Snackbar snackbar = Snackbar
                                .make(constraintLayout, "Facebook not installed!!", Snackbar.LENGTH_LONG)
                                .setAction("Download", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                    }
                                });
                        snackbar.setActionTextColor(Color.MAGENTA);
                        snackbar.show();
                        showUserSocialMediaAccount("https://www.facebook.com/"+authorFaceBookLink.getText().toString());
                    }
                }
            }
        });

        authorInstagramProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(authorInstagramLink.getText().toString().isEmpty()){
                    Snackbar snackbar = Snackbar
                            .make(constraintLayout, AuthorUserName+" has no Instagram Account!!", Snackbar.LENGTH_LONG)
                            .setAction("Ok", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                }
                            });
                    snackbar.setActionTextColor(Color.GREEN);
                    snackbar.show();

                }else {
                    if (isInstagramInstalled()) {
                        Intent instagramIntent = new Intent(Intent.ACTION_VIEW);
                        String facebookUrl = getInstragamPageURL(getApplicationContext());
                        instagramIntent.setData(Uri.parse(facebookUrl));
                        startActivity(instagramIntent);

                    } else {
                        Snackbar snackbar = Snackbar
                                .make(constraintLayout, "Instagram not installed!!", Snackbar.LENGTH_LONG)
                                .setAction("Download", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                    }
                                });
                        snackbar.setActionTextColor(Color.MAGENTA);
                        snackbar.show();
                        showUserSocialMediaAccount("https://www.instagram.com/" + authorInstagramLink.getText().toString());
                    }
                }

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

        shareBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Share.shareLink(getApplicationContext(),"event/"+blogId);
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
                        showAuthorData(slug);

                        //blogLoader.dismiss();

                        blogDetailsDescriptionTextView.setText(Html.fromHtml(description, Html.FROM_HTML_MODE_COMPACT));

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }else{
                    Toast.makeText(getApplicationContext(),postId+" Try Again",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Check Internet !!",Toast.LENGTH_LONG).show();
                Loader.off();
            }
        });
    }

    public void showAuthorData(String userName){

        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .otherUserProfileInformation(userName);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.body().string());
                        JSONObject profile = jsonObject.getJSONObject("profile");
                        authorFullName.setText(profile.getString("name"));
                        authorFaceBookLink.setText(profile.getString("fb"));
                        authorInstagramLink.setText(profile.getString("insta"));
                        authorBio.setText(profile.getString("bio"));
                        Picasso.get().load("https://www.tourday.team"+profile.getString("picture")).into(authorImage);

                        Loader.off();

                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"Token Not Correct",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Check Internet !!",Toast.LENGTH_LONG).show();
                Loader.off();
            }
        });
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void showBdMap(String userName) {
        ImageView close;
        myDialog.setContentView(R.layout.custom_bd_map_pop_up);
        close = myDialog.findViewById(R.id.socialMediaClose);

        final WebView webView = myDialog.findViewById(R.id.webViewSocialMedia);


        WebSettings webSettings = webView.getSettings();
        webView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        WebViewClient webViewClient = new WebViewClient();
        webView.setWebViewClient(webViewClient);

        webView.loadUrl("https://www.tourday.team/api/map/"+userName);
        // set image scale to fit screen if larger than screen width
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) BlogDetailsActivity.this.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(displayMetrics);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.setCancelable(false);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void showUserSocialMediaAccount(String url) {
        ImageView close;
        myDialog.setContentView(R.layout.custom_bd_map_pop_up);
        close = myDialog.findViewById(R.id.socialMediaClose);

        final WebView webView = myDialog.findViewById(R.id.webViewSocialMedia);

        WebSettings webSettings = webView.getSettings();
        webView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        WebViewClient webViewClient = new WebViewClient();
        webView.setWebViewClient(webViewClient);

        webView.loadUrl(url);
        // set image scale to fit screen if larger than screen width
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) BlogDetailsActivity.this.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(displayMetrics);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.setCancelable(false);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    public String getFacebookPageURL(Context context) {
        String fbUsername = authorFaceBookLink.getText().toString();
        String FACEBOOK_URL = "https://www.facebook.com/"+fbUsername;
        String FACEBOOK_PAGE_ID = "YourPageName";
        PackageManager packageManager = context.getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.orca", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                return "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else { //older versions of fb app
                return "fb://page/" + FACEBOOK_PAGE_ID;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return FACEBOOK_URL; //normal web url
        }
    }
    public String getInstragamPageURL(Context context) {
        String INSTAGRAM_URL = "https://www.instagram.com/"+authorInstagramLink.getText().toString();
        Uri uri = Uri.parse("http://instagram.com/_u/"+authorInstagramLink.getText().toString());
        Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

        likeIng.setPackage("com.instagram.android");

        try {
            startActivity(likeIng);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(INSTAGRAM_URL)));
        }
        return INSTAGRAM_URL;
    }

    public boolean isFacebookAppInstalled() {
        try {
            getApplicationContext().getPackageManager().getApplicationInfo("com.facebook.katana", 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
    public boolean isInstagramInstalled() {
        try {
            getApplicationContext().getPackageManager().getApplicationInfo("com.instagram.android", 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams winParams = window.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        window.setAttributes(winParams);
    }
}