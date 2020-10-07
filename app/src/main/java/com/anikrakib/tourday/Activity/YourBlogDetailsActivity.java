package com.anikrakib.tourday.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.anikrakib.tourday.R;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.squareup.picasso.Picasso;
import com.tylersuehr.socialtextview.SocialTextView;

public class YourBlogDetailsActivity extends AppCompatActivity {
    ImageButton yourBlogDetailsBackButton;
    Intent intent;
    KenBurnsView yourBlogImageKenBurnsView;
    TextView yourBlogDetailsTitleTextView,yourBlogDetailsDescriptionTextView,yourBlogDetailsDivisionTextView,yourBlogDetailsDateTextView;

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


        intent = getIntent();
        Bundle extras = intent.getExtras();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }


        assert extras != null;
        String yourBlogTitle = extras.getString("yourBlogTitle");
        String yourBlogImage = extras.getString("yourBlogImage");
        String yourBlogDescription = extras.getString("yourBlogDescription");
        String yourBlogDivision = extras.getString("yourBlogDivision");
        String yourBlogDate = extras.getString("yourBlogDate");
        int yourBlogId = extras.getInt("yourBlogId");

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

    }
}