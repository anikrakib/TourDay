package com.anikrakib.tourday.Adapter;


import android.graphics.Color;
import android.provider.Settings;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.anikrakib.tourday.Fragment.About;
import com.anikrakib.tourday.Fragment.EditProfile;
import com.anikrakib.tourday.Fragment.Post;
import com.anikrakib.tourday.Fragment.viewEditFragment;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

/*
 *
 *for more visit www.materialuiux.com
 *
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
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