package com.anikrakib.tourday.Adapter.Blog;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.anikrakib.tourday.Fragment.Blog.Blog;
import com.anikrakib.tourday.Fragment.Blog.YourBlog;

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