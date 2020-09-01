package com.anikrakib.tourday.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anikrakib.tourday.Adapter.ViewCreatePostAndEventPagerAdapter;
import com.anikrakib.tourday.R;
import com.google.android.material.tabs.TabLayout;

public class Event extends Fragment {



    public Event() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event, container, false);

        return  view;
    }
}