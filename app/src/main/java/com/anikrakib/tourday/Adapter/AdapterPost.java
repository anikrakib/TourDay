package com.anikrakib.tourday.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.anikrakib.tourday.Models.PostItem;
import com.anikrakib.tourday.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class AdapterPost extends RecyclerView.Adapter<AdapterPost.ExampleViewHolder> {
    private Context mContext;
    private ArrayList<PostItem> mPostItemList;
    Context context;
    Dialog myDialog;

    public AdapterPost(Context context, ArrayList<PostItem> exampleList) {
        mContext = context;
        mPostItemList = exampleList;
    }
    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.list_post_item, parent, false);
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
        boolean selfLike = currentItem.getSelfLike();
        holder.txtPost.setText(post);
        holder.txtLocation.setText(location);
        holder.txtDate.setText(date);
        holder.mTextViewLikes.setText(""+likeCount);
        Picasso.get().load("https://tourday.team/"+imageUrl).fit().centerInside().into(holder.postImage);

        // check if it was liked or no
        if (selfLike) {
            holder.blike.setImageResource(R.drawable.ic_like);
        } else {
            holder.blike.setImageResource(R.drawable.ic_unlike);
        }

        holder.relativeLayoutPostItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog = new Dialog(mContext);
                showDialog(currentItem);
            }
        });
    }
    @Override
    public int getItemCount() {
        return mPostItemList.size();
    }
    public class ExampleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, PopupMenu.OnMenuItemClickListener  {
        public ImageView postImage,morePostButton,blike;
        public TextView txtLocation,txtPost,txtDate;
        public TextView mTextViewLikes;
        RelativeLayout relativeLayoutPostItem;

        public ExampleViewHolder(View itemView) {
            super(itemView);

            mTextViewLikes = itemView.findViewById(R.id.id_likeCount_TextView);
            txtLocation = itemView.findViewById(R.id.postLocation);
            txtPost = itemView.findViewById(R.id.postDescriptionTextView);
            txtDate = itemView.findViewById(R.id.id_Date_TextView);
            postImage = itemView.findViewById(R.id.postImage);
            relativeLayoutPostItem = itemView.findViewById(R.id.relativeLayoutPostItem);
            morePostButton = itemView.findViewById(R.id.moreButtonPost);
            blike = itemView.findViewById(R.id.id_like_ImageButton);


            morePostButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            PopupMenu popupMenu = new PopupMenu(v.getContext(),v);
            popupMenu.inflate(R.menu.edit_delete_post_menu);
            popupMenu.setOnMenuItemClickListener(this);
            popupMenu.show();
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()){
                case R.id.editPost:
                    return true;
                case R.id.delete_post:
                    return true;
            }
            return false;
        }
    }

    public void showDialog(PostItem item){

        ImageView close,postDetailImage;
        ImageButton postDetailLike;
        TextView postDetailDescriptionTextView,postDetailDateView,postDetailsLikeCount,postDetailsLocation;


        myDialog.setContentView(R.layout.post_details);
        close = myDialog.findViewById(R.id.postDetailsClose);
        postDetailImage = myDialog.findViewById(R.id.postDetailImage);
        postDetailLike = myDialog.findViewById(R.id.postLikeImageButton);
        postDetailDescriptionTextView = myDialog.findViewById(R.id.postDetailDescriptionTextView);
        postDetailDateView = myDialog.findViewById(R.id.postDetailDate);
        postDetailsLikeCount = myDialog.findViewById(R.id.postLikeDetailTextView);
        postDetailsLocation = myDialog.findViewById(R.id.postDetailsLocation);


        postDetailDateView.setText(item.getDate());
        Picasso.get().load("https://tourday.team/"+item.getImageUrl()).fit().centerInside().into(postDetailImage);
        postDetailDescriptionTextView.setText(item.getPost());
        postDetailsLocation.setText(item.getLocation());
        postDetailsLikeCount.setText(String.valueOf(item.getLikeCount()));
        if (item.getSelfLike()) {
            postDetailLike.setImageResource(R.drawable.ic_like);
        } else {
            postDetailLike.setImageResource(R.drawable.ic_unlike);
        }


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        myDialog.setCancelable(false);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();

    };
}
