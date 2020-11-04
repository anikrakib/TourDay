package com.anikrakib.tourday.Adapter.Profile;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.anikrakib.tourday.Fragment.Profile.About;
import com.anikrakib.tourday.Fragment.Profile.Photos;
import com.anikrakib.tourday.Fragment.Profile.Post;


public class ViewProfilePagerAdapter extends FragmentPagerAdapter {

    public ViewProfilePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new Post();
            case 1:
                return new Photos();
            case 2:
                return new About();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Post";
            case 1:
                return "Photo";
            case 2:
                return "About";
            default:
                return null;
        }
    }
}