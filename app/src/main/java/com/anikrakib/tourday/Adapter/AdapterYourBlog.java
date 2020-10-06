package com.anikrakib.tourday.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anikrakib.tourday.Models.BlogItem;
import com.anikrakib.tourday.Models.YourBlogItem;
import com.anikrakib.tourday.R;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterYourBlog extends RecyclerView.Adapter<AdapterYourBlog.YourBlogViewHolder>{
    private Context mContext;
    private ArrayList<YourBlogItem> mYourBlogItemList;
    Context context;
    Dialog myDialog;

    public AdapterYourBlog(Context mContext, ArrayList<YourBlogItem> mBlogItemList) {
        this.mContext = mContext;
        this.mYourBlogItemList = mBlogItemList;
    }

    @NonNull
    @Override
    public AdapterYourBlog.YourBlogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.list_your_blog_item, parent, false);
        return new AdapterYourBlog.YourBlogViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterYourBlog.YourBlogViewHolder holder, int position) {
        final YourBlogItem currentItem = mYourBlogItemList.get(position);
        String imageUrl = currentItem.getYourBlogImageUrl();
        String title = currentItem.getYourBlogTitle();
        String description = currentItem.getYourBlogDescription();
        String division = currentItem.getYourBlogDivision();
        String date = currentItem.getYourBlogDate();
        String id = currentItem.getYourBlogId();
        String author = currentItem.getYourBlogAuthorName();

        holder.yourBlogLocation.setText(division);
        holder.yourBlogDate.setText(date);
        holder.yourBlogTitle.setText(title);
        Picasso.get().load("https://tourday.team/"+imageUrl).fit().centerInside().into(holder.yourBlogImage);
    }

    @Override
    public int getItemCount() {
        return mYourBlogItemList.size();
    }

    public class YourBlogViewHolder extends RecyclerView.ViewHolder {

        public RoundedImageView yourBlogImage;
        public TextView yourBlogLocation,yourBlogDate,yourBlogTitle;
        RelativeLayout relativeLayoutYourBlogItem;


        public YourBlogViewHolder(@NonNull View itemView) {
            super(itemView);

            yourBlogImage= itemView.findViewById(R.id.yourBlogImage);
            yourBlogDate = itemView.findViewById(R.id.yourBlogDate);
            yourBlogLocation = itemView.findViewById(R.id.yourBlogLocation);
            yourBlogTitle = itemView.findViewById(R.id.yourBlogTitle);
        }
    }
}
