package com.anikrakib.tourday.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anikrakib.tourday.Adapter.AdapterPost;
import com.anikrakib.tourday.Models.PostItem;
import com.anikrakib.tourday.R;

import java.util.ArrayList;
import java.util.List;


public class Post extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;


    public Post() {
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
        View v = inflater.inflate(R.layout.fragment_post, container, false);

        /////*     initialize view   */////
        recyclerView = (RecyclerView)v. findViewById(R.id.postRecyclerView);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setFocusable(false);

        //create dummy data to show in list
        List<PostItem> list = new ArrayList();
        list.add(new PostItem("QUOTES", "Contrary to popular belief, Lorem Ipsum is not simply random text. It has roots in a piece of classical Latin literature from 45 BC, making it over 2000 years old. Richard McClintock, a Latin professor at Hampden-Sydney College in Virginia, looked up one of the more obscure Latin words, consectetur, from a Lorem Ipsum passage, and going through the cites of the word in classical literature, discovered the undoubtable source. Lorem Ipsum comes from sections 1.10.32 and 1.10.33 of \"de Finibus Bonorum et Malorum\" (The Extremes of Good and Evil) by Cicero, written in 45 BC. This book is a treatise on the theory of ethics, very popular during the Renaissance. The first line of Lorem Ipsum, \"Lorem ipsum dolor sit amet..\", comes from a line in section 1.10.32.", "4th october", 50, 40, true));
        list.add(new PostItem("QUOTES", "Life is what happens when you’re busy making other plans. John Lennon", "4th october ", 14, 12, false));
        list.add(new PostItem("QUOTES", "Very little is needed to make a happy life; it is all within yourself, in your way of thinking. Marcus Aurelius", "4th october", 31, 12, true));
        list.add(new PostItem("QUOTES", "Life is like playing a violin in public and learning the instrument as one goes on. Samuel Butler", "4th october", 11, 14, true));
        list.add(new PostItem("QUOTES", "In the end, it’s not the years in your life that count. It’s the life in your years. Abraham Lincoln ", "4th october", 51, 51, false));
        list.add(new PostItem("QUOTES", "Believe that life is worth living and your belief will help create the fact. William James", "4th october", 21, 51, true));
        list.add(new PostItem("QUOTES", "The trick in life is learning how to deal with it. Helen Mirren", "4th october", 11, 51, false));
        list.add(new PostItem("QUOTES", "Don’t gain the world and lose your soul, wisdom is better than silver or gold. Bob Marley", "4th october", 21, 58, false));
        list.add(new PostItem("QUOTES", "Anyone who lives within their means suffers from a lack of imagination. Oscar Wilde", "4th october", 61, 52, true));
        list.add(new PostItem("QUOTES", "Not how long, but how well you have lived is the main thing. Seneca", "4th october", 44, 52, true));
        list.add(new PostItem("QUOTES", "Tis better to have loved and lost than never to have loved at all. St. Augustine", "4th october", 21, 52, false));
        list.add(new PostItem("QUOTES", "You only live once, but if you do it right, once is enough. Mae West", "4th october", 32, 42, true));


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        ///  add items to the adapter
        mAdapter = new AdapterPost(list);
        ///  set Adapter to RecyclerView
        recyclerView.setAdapter(mAdapter);

        return v;
    }
}