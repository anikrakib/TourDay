package com.anikrakib.tourday.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anikrakib.tourday.Models.BlogItem;
import com.anikrakib.tourday.Models.PostItem;
import com.anikrakib.tourday.R;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterBlog extends RecyclerView.Adapter<AdapterBlog.BlogViewHolder>{

    private Context mContext;
    private ArrayList<BlogItem> mBlogItemList;
    Context context;
    Dialog myDialog;

    public AdapterBlog(Context mContext, ArrayList<BlogItem> mBlogItemList) {
        this.mContext = mContext;
        this.mBlogItemList = mBlogItemList;
    }

    @NonNull
    @Override
    public BlogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.list_blog_item, parent, false);
        return new BlogViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BlogViewHolder holder, int position) {
        final BlogItem currentItem = mBlogItemList.get(position);
        String imageUrl = currentItem.getBlogImageUrl();
        String title = currentItem.getBlogTitle();
        String description = currentItem.getBlogDescription();
        String division = currentItem.getBlogDivision();
        String date = currentItem.getBlogDate();
        String id = currentItem.getBlogId();
        String author = currentItem.getBlogAuthorName();

        holder.blogLocation.setText(division);
        holder.blogDate.setText(date);
        holder.blogTitle.setText(title);
        Picasso.get().load("https://tourday.team/"+imageUrl).fit().centerInside().into(holder.blogImage);
    }

    @Override
    public int getItemCount() {
        return mBlogItemList.size();
    }

    public class BlogViewHolder extends RecyclerView.ViewHolder {

        public RoundedImageView blogImage;
        public TextView blogLocation,blogDate,blogTitle;
        RelativeLayout relativeLayoutPostItem;


        public BlogViewHolder(@NonNull View itemView) {
            super(itemView);

            blogImage = itemView.findViewById(R.id.blogImage);
            blogDate = itemView.findViewById(R.id.blogDate);
            blogLocation = itemView.findViewById(R.id.blogLocation);
            blogTitle = itemView.findViewById(R.id.blogTitle);
        }
    }
}
