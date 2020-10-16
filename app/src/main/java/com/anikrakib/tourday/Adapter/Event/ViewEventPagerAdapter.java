package com.anikrakib.tourday.Adapter.Event;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.anikrakib.tourday.Fragment.Event.YourEvent;
import com.anikrakib.tourday.Fragment.Event.Event;


public class ViewEventPagerAdapter extends FragmentPagerAdapter {

    public ViewEventPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new Event();
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
                return "Event";
            case 1:
                return "Your Event";
            default:
                return null;
        }
    }
}