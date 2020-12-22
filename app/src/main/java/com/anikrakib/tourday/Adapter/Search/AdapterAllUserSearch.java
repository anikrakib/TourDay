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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.anikrakib.tourday.Activity.Blog.BlogDetailsActivity;
import com.anikrakib.tourday.Activity.Blog.YourBlogDetailsActivity;
import com.anikrakib.tourday.Activity.Profile.MyProfileActivity;
import com.anikrakib.tourday.Activity.Profile.OthersUserProfile;
import com.anikrakib.tourday.Models.Event.AllEventResult;
import com.anikrakib.tourday.Models.Profile.Profile;
import com.anikrakib.tourday.R;
import com.anikrakib.tourday.Utils.ApiURL;
import com.anikrakib.tourday.Utils.PaginationAdapterCallback;
import com.anikrakib.tourday.Utils.TapToProgress.Circle;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterAllUserSearch extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // View Types
    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private List<Profile> allProfileResults;
    private Context context;

    private boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;

    private PaginationAdapterCallback mCallback;

    private String errorMsg;

    public AdapterAllUserSearch(Context context) {
        this.context = context;
        allProfileResults = new ArrayList<>();
    }

    public List<Profile> getAllProfileResults() {
        return allProfileResults;
    }

    public void setMovies(List<Profile> allProfileResults) {
        this.allProfileResults = allProfileResults;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View viewItem = inflater.inflate(R.layout.list_item_search_user, parent, false);
        viewHolder = new UserProfileVH(viewItem);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Profile profile = allProfileResults.get(position);
        SharedPreferences userPref = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        String currentUserName = userPref.getString("userName","");

        final UserProfileVH userProfileVH = (UserProfileVH) holder;

        userProfileVH.userFullNAme.setText(profile.getName());

        if(profile.getCity() != null){
            userProfileVH.userLocation.setText(profile.getCity());
        }else{
            userProfileVH.locationLayout.setVisibility(View.GONE);
        }

        userProfileVH.userEmail.setText(profile.getEmail());
        Glide.with(context)
                .load(ApiURL.IMAGE_BASE+profile.getPicture())
                .placeholder(R.drawable.loading)
                .error(Glide.with(context)
                        .load("https://i.pinimg.com/originals/a7/46/df/a746dfd74e09d8c7cbcdfa7be02a6250.gif"))
                .transforms(new CenterCrop(),new RoundedCorners(16))
                .into(userProfileVH.userProfilePic);

        userProfileVH.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentUserName.equals(profile.getUsername())){
                    final Intent intent;
                    intent =  new Intent(context, MyProfileActivity.class);
                    //intent.putExtra("userName",profile.getUsername());
                    context.startActivity(intent);
                }else{
                    final Intent intent;
                    intent =  new Intent(context, OthersUserProfile.class);
                    intent.putExtra("userName",profile.getUsername());
                    context.startActivity(intent);
                }
            }
        });

    }

    protected static class UserProfileVH extends RecyclerView.ViewHolder {
        CircleImageView userProfilePic;
        TextView userFullNAme,userEmail,userLocation;
        LinearLayout locationLayout;
        CardView cardView;

        public UserProfileVH(View itemView) {
            super(itemView);
            userEmail = itemView.findViewById(R.id.searchUserEmail);
            userProfilePic = itemView.findViewById(R.id.searchUSerProfilePic);
            userLocation = itemView.findViewById(R.id.searchUserLocation);
            userFullNAme = itemView.findViewById(R.id.searchUserFullName);
            locationLayout = itemView.findViewById(R.id.locationLayout);
            cardView = itemView.findViewById(R.id.searchUserCardView);

        }
    }


    @Override
    public int getItemCount() {
        return allProfileResults == null ? 0 : allProfileResults.size();
    }

    @Override
    public int getItemViewType(int position) {

        return (position == allProfileResults.size() - 1 && isLoadingAdded) ? LOADING : ITEM;

    }


    public void add(Profile r) {
        allProfileResults.add(r);
        notifyItemInserted(allProfileResults.size() - 1);
    }

    public void addAll(List<Profile> profileResults) {
        for (Profile profile : profileResults) {
            add(profile);
        }
    }

    public void remove(Profile r) {
        int position = allProfileResults.indexOf(r);
        if (position > -1) {
            allProfileResults.remove(position);
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
        add(new Profile());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = allProfileResults.size() - 1;
        Profile profile = getItem(position);

        if (profile != null) {
            allProfileResults.remove(position);
            notifyItemRemoved(position);
        }
    }

    public Profile getItem(int position) {
        return allProfileResults.get(position);
    }


    public void showRetry(boolean show, @Nullable String errorMsg) {
        retryPageLoad = show;
        notifyItemChanged(allProfileResults.size() - 1);

        if (errorMsg != null) this.errorMsg = errorMsg;
    }
}