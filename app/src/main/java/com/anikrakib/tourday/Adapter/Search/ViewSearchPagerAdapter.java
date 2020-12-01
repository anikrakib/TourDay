package com.anikrakib.tourday.Adapter.Search;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.anikrakib.tourday.Fragment.Event.Event;
import com.anikrakib.tourday.Fragment.Event.YourEvent;
import com.anikrakib.tourday.Fragment.Search.BlogSearchAll;
import com.anikrakib.tourday.Fragment.Search.ProductSearchAll;
import com.anikrakib.tourday.Fragment.Search.UserSearchAll;

public class ViewSearchPagerAdapter extends FragmentPagerAdapter {

    public ViewSearchPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new UserSearchAll();
            case 1:
                return new BlogSearchAll();
            case 2:
                return new ProductSearchAll();
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
                return "People";
            case 1:
                return "Blog";
            case 2:
                return "Product";
            default:
                return null;
        }
    }
}