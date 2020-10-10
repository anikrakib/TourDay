package com.anikrakib.tourday.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.anikrakib.tourday.Activity.YourBlogDetailsActivity;
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

        holder.yourBlogLocation.setText(division);
        holder.yourBlogDate.setText(date);
        holder.yourBlogTitle.setText(title);
        Picasso.get().load("https://tourday.team/"+imageUrl).fit().centerInside().into(holder.yourBlogImage);

        holder.cardViewYourBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent;
                intent =  new Intent(mContext, YourBlogDetailsActivity.class);
                intent.putExtra("yourBlogTitle",title);
                intent.putExtra("yourBlogImage",imageUrl);
                intent.putExtra("yourBlogDescription",description);
                intent.putExtra("yourBlogDivision",division);
                intent.putExtra("yourBlogDate",date);
                intent.putExtra("yourBlogId",id);
                mContext.startActivity(intent);
            }
        });

        //set Animation in recyclerView Item
        holder.cardViewYourBlog.setAnimation(AnimationUtils.loadAnimation(mContext,R.anim.fade_transition_animation));
        holder.cardViewYourBlog.setAnimation(AnimationUtils.loadAnimation(mContext,R.anim.fade_scale_animation));
    }

    @Override
    public int getItemCount() {
        return mYourBlogItemList.size();
    }

    public class YourBlogViewHolder extends RecyclerView.ViewHolder {

        public RoundedImageView yourBlogImage;
        public TextView yourBlogLocation,yourBlogDate,yourBlogTitle;
        CardView cardViewYourBlog;


        public YourBlogViewHolder(@NonNull View itemView) {
            super(itemView);

            yourBlogImage= itemView.findViewById(R.id.yourBlogImage);
            yourBlogDate = itemView.findViewById(R.id.yourBlogDate);
            yourBlogLocation = itemView.findViewById(R.id.yourBlogLocation);
            yourBlogTitle = itemView.findViewById(R.id.yourBlogTitle);
            cardViewYourBlog = itemView.findViewById(R.id.cardViewYourBlog);
        }
    }
}
