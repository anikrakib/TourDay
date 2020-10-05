package com.anikrakib.tourday.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.anikrakib.tourday.Fragment.Blog;
import com.anikrakib.tourday.Fragment.Event;
import com.anikrakib.tourday.Fragment.YourBlog;
import com.anikrakib.tourday.Fragment.YourEvent;

public class ViewBlogPagerAdapter extends FragmentPagerAdapter {

    public ViewBlogPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new Blog();
            case 1:
                return new YourBlog();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Blog";
            case 1:
                return "Your Blog";
            default:
                return null;
        }
    }
}