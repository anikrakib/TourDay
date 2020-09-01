package com.anikrakib.tourday.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.anikrakib.tourday.Fragment.YourEvent;
import com.anikrakib.tourday.Fragment.CreatePost;



public class ViewCreatePostAndEventPagerAdapter extends FragmentPagerAdapter {

    public ViewCreatePostAndEventPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new CreatePost();
            case 1:
                return new YourEvent();
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
                return "Create Post";
            case 1:
                return "Create Event";
            default:
                return null;
        }
    }
}