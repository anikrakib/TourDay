package com.anikrakib.tourday.Fragment;

import android.app.Dialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anikrakib.tourday.R;


public class EditProfile extends Fragment {

    Dialog myDialog;
    TextView userEmailTextView,userLocationTextView;
    LinearLayout userEmailLayout,userLocationLayout;

    public EditProfile() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {




        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        userEmailLayout = view.findViewById(R.id.editEmailLayout);
        userLocationLayout = view.findViewById(R.id.editLocationLayout);
        userEmailTextView = view.findViewById(R.id.editEmailTextView);
        userLocationTextView = view.findViewById(R.id.editLocationTextView);

        userEmailTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                userEmailLayout.setVisibility(View.VISIBLE);
                userEmailTextView.setVisibility(View.GONE);
                return false;
            }
        });
        userLocationTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                userLocationLayout.setVisibility(View.VISIBLE);
                userLocationTextView.setVisibility(View.GONE);
                return false;
            }
        });

        return view;

    }
}