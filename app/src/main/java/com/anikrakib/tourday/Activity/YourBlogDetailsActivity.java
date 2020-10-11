package com.anikrakib.tourday.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.anikrakib.tourday.Models.DeleteBlogResponse;
import com.anikrakib.tourday.R;
import com.anikrakib.tourday.WebService.RetrofitClient;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.marozzi.roundbutton.RoundButton;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;
import com.squareup.picasso.Picasso;
import com.tylersuehr.socialtextview.SocialTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.richeditor.RichEditor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class YourBlogDetailsActivity extends AppCompatActivity {
    ImageButton yourBlogDetailsBackButton;
    Intent intent;
    KenBurnsView yourBlogImageKenBurnsView;
    TextView yourBlogDetailsTitleTextView,yourBlogDetailsDescriptionTextView,yourBlogDetailsDivisionTextView,yourBlogDetailsDateTextView;
    FloatingActionButton editBlog,deleteBlog;
    Dialog myDialog,previewDialog;
    Resources resources;
    String[] division;
    InputStream blogPostInputStream;
    EditText blogPopUpTitle;
    ImageView blogImageView;
    private static final int INTENT_REQUEST_CODE = 100;
    String yourBlogTitle,yourBlogImage,yourBlogDescription,yourBlogDivision,yourBlogDate,yourBlogId;



    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_blog_details);

        yourBlogImageKenBurnsView = findViewById(R.id.yourBlogDetailsImage);
        yourBlogDetailsTitleTextView = findViewById(R.id.yourBlogDetailsTitleTextView);
        yourBlogDetailsDivisionTextView = findViewById(R.id.yourBlogDetailsLocationTextView);
        yourBlogDetailsDescriptionTextView = findViewById(R.id.yourBlogDetailsDescriptionTextView);
        yourBlogDetailsDateTextView = findViewById(R.id.yourBlogDetailsDate);
        yourBlogDetailsBackButton = findViewById(R.id.backButtonBlogDetails);
        editBlog = findViewById(R.id.yourBlogEditFloatingActionButton);
        deleteBlog = findViewById(R.id.yourBlogDeleteFloatingActionButton);


        intent = getIntent();
        Bundle extras = intent.getExtras();
        resources= getResources();
        division = resources.getStringArray(R.array.bdDivision);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        myDialog = new Dialog(this);
        previewDialog = new Dialog(this);


        assert extras != null;
        yourBlogTitle = extras.getString("yourBlogTitle");
        yourBlogImage = extras.getString("yourBlogImage");
        yourBlogDescription = extras.getString("yourBlogDescription");
        yourBlogDivision = extras.getString("yourBlogDivision");
        yourBlogDate = extras.getString("yourBlogDate");
        yourBlogId = extras.getString("yourBlogId");


        Picasso.get().load("https://tourday.team"+yourBlogImage).into(yourBlogImageKenBurnsView);
        yourBlogDetailsTitleTextView.setText(yourBlogTitle);
        yourBlogDetailsDivisionTextView.setText(yourBlogDivision);
        yourBlogDetailsDateTextView.setText(yourBlogDate);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            yourBlogDetailsDescriptionTextView.setText(Html.fromHtml(yourBlogDescription, Html.FROM_HTML_MODE_COMPACT));
        } else {
            yourBlogDetailsDescriptionTextView.setText(Html.fromHtml(yourBlogDescription));
        }


        // on click listener
        yourBlogDetailsBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        deleteBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteBlog(yourBlogId);
                finish();
            }
        });
        editBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
    public void deleteBlog(String getId){
        SharedPreferences userPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        String token = userPref.getString("token","");

        Call<DeleteBlogResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .deleteBlog("Token "+token,getId);

        call.enqueue(new Callback<DeleteBlogResponse>() {
            @Override
            public void onResponse(Call<DeleteBlogResponse> call, Response<DeleteBlogResponse> response) {
                if(response.isSuccessful()){
                    String success = response.body().getSuccess();
                    DynamicToast.makeSuccess(getApplicationContext(), success).show();

                }else{
                    DynamicToast.makeError(getApplicationContext(), "Something Wrong !!").show();
                }
            }

            @Override
            public void onFailure(Call<DeleteBlogResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


}