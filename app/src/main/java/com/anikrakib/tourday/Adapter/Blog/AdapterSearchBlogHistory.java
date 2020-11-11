package com.anikrakib.tourday.Adapter.Blog;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import com.anikrakib.tourday.Activity.SearchActivity;
import com.anikrakib.tourday.Models.Blog.SearchBlogHistory;
import com.anikrakib.tourday.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class AdapterSearchBlogHistory extends RecyclerView.Adapter<AdapterSearchBlogHistory.Holder> {

    private final Context context;
    private List<SearchBlogHistory> searchBlogHistories;
    private final SearchView blogSearchView;

    public AdapterSearchBlogHistory(Context context, List<SearchBlogHistory> searchBlogHistories, SearchView blogSearchView) {
        if (searchBlogHistories == null) {
            this.context = context;
            this.searchBlogHistories = searchBlogHistories;
            this.blogSearchView = blogSearchView;
        }else{
            this.context = context;
            this.searchBlogHistories = searchBlogHistories;
            this.blogSearchView = blogSearchView;
            Collections.reverse(searchBlogHistories);
        }
    }

    public void setTaskModelList(List<SearchBlogHistory> searchBlogHistories) {
        this.searchBlogHistories = searchBlogHistories;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(context)
                .inflate(R.layout.list_search_history_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.searchText.setText(searchBlogHistories.get(position).getSearchText());

        holder.blogSearchViewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return searchBlogHistories != null ? searchBlogHistories.size() : 0;
    }

    public static class Holder extends RecyclerView.ViewHolder {
        TextView searchText;
        RelativeLayout blogSearchViewLayout;

        public Holder(@NonNull View itemView) {
            super(itemView);
            searchText = itemView.findViewById(R.id.searchText);
            blogSearchViewLayout = itemView.findViewById(R.id.blogSearchViewLayout);
        }
    }
}

