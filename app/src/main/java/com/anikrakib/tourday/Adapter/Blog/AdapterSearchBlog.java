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
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.anikrakib.tourday.Activity.Blog.BlogDetailsActivity;
import com.anikrakib.tourday.Activity.Blog.YourBlogDetailsActivity;
import com.anikrakib.tourday.Models.Blog.BlogItem;
import com.anikrakib.tourday.Models.Blog.DeleteBlogResponse;
import com.anikrakib.tourday.Models.Blog.SearchBlogHistory;
import com.anikrakib.tourday.R;
import com.anikrakib.tourday.WebService.RetrofitClient;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class AdapterSearchBlog extends RecyclerView.Adapter<AdapterSearchBlog.BlogViewHolder> {

    private final Context mContext;
    private final ArrayList<BlogItem> mBlogItemList;
    Dialog myDialog;
    SearchView blogSearchView;
    private List<SearchBlogHistory> searchBlogHistories;
    AdapterSearchBlogHistory adapterSearchBlogHistory;

    public AdapterSearchBlog(Context mContext, ArrayList<BlogItem> mBlogItemList) {
        this.mContext = mContext;
        this.mBlogItemList = mBlogItemList;
    }

    public AdapterSearchBlog(Context mContext, ArrayList<BlogItem> mBlogItemList, SearchView blogSearchView, List<SearchBlogHistory> searchBlogHistories) {
        this.mContext = mContext;
        this.mBlogItemList = mBlogItemList;
        this.blogSearchView = blogSearchView;
        this.searchBlogHistories = searchBlogHistories;
    }

    @NonNull
    @Override
    public BlogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.list_search_item, parent, false);
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
        holder.blogAuthor.setText(author);
        holder.blogTitle.setText(title);

        holder.blogItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // save value in sharedPref list  when user click blogSearch list item
                searchBlogHistories.add(new SearchBlogHistory(blogSearchView.getQuery().toString()));
                writeListInPref(mContext, searchBlogHistories);
//                Collections.reverse(searchBlogHistories);
//                if(searchBlogHistories!= null){
//                    adapterSearchBlogHistory.setTaskModelList(searchBlogHistories);
//                }

                if(author.equals(userName)){
                    final Intent intent;
                    intent =  new Intent(mContext, YourBlogDetailsActivity.class);
                    intent.putExtra("yourBlogId",Integer.parseInt(id));
                    mContext.startActivity(intent);
                } else {
                    final Intent intent;
                    intent =  new Intent(mContext, BlogDetailsActivity.class);
                    intent.putExtra("blogId",Integer.parseInt(id));
                    mContext.startActivity(intent);
                }
            }
        });

        Picasso.get().load(imageUrl).fit().centerInside().into(holder.blogImage);

    }

    @Override
    public int getItemCount() {
        return mBlogItemList.size();
    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder {

        public RoundedImageView blogImage;
        public TextView blogLocation,blogDate,blogTitle,blogAuthor;
        LinearLayout blogItemLayout;


        public BlogViewHolder(@NonNull View itemView) {
            super(itemView);

            blogImage = itemView.findViewById(R.id.image);
            blogDate = itemView.findViewById(R.id.date);
            blogLocation = itemView.findViewById(R.id.location);
            blogTitle = itemView.findViewById(R.id.title);
            blogAuthor = itemView.findViewById(R.id.author);
            blogItemLayout = itemView.findViewById(R.id.linearLayout);

        }
    }

    public void writeListInPref(Context context, List<SearchBlogHistory> list) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(list);
        SharedPreferences user = context.getSharedPreferences("user", MODE_PRIVATE);
        String userName = user.getString("userName","");
        SharedPreferences userPref = context.getSharedPreferences(userName, MODE_PRIVATE);
        SharedPreferences.Editor editor = userPref.edit();
        editor.putString("searchHistoryKey", jsonString);
        editor.apply();
    }
}