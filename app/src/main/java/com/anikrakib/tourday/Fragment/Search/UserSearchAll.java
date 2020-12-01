package com.anikrakib.tourday.Fragment.Search;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.anikrakib.tourday.Activity.SearchAllActivity;
import com.anikrakib.tourday.Adapter.Search.ViewSearchPagerAdapter;
import com.anikrakib.tourday.R;

public class UserSearchAll extends Fragment {
    TextView textView;
    SearchAllActivity searchAllActivity;

    public UserSearchAll() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_search_all, container, false);

        textView = view.findViewById(R.id.yesButton);

        //textView.setText(SearchAllActivity.keyWordText);

        return view;
    }
}