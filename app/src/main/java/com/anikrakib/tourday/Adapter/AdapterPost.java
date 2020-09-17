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
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anikrakib.tourday.Models.PostItem;
import com.anikrakib.tourday.R;
import java.util.List;

public class AdapterPost  extends RecyclerView.Adapter<AdapterPost.ViewHolder>{
    private LayoutInflater mInflater;
    private List<PostItem> mData;
    Context context;
    Dialog myDialog;


    public AdapterPost(Context context,List<PostItem> Data) {
        this.context =context;
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

        final PostItem item = mData.get(position);
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


        viewHolder.relativeLayoutPostItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog = new Dialog(context);
                showDialog(item);
            }
        });

    }


    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, PopupMenu.OnMenuItemClickListener  {
        public TextView txTitle, txtBody, txtDate, txtcommentcount, txtlikecount;
        public ImageButton bLike;
        public ImageView postImage,morePostButton;
        RelativeLayout relativeLayoutPostItem;



        public ViewHolder(View v) {
            super(v);
            txTitle = v.findViewById(R.id.id_Title_TextView);
            txtBody = v.findViewById(R.id.postDescriptionTextView);
            txtDate = v.findViewById(R.id.id_Date_TextView);
            txtcommentcount = v.findViewById(R.id.id_commentCount_TextView);
            txtlikecount = v.findViewById(R.id.id_likeCount_TextView);
            bLike = v.findViewById(R.id.id_like_ImageButton);
            postImage = v.findViewById(R.id.postImage);
            morePostButton = v.findViewById(R.id.moreButtonPost);
            relativeLayoutPostItem = v.findViewById(R.id.relativeLayoutPostItem);


            morePostButton.setOnClickListener(this);
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
    }

    public void showDialog(PostItem item){

        ImageView close,postDetailImage;
        ImageButton postDetailLike;
        TextView postDetailDescriptionTextView,postDetailDateView,postDetailsLikeCount;


        myDialog.setContentView(R.layout.post_details);
        close = myDialog.findViewById(R.id.postDetailsClose);
        postDetailImage = myDialog.findViewById(R.id.postDetailImage);
        postDetailLike = myDialog.findViewById(R.id.postLikeImageButton);
        postDetailDescriptionTextView = myDialog.findViewById(R.id.postDetailDescriptionTextView);
        postDetailDateView = myDialog.findViewById(R.id.postDetailDate);
        postDetailsLikeCount = myDialog.findViewById(R.id.postLikeDetailTextView);


        postDetailDateView.setText(item.Date);
        postDetailImage.setImageResource(item.imageUrl);
        postDetailDescriptionTextView.setText(item.Body);
        if (item.like) {
            postDetailLike.setImageResource(R.drawable.ic_like);
        } else {
            postDetailLike.setImageResource(R.drawable.ic_unlike);
        }
        postDetailsLikeCount.setText(String.valueOf(item.likecount));


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
