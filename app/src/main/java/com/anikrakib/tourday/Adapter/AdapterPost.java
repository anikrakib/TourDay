package com.anikrakib.tourday.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.anikrakib.tourday.Models.PostItem;
import com.anikrakib.tourday.R;
import java.util.List;

public class AdapterPost  extends RecyclerView.Adapter<AdapterPost.ViewHolder> {
    private LayoutInflater mInflater;
    private List<PostItem> mData;

    public AdapterPost(List<PostItem> Data) {
        mData = Data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_post_item, viewGroup, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        PostItem item = mData.get(position);
        viewHolder.txTitle.setText(item.Title);
        viewHolder.txtBody.setText(item.Body);
        viewHolder.txtcommentcount.setText(String.valueOf(item.commentCount));
        viewHolder.txtDate.setText(item.Date);
        viewHolder.txtlikecount.setText(String.valueOf(item.likecount));
        viewHolder.postImage.setImageResource(item.imageUrl);

        // check if it was liked or no
        if (item.like) {
            viewHolder.bLike.setImageResource(R.drawable.ic_like);
        } else {
            viewHolder.bLike.setImageResource(R.drawable.ic_unlike);
        }


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txTitle, txtBody, txtDate, txtcommentcount, txtlikecount;
        public ImageButton bLike;
        public ImageView postImage;

        public ViewHolder(View v) {
            super(v);
            txTitle = v.findViewById(R.id.id_Title_TextView);
            txtBody = v.findViewById(R.id.postDescriptionTextView);
            txtDate = v.findViewById(R.id.id_Date_TextView);
            txtcommentcount = v.findViewById(R.id.id_commentCount_TextView);
            txtlikecount = v.findViewById(R.id.id_likeCount_TextView);
            bLike = v.findViewById(R.id.id_like_ImageButton);
            postImage = v.findViewById(R.id.postImage);
        }
    }
}
