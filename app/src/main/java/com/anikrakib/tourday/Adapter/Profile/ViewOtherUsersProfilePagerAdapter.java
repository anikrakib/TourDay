package com.anikrakib.tourday.Adapter.Profile;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.anikrakib.tourday.Fragment.Profile.OtherUsersGallery;
import com.anikrakib.tourday.Fragment.Profile.OtherUsersPost;


public class ViewOtherUsersProfilePagerAdapter extends FragmentPagerAdapter {

    public ViewOtherUsersProfilePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new OtherUsersPost();
            case 1:
                return new OtherUsersGallery();
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
                return "Post";
            case 1:
                return "Photos";
            default:
                return null;
        }
    }
}