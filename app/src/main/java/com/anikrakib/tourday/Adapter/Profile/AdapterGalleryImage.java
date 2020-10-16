package com.anikrakib.tourday.Adapter.Profile;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.anikrakib.tourday.Models.Profile.PostItem;
import com.anikrakib.tourday.R;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterGalleryImage extends RecyclerView.Adapter<AdapterGalleryImage.ExampleViewHolder> {
    private Context mContext;
    private ArrayList<PostItem> mPostItemList;
    Dialog myDialog;
    String postId;

    public AdapterGalleryImage(Context context, ArrayList<PostItem> exampleList) {
        mContext = context;
        mPostItemList = exampleList;
    }
    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.gallery_image_item, parent, false);
        return new ExampleViewHolder(v);
    }
    @Override
    public void onBindViewHolder(ExampleViewHolder holder, int position) {

        final PostItem currentItem = mPostItemList.get(position);
        String imageUrl = currentItem.getImageUrl();
        String post = currentItem.getPost();
        String date = currentItem.getDate();
        String location = currentItem.getLocation();
        int likeCount = currentItem.getLikeCount();
        postId = currentItem.getmId();

        Picasso.get().load("https://www.tourday.team/"+imageUrl).into(holder.galleryImage);

    }
    @Override
    public int getItemCount() {
        return mPostItemList.size();
    }
    public class ExampleViewHolder extends RecyclerView.ViewHolder{

        RoundedImageView galleryImage;

        public ExampleViewHolder(View itemView) {
            super(itemView);

            galleryImage = itemView.findViewById(R.id.galleryImageItem);
        }
    }

}