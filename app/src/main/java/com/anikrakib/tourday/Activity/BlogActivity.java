package com.anikrakib.tourday.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageButton;

import com.anikrakib.tourday.Adapter.DivisionAdapter;
import com.anikrakib.tourday.Adapter.ViewBlogPagerAdapter;
import com.anikrakib.tourday.Models.DivisionModelItem;
import com.anikrakib.tourday.R;
import com.fiberlink.maas360.android.richtexteditor.RichEditText;
import com.fiberlink.maas360.android.richtexteditor.RichTextActions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class BlogActivity extends AppCompatActivity {

    ViewPager2 viewPager;
    DivisionAdapter adapter;
    List<DivisionModelItem> models;
    ViewBlogPagerAdapter viewBlogPagerAdapter;
    ViewPager viewPagerBlog;
    TabLayout tabLayoutBlog;
    FloatingActionButton createBlog;
    Dialog myDialog;
    EditText blogPopUpTitle;
    RichEditText blogPopUpDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);

        viewPager = findViewById(R.id.viewPager);
        createBlog = findViewById(R.id.fabButtonCreateBlog);



        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        myDialog = new Dialog(this);



        /////*     initialize view   */////
        viewPagerBlog = (ViewPager) findViewById(R.id.viewPagerBlog);

        /////*     initialize ViewPager   */////
        viewBlogPagerAdapter = new ViewBlogPagerAdapter(getSupportFragmentManager());

        /////*     add adapter to ViewPager  */////
        viewPagerBlog.setAdapter(viewBlogPagerAdapter);
        tabLayoutBlog = (TabLayout) findViewById(R.id.slidingTabsBlog);
        tabLayoutBlog.setupWithViewPager(viewPagerBlog);
        tabLayoutBlog.setTabRippleColor(null);


        models = new ArrayList<>();

        models.add(new DivisionModelItem(R.drawable.sylhet,"Sylhet"));
        models.add(new DivisionModelItem(R.drawable.dhaka,"Dhaka"));
        models.add(new DivisionModelItem(R.drawable.chattogram,"Chattogram"));
        models.add(new DivisionModelItem(R.drawable.rangpur,"Rangpur"));
        models.add(new DivisionModelItem(R.drawable.rajshahi,"Rajshahi"));
        models.add(new DivisionModelItem(R.drawable.mymensingh,"Mymensingh"));
        models.add(new DivisionModelItem(R.drawable.barishal,"Barishal"));
        models.add(new DivisionModelItem(R.drawable.khulna,"Khulna"));


        adapter = new DivisionAdapter(this,models);

        viewPager.setAdapter(adapter);

        viewPager.setClipToPadding(false);
        viewPager.setClipChildren(false);
        viewPager.setOffscreenPageLimit(3);
        viewPager.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(10));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1- Math.abs(position);
                page.setScaleY(.80f + r * 0.20f);
            }
        });
        viewPager.setPageTransformer(compositePageTransformer);



        createBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createEventPopUp();


                if (0 != (getApplication().getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE)) {
                    WebView.setWebContentsDebuggingEnabled(true);
                }

            }
        });

    }


    private void createEventPopUp() {
        ImageButton postCloseButton;
        Animation top_to_bottom;
        final ConstraintLayout createEventLayout;
        final String[] blogTitleSave = new String[1];
        final String[] blogDescriptionSave = new String[1];
        RichTextActions richTextActions ;



        myDialog.setContentView(R.layout.create_blog);
        postCloseButton = myDialog.findViewById(R.id.createBlogCloseButton);

        createEventLayout = myDialog.findViewById(R.id.createBlogLayout);
        blogPopUpTitle = myDialog.findViewById(R.id.popupBlogTitle);
        blogPopUpDescription = myDialog.findViewById(R.id.popupBlogDescription);
        richTextActions = myDialog.findViewById(R.id.rich_text_actions);


        top_to_bottom = AnimationUtils.loadAnimation(this, R.anim.top_to_bottom);

        // Retrieve and set Event Title and Description from SharedPreferences when again open CreateEvent PopUp

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String eventTitle = sharedPreferences.getString("BlogTitle","");
        String eventDescription = sharedPreferences.getString("BlogDescription","");

//        //delete SharedPreference data
//        SharedPreferences preferences = getSharedPreferences("postTitle", 0);
//        preferences.edit().remove("postTitle").apply();

        blogPopUpTitle.setText(eventTitle);

        blogPopUpDescription.setRichTextActionsView(richTextActions);
        //blogPopUpDescription.setPreviewText(eventDescription);
        //blogPopUpDescription.setHint("Enter Blog Description");
        //blogPopUpDescription.setBackgroundColor(R.id.color_white);



        postCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save Event Title and Description in SharedPreferences when close CreateEvent PopUp

                blogTitleSave[0] = blogPopUpTitle.getText().toString();
                blogDescriptionSave[0] = blogPopUpDescription.getHtml();
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("BlogTitle", blogTitleSave[0]);
                editor.putString("BlogDescription", blogDescriptionSave[0]);
                editor.apply();

                myDialog.dismiss();
            }
        });

        createEventLayout.startAnimation(top_to_bottom);

        myDialog.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        myDialog.getWindow().getAttributes().gravity = Gravity.TOP;
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.setCancelable(false);
        myDialog.show();
    }

}