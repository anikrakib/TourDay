package com.anikrakib.tourday.Activity;

import androidx.appcompat.app.AppCompatActivity;

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

public class BlogDetailsActivity extends AppCompatActivity {
    Intent intent;
    KenBurnsView blogImageKenBurnsView;
    TextView blogDetailsTitleTextView,blogDetailsDescriptionTextView,blogDetailsDivisionTextView,blogDetailsDateTextView;
    SocialTextView blogAuthorName;
    ImageButton blogDetailsBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_details);

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
        String blogTitle = extras.getString("blogTitle");
        String blogImage = extras.getString("blogImage");
        String blogDescription = extras.getString("blogDescription");
        String division = extras.getString("division");
        String blogDate = extras.getString("blogDate");
        int blogId = extras.getInt("blogId");
        String blogAuthor = extras.getString("blogAuthor");

        Picasso.get().load("https://tourday.team"+blogImage).into(blogImageKenBurnsView);
        blogAuthorName.setLinkText("@"+blogAuthor);
        blogDetailsTitleTextView.setText(blogTitle);
        blogDetailsDivisionTextView.setText(division);
        blogDetailsDateTextView.setText(blogDate);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            blogDetailsDescriptionTextView.setText(Html.fromHtml(blogDescription, Html.FROM_HTML_MODE_COMPACT));
        } else {
            blogDetailsDescriptionTextView.setText(Html.fromHtml(blogDescription));
        }

        // on click listener
        blogDetailsBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}