package com.anikrakib.tourday.Adapter.Search;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.anikrakib.tourday.Activity.Blog.BlogDetailsActivity;
import com.anikrakib.tourday.Activity.Blog.YourBlogDetailsActivity;
import com.anikrakib.tourday.Models.Blog.AllBlogResponse;
import com.anikrakib.tourday.Models.Blog.AllBlogResult;
import com.anikrakib.tourday.Models.Blog.SearchBlogHistory;
import com.anikrakib.tourday.Models.Event.AllEventResult;
import com.anikrakib.tourday.Models.Profile.Profile;
import com.anikrakib.tourday.R;
import com.anikrakib.tourday.Utils.ApiURL;
import com.anikrakib.tourday.Utils.PaginationAdapterCallback;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterAllBlogSearch extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // View Types
    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private List<AllBlogResult> allBlogResults;
    private Context context;

    private boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;

    private PaginationAdapterCallback mCallback;

    private String errorMsg;

    public AdapterAllBlogSearch(Context context) {
        this.context = context;
        allBlogResults = new ArrayList<>();
    }

    public List<AllBlogResult> getAllProfileResults() {
        return allBlogResults;
    }

    public void setBlog(List<AllBlogResult> allBlogResults) {
        this.allBlogResults = allBlogResults;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View viewItem = inflater.inflate(R.layout.list_search_item, parent, false);
        viewHolder = new BlogVH(viewItem);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        AllBlogResult allBlogResult = allBlogResults.get(position);

        final BlogVH blogVH = (BlogVH) holder;
        SharedPreferences userPref = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        String userName = userPref.getString("userName","");

        blogVH.title.setText(allBlogResult.getTitle());
        blogVH.location.setText(allBlogResult.getDivision());
        blogVH.author.setText(allBlogResult.getSlug());
        blogVH.date.setText(allBlogResult.getDate());
        Glide.with(context)
                .load(allBlogResult.getImage())
                .placeholder(R.drawable.loading)
                .error(Glide.with(context)
                        .load("https://i.pinimg.com/originals/a7/46/df/a746dfd74e09d8c7cbcdfa7be02a6250.gif"))
                .transforms(new CenterCrop(),new RoundedCorners(16))
                .into(blogVH.blogImage);

        blogVH.linearLayout.setOnClickListener(new View.OnClickListener() {
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

    }

    protected static class BlogVH extends RecyclerView.ViewHolder {
        RoundedImageView blogImage;
        TextView title,author,location,date;
        LinearLayout linearLayout;

        public BlogVH(View itemView) {
            super(itemView);
            blogImage = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            location = itemView.findViewById(R.id.location);
            author = itemView.findViewById(R.id.author);
            date = itemView.findViewById(R.id.date);
            linearLayout = itemView.findViewById(R.id.linearLayout);
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

    public void addAll(List<AllBlogResult> allBlogResults) {
        for (AllBlogResult allBlogResult : allBlogResults) {
            add(allBlogResult);
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
        AllBlogResult allBlogResult = getItem(position);

        if (allBlogResult != null) {
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

        if (errorMsg != null) this.errorMsg = errorMsg;
    }
}