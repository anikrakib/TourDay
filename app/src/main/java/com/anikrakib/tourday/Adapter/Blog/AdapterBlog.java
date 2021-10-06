package com.anikrakib.tourday.Adapter.Blog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.anikrakib.tourday.Activity.Blog.BlogDetailsActivity;
import com.anikrakib.tourday.Models.Blog.BlogItem;
import com.anikrakib.tourday.Models.Blog.DeleteBlogResponse;
import com.anikrakib.tourday.R;
import com.anikrakib.tourday.Utils.ApiURL;
import com.anikrakib.tourday.WebService.RetrofitClient;
import com.makeramen.roundedimageview.RoundedImageView;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterBlog extends RecyclerView.Adapter<AdapterBlog.BlogViewHolder>{

    private final Context mContext;
    private final ArrayList<BlogItem> mBlogItemList;
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

        myDialog = new Dialog(mContext);

        SharedPreferences userPref = mContext.getSharedPreferences("user", Context.MODE_PRIVATE);
        String userName = userPref.getString("userName","");

        holder.blogLocation.setText(division);
        holder.blogDate.setText(date);
        holder.blogTitle.setText(title);
        if(currentItem.isSearch()){
            Picasso.get().load(imageUrl).fit().centerInside().into(holder.blogImage);
        }else{
            Picasso.get().load(ApiURL.IMAGE_BASE+"/"+imageUrl).fit().centerInside().into(holder.blogImage);
        }

        if(currentItem.getBlogAuthorName().equals(userName)){
            holder.moreBlogButton.setVisibility(View.VISIBLE);
        } else {
            holder.moreBlogButton.setVisibility(View.GONE);
        }

        holder.cardViewBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent;
                intent =  new Intent(mContext, BlogDetailsActivity.class);
                intent.putExtra("blogId",Integer.parseInt(id));
                mContext.startActivity(intent);
            }
        });

        holder.moreBlogButton.setOnClickListener(v->{
            PopupMenu popupMenu = new PopupMenu(mContext,holder.moreBlogButton);
            popupMenu.inflate(R.menu.delete_post_or_blog_menu);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    if (item.getItemId() == R.id.delete_post) {
                        showDeleteBlogPopUp(String.valueOf(id),position);
                    }

                    return false;
                }
            });
            popupMenu.show();
        });

        //set Animation in recyclerView Item
        holder.blogItemLayout.setAnimation(AnimationUtils.loadAnimation(mContext,R.anim.fade_scale_animation));
    }

    @Override
    public int getItemCount() {
        return mBlogItemList.size();
    }

    public class BlogViewHolder extends RecyclerView.ViewHolder {

        public RoundedImageView blogImage;
        public TextView blogLocation,blogDate,blogTitle;
        LinearLayout blogItemLayout;
        CardView cardViewBlog;
        ImageView moreBlogButton;


        public BlogViewHolder(@NonNull View itemView) {
            super(itemView);

            blogImage = itemView.findViewById(R.id.blogImage);
            blogDate = itemView.findViewById(R.id.blogDate);
            blogLocation = itemView.findViewById(R.id.blogLocation);
            blogTitle = itemView.findViewById(R.id.blogTitle);
            blogItemLayout = itemView.findViewById(R.id.blogItemLayout);
            cardViewBlog = itemView.findViewById(R.id.cardViewBlog);
            moreBlogButton = itemView.findViewById(R.id.moreButtonBlog);
        }
    }


    // this method for show custom pop user wants to delete pop up or not
    public void showDeleteBlogPopUp(String blogId,int position) {
        Button yesButton,noButton;
        myDialog.setContentView(R.layout.custom_delete_blog_pop_up);
        yesButton = myDialog.findViewById(R.id.yesButton);
        noButton = myDialog.findViewById(R.id.noButton);

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteBlog(blogId,position);
                myDialog.dismiss();
            }
        });
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.setCancelable(false);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();

    }

    /* This Method delete specific Blog from server using blogId
      API->(DELETE) https://www.tourday.team/api/blog/delete/post_id
      Token is required, Request Token who create a post. Add token with request header.
      Request:
        {
            "Authorization": "Token 93bc86220b144548e5bb507851b6ef7c2a5e1a14",
        }
        Response:
            "Delete Successfully."
    */
    public void deleteBlog(String getId,int position){
        SharedPreferences userPref = mContext.getSharedPreferences("user", Context.MODE_PRIVATE);
        String token = userPref.getString("token","");

        Call<DeleteBlogResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .deleteBlog("Token "+token,getId);

        call.enqueue(new Callback<DeleteBlogResponse>() {
            @Override
            public void onResponse(Call<DeleteBlogResponse> call, Response<DeleteBlogResponse> response) {
                if(response.isSuccessful()){
                    mBlogItemList.remove(position);
                    notifyItemRemoved(position);
                    assert response.body() != null;
                    String success = response.body().getSuccess();
                    DynamicToast.makeSuccess(mContext, success).show();

                }else{
                    DynamicToast.makeError(mContext, "Something Wrong !!").show();
                }
            }

            @Override
            public void onFailure(Call<DeleteBlogResponse> call, Throwable t) {
                Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
