package com.anikrakib.tourday.Adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.anikrakib.tourday.Activity.MyProfileActivity;
import com.anikrakib.tourday.Activity.SignInActivity;
import com.anikrakib.tourday.Models.PostItem;
import com.anikrakib.tourday.Models.Token;
import com.anikrakib.tourday.R;
import com.anikrakib.tourday.WebService.RetrofitClient;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;
import com.squareup.picasso.Picasso;
import com.tylersuehr.socialtextview.SocialTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


public class AdapterPost extends RecyclerView.Adapter<AdapterPost.ExampleViewHolder> {
    private Context mContext;
    private ArrayList<PostItem> mPostItemList;
    Dialog myDialog;

    public AdapterPost(Context context, ArrayList<PostItem> exampleList) {
        mContext = context;
        mPostItemList = exampleList;
    }
    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.list_post_item_update, parent, false);
        return new ExampleViewHolder(v);
    }
    @Override
    public void onBindViewHolder(ExampleViewHolder holder, int position) {
        SharedPreferences userPref =mContext.getSharedPreferences("user", MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = userPref.edit();
        final PostItem currentItem = mPostItemList.get(position);
        String imageUrl = currentItem.getImageUrl();
        String post = currentItem.getPost();
        String date = currentItem.getDate();
        String location = currentItem.getLocation();
        int likeCount = currentItem.getLikeCount();
        String postId = currentItem.getmId();
//        boolean selfLike = currentItem.getSelfLike();
        holder.txtPost.setLinkText(post);
        holder.txtLocation.setText(location);
        holder.txtDate.setText(date);
        holder.mTextViewLikes.setText(""+likeCount);
        Picasso.get().load("https://www.tourday.team/"+imageUrl).fit().centerInside().into(holder.postImage);

        String userProfilePicture = userPref.getString("userProfilePicture","");
        String userName = userPref.getString("userName","");
        Picasso.get().load("https://www.tourday.team/"+userProfilePicture).fit().centerInside().into(holder.userProfilePic);
        holder.userName.setText(userName);

        // check post is self liked or not

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

        holder.morePostButton.setOnClickListener(v->{
            PopupMenu popupMenu = new PopupMenu(mContext,holder.morePostButton);
            popupMenu.inflate(R.menu.edit_delete_post_menu);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    switch (item.getItemId()){
                        case R.id.editPost: {

                            return true;
                        }
                        case R.id.delete_post: {
                            deletePost(currentItem.getmId(),position);
                            return true;
                        }
                    }

                    return false;
                }
            });
            popupMenu.show();
        });
    }
    @Override
    public int getItemCount() {
        return mPostItemList.size();
    }
    public class ExampleViewHolder extends RecyclerView.ViewHolder{
        public ImageView postImage,morePostButton,blike;
        public TextView txtLocation,txtDate;
        public TextView mTextViewLikes;
        RelativeLayout relativeLayoutPostItem;
        SocialTextView txtPost;
        CircleImageView userProfilePic;
        TextView userName;

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
            userName = itemView.findViewById(R.id.userNameListItem);


        }
    }

    public void selfLike(String postId, int position, ExampleViewHolder holder, ArrayList<PostItem> mPostItemList){
        SharedPreferences userPref = mContext.getSharedPreferences("user", MODE_PRIVATE);
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

    public void deletePost(String getId,int position){
        SharedPreferences userPref = mContext.getSharedPreferences("user", MODE_PRIVATE);
        String token = userPref.getString("token","");

        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .deletePost("Token "+token,getId);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    mPostItemList.remove(position);
                    notifyItemRemoved(position);
                    DynamicToast.makeError(mContext, "Post Deleted !!").show();
                }else{
                    DynamicToast.makeError(mContext, "Something Wrong !!").show();
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
