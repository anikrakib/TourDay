package com.anikrakib.tourday.Adapter.Profile;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.anikrakib.tourday.Models.Profile.PostItem;
import com.anikrakib.tourday.R;
import com.anikrakib.tourday.Utils.ApiURL;
import com.anikrakib.tourday.WebService.RetrofitClient;
import com.squareup.picasso.Picasso;
import com.tylersuehr.socialtextview.SocialTextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtherUserAdapterPost extends RecyclerView.Adapter<OtherUserAdapterPost.ExampleViewHolder> {
    private Context mContext;
    private ArrayList<PostItem> mPostItemList;
    Dialog myDialog;

    public OtherUserAdapterPost(Context context, ArrayList<PostItem> exampleList) {
        mContext = context;
        mPostItemList = exampleList;
    }
    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.list_post_item, parent, false);
        return new ExampleViewHolder(v);
    }
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ExampleViewHolder holder, int position) {
        final PostItem currentItem = mPostItemList.get(position);
        String imageUrl = currentItem.getImageUrl();
        String post = currentItem.getPost();
        String date = currentItem.getDate();
        String location = currentItem.getLocation();
        int likeCount = currentItem.getLikeCount();
        String postId = currentItem.getmId();

        SharedPreferences userPref = mContext.getSharedPreferences("user", Context.MODE_PRIVATE);
        String userName = userPref.getString("userName","");
        SharedPreferences userPref2 = mContext.getSharedPreferences("otherUser", Context.MODE_PRIVATE);
        String userName2 = userPref2.getString("otherUsersUserName","");
        String otherUsersProfilePic = userPref2.getString("otherUsersProfilePic","");


//        boolean selfLike = currentItem.getSelfLike();
        holder.txtPost.setLinkText(post);
        holder.txtLocation.setText(location);
        holder.txtDate.setText(date);
        holder.userNameListItem.setText(userName2);
        //holder.mTextViewLikes.setText(likeCount+" Likes");
        if(currentItem.getSelfLike()){
            if((likeCount-1) == 0 ){
                holder.mTextViewLikes.setText("You Liked");
            }else{
                holder.mTextViewLikes.setText("You and "+(likeCount-1)+" Other Likes");
            }
        }else{
            holder.mTextViewLikes.setText(likeCount+" Likes");
        }
        Picasso.get().load(ApiURL.IMAGE_BASE+"/"+imageUrl).fit().centerInside().into(holder.postImage);
        Picasso.get().load(ApiURL.IMAGE_BASE+"/"+otherUsersProfilePic).fit().centerInside().into(holder.userProfilePic);


        assert userName != null;
        if(userName.equals(userName2)){
            holder.morePostButton.setVisibility(View.VISIBLE);
        } else {
            holder.morePostButton.setVisibility(View.GONE);
        }

        holder.blike.setImageResource(
                currentItem.getSelfLike()?R.drawable.ic_like:R.drawable.ic_unlike
        );

        holder.blike.setOnClickListener(v->{
            holder.blike.setImageResource(
                    currentItem.getSelfLike()?R.drawable.ic_unlike:R.drawable.ic_like
            );
            selfLike(postId,position,holder,mPostItemList);
        });


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

    public class ExampleViewHolder extends RecyclerView.ViewHolder{
        public ImageView postImage,morePostButton,blike;
        public TextView txtLocation,txtDate;
        public TextView mTextViewLikes,userNameListItem;
        RelativeLayout relativeLayoutPostItem;
        SocialTextView txtPost;
        CircleImageView userProfilePic;

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
            userProfilePic = itemView.findViewById(R.id.userProfilePicListItem);
            userNameListItem = itemView.findViewById(R.id.userNameListItem);


        }
    }

    public void selfLike(String postId, int position, ExampleViewHolder holder, ArrayList<PostItem> mPostItemList){
        SharedPreferences userPref = mContext.getSharedPreferences("user", Context.MODE_PRIVATE);
        String token = userPref.getString("token","");

        PostItem postItem = mPostItemList.get(position);

        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .selfLike("Token "+token,Integer.parseInt(postId));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    postItem.setSelfLike(!postItem.getSelfLike());
                    postItem.setLikes(postItem.getSelfLike()?postItem.getLikeCount()+1:postItem.getLikeCount()-1);
                    mPostItemList.set(position,postItem);
                    notifyItemChanged(position);
                    notifyDataSetChanged();
                }else{
                    holder.blike.setImageResource(
                            postItem.getSelfLike()?R.drawable.ic_like:R.drawable.ic_unlike
                    );
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    public void showDialog(PostItem item){

        ImageView close,postDetailImage;
        ImageButton postDetailLike;
        TextView postDetailDateView,postDetailsLikeCount,postDetailsLocation;
        SocialTextView postDetailDescriptionTextView;


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
        postDetailDescriptionTextView.setLinkText(item.getPost());
        postDetailsLocation.setText(item.getLocation());
        postDetailsLikeCount.setText(String.valueOf(item.getLikeCount()));

        postDetailLike.setImageResource(
                item.getSelfLike()?R.drawable.ic_like:R.drawable.ic_unlike
        );

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