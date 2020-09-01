package com.anikrakib.tourday.Adapter;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.anikrakib.tourday.Fragment.About;
import com.anikrakib.tourday.Fragment.EditProfile;
import com.anikrakib.tourday.Fragment.Post;


public class ViewProfilePagerAdapter extends FragmentPagerAdapter {

    public ViewProfilePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new About();
            case 1:
                return new EditProfile();
            case 2:
                return new Post();
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
                return "About";
            case 1:
                return "Edit Profile";
            case 2:
                return "Post";
            default:
                return null;
        }
    }
}