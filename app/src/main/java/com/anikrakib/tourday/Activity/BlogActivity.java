package com.anikrakib.tourday.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;

import com.anikrakib.tourday.Adapter.DistrictAdapter;
import com.anikrakib.tourday.Adapter.ViewBlogPagerAdapter;
import com.anikrakib.tourday.Adapter.ViewEventPagerAdapter;
import com.anikrakib.tourday.Models.DistrictModelItem;
import com.anikrakib.tourday.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class BlogActivity extends AppCompatActivity {

    ViewPager2 viewPager;
    DistrictAdapter adapter;
    List<DistrictModelItem> models;
    ViewBlogPagerAdapter viewBlogPagerAdapter;
    ViewPager viewPagerBlog;
    TabLayout tabLayoutBlog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);

        viewPager = findViewById(R.id.viewPager);


        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

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

        models.add(new DistrictModelItem(R.drawable.sylhet,"Sylhet"));
        models.add(new DistrictModelItem(R.drawable.dhaka,"Dhaka"));
        models.add(new DistrictModelItem(R.drawable.chattogram,"Chattogram"));
        models.add(new DistrictModelItem(R.drawable.rangpur,"Rangpur"));
        models.add(new DistrictModelItem(R.drawable.rajshahi,"Rajshahi"));
        models.add(new DistrictModelItem(R.drawable.mymensingh,"Mymensingh"));
        models.add(new DistrictModelItem(R.drawable.barishal,"Barishal"));
        models.add(new DistrictModelItem(R.drawable.khulna,"Khulna"));


        adapter = new DistrictAdapter(this,models);

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
    }
}