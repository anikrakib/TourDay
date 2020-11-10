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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.anikrakib.tourday.Activity.Blog.BlogDetailsActivity;
import com.anikrakib.tourday.Activity.Blog.YourBlogDetailsActivity;
import com.anikrakib.tourday.Models.Blog.AllBlogResult;
import com.anikrakib.tourday.Models.Blog.BlogItem;
import com.anikrakib.tourday.Models.Blog.DeleteBlogResponse;
import com.anikrakib.tourday.R;
import com.anikrakib.tourday.Utils.ApiURL;
import com.anikrakib.tourday.Utils.PaginationAdapterCallback;
import com.anikrakib.tourday.WebService.RetrofitClient;
import com.makeramen.roundedimageview.RoundedImageView;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterAllBlog extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // View Types
    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private List<AllBlogResult> allBlogResults;
    private List<BlogItem> blogItems;
    private final Context context;
    Dialog myDialog;

    private boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;

    private PaginationAdapterCallback mCallback;

    public AdapterAllBlog(Context context) {
        this.context = context;
        allBlogResults = new ArrayList<>();
    }

    public List<AllBlogResult> getAllBlogResults() {
        return allBlogResults;
    }

    public void setAllBlogResults(List<AllBlogResult> allBlogResults) {
        this.allBlogResults = allBlogResults;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View viewItem = inflater.inflate(R.layout.list_blog_item, parent, false);
        viewHolder = new BlogVh(viewItem);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        AllBlogResult allBlogResult = allBlogResults.get(position);
       // BlogItem currentItem = blogItems.get(position);

        final BlogVh blogVh = (BlogVh) holder;

        myDialog = new Dialog(context);

        SharedPreferences userPref = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        String userName = userPref.getString("userName","");

        blogVh.blogLocation.setText(allBlogResult.getDivision());
        blogVh.blogDate.setText(allBlogResult.getDate());
        blogVh.blogTitle.setText(allBlogResult.getTitle());


        Picasso.get().load(ApiURL.IMAGE_BASE +allBlogResult.getImage()).fit().centerInside().into(blogVh.blogImage);


        if(allBlogResult.getSlug().equals(userName)){
            blogVh.moreBlogButton.setVisibility(View.VISIBLE);
        } else {
            blogVh.moreBlogButton.setVisibility(View.GONE);
        }

        blogVh.cardViewBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(allBlogResult.getSlug().equals(userName)){
                    final Intent intent;
                    intent =  new Intent(context, YourBlogDetailsActivity.class);
                    intent.putExtra("yourBlogId",allBlogResult.getId());
                    context.startActivity(intent);
                } else {
                    final Intent intent;
                    intent =  new Intent(context, BlogDetailsActivity.class);
                    intent.putExtra("blogId",allBlogResult.getId());
                    context.startActivity(intent);
                }

            }
        });

        blogVh.moreBlogButton.setOnClickListener(v->{
            PopupMenu popupMenu = new PopupMenu(context,blogVh.moreBlogButton);
            popupMenu.inflate(R.menu.delete_post_or_blog_menu);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    if (item.getItemId() == R.id.delete_post) {
                        showDeleteBlogPopUp(String.valueOf(allBlogResult.getId()),position);
                    }

                    return false;
                }
            });
            popupMenu.show();
        });

        //set Animation in recyclerView Item
        blogVh.blogItemLayout.setAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_scale_animation));

    }

    protected static class BlogVh extends RecyclerView.ViewHolder {
        public RoundedImageView blogImage;
        public TextView blogLocation,blogDate,blogTitle;
        LinearLayout blogItemLayout;
        CardView cardViewBlog;
        ImageView moreBlogButton;

        public BlogVh(View itemView) {
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


    @Override
    public int getItemCount() {
        return allBlogResults == null ? 0 : allBlogResults.size();
    }

    @Override
    public int getItemViewType(int position) {

        return (position == allBlogResults.size() - 1 && isLoadingAdded) ? LOADING : ITEM;

    }


    public void add(AllBlogResult r) {
        allBlogResults.add(r);
        notifyItemInserted(allBlogResults.size() - 1);
    }

    public void addAll(List<AllBlogResult> moveResults) {
        for (AllBlogResult result : moveResults) {
            add(result);
        }
    }

    public void remove(AllBlogResult r) {
        int position = allBlogResults.indexOf(r);
        if (position > -1) {
            allBlogResults.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new AllBlogResult());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = allBlogResults.size() - 1;
        AllBlogResult result = getItem(position);

        if (result != null) {
            allBlogResults.remove(position);
            notifyItemRemoved(position);
        }
    }

    public AllBlogResult getItem(int position) {
        return allBlogResults.get(position);
    }


    public void showRetry(boolean show, @Nullable String errorMsg) {
        retryPageLoad = show;
        notifyItemChanged(allBlogResults.size() - 1);

        String errorMsg1;
        if (errorMsg != null) errorMsg1 = errorMsg;
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
        SharedPreferences userPref = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        String token = userPref.getString("token","");

        Call<DeleteBlogResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .deleteBlog("Token "+token,getId);

        call.enqueue(new Callback<DeleteBlogResponse>() {
            @Override
            public void onResponse(Call<DeleteBlogResponse> call, Response<DeleteBlogResponse> response) {
                if(response.isSuccessful()){
                    allBlogResults.remove(position);
                    notifyItemRemoved(position);
                    assert response.body() != null;
                    String success = response.body().getSuccess();
                    DynamicToast.makeSuccess(context, success).show();

                }else{
                    DynamicToast.makeError(context, "Something Wrong !!").show();
                }
            }

            @Override
            public void onFailure(Call<DeleteBlogResponse> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}