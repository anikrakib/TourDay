package com.anikrakib.tourday.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anikrakib.tourday.Adapter.AdapterEvent;
import com.anikrakib.tourday.Models.PostEvent;
import com.anikrakib.tourday.R;

import java.util.ArrayList;
import java.util.List;

public class Event extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;



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

        /////*     initialize view   */////
        recyclerView = (RecyclerView)view. findViewById(R.id.eventRecyclerView);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setFocusable(false);

        //create dummy data to show in list
        List<PostEvent> list = new ArrayList();
        list.add(new PostEvent("Google I/O 2020", "Contrary to popular belief, Lorem Ipsum is not simply random text. It has roots in a piece of classical Latin literature from 45 BC, making it over 2000 years old. Richard McClintock, a Latin professor at Hampden-Sydney College in Virginia, looked up one of the more obscure Latin words, consectetur, from a Lorem Ipsum passage, and going through the cites of the word in classical literature, discovered the undoubtable source. Lorem Ipsum comes from sections 1.10.32 and 1.10.33 of \"de Finibus Bonorum et Malorum\" (The Extremes of Good and Evil) by Cicero, written in 45 BC. This book is a treatise on the theory of ethics, very popular during the Renaissance. The first line of Lorem Ipsum, \"Lorem ipsum dolor sit amet..\", comes from a line in section 1.10.32.", "22-04-2020", "20-06-2020","Nurjahan Road, Mohammadpur",true,R.drawable.event_pic));
        list.add(new PostEvent("About Create Event 2018", "Life is what happens when you’re busy making other plans. John Lennon", "21-08-2020", "22-05-2020","Mohammadpur",false,R.drawable.event_pic2));
        list.add(new PostEvent("QUOTES - 1", "Very little is needed to make a happy life; it is all within yourself, in your way of thinking. Marcus Aurelius", "22-06-2020", "15-04-2020","Dhanmondi",false,R.drawable.event_pic3));
        list.add(new PostEvent("QUOTES - 2", "Tis better to have loved and lost than never to have loved at all. St. Augustine", "22-01-2020", "30-02-2020","Laksam,Cumilla",false,R.drawable.event_pic4));
        list.add(new PostEvent("QUOTES - 3", "Not how long, but how well you have lived is the main thing. Seneca", "20-08-2020", "22-09-2020","Mirpur1, Dhaka",true,R.drawable.event_pic5));
        list.add(new PostEvent("QUOTES - 4", "You only live once, but if you do it right, once is enough. Mae West", "20-08-2020", "22-09-2020","Mirpur10, Dhaka",true,R.drawable.event_pic3));
        list.add(new PostEvent("QUOTES - 5", "In the end, it’s not the years in your life that count. It’s the life in your years. Abraham Lincoln", "20-08-2020", "22-09-2020","New Market, Dhaka",false,R.drawable.event_pic5));


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        ///  add items to the adapter
        mAdapter = new AdapterEvent(list);
        ///  set Adapter to RecyclerView
        recyclerView.setAdapter(mAdapter);

        return  view;
    }
}