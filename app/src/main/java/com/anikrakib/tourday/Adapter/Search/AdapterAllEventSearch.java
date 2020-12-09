package com.anikrakib.tourday.Adapter.Search;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.anikrakib.tourday.Activity.Event.EventDetailsActivity;
import com.anikrakib.tourday.Activity.Event.YourEventDetailsActivity;
import com.anikrakib.tourday.Models.Event.AllEventResult;
import com.anikrakib.tourday.Models.Profile.Profile;
import com.anikrakib.tourday.R;
import com.anikrakib.tourday.RoomDatabse.FavouriteEventDatabaseTable;
import com.anikrakib.tourday.RoomDatabse.MyDatabase;
import com.anikrakib.tourday.Utils.ApiURL;
import com.anikrakib.tourday.Utils.PaginationAdapterCallback;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

public class AdapterAllEventSearch extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // View Types
    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private List<AllEventResult> allEventResults;
    private List<FavouriteEventDatabaseTable> favouriteEventDatabaseTables;
    private Context context;
    private CardView empty;
    TextView textView,textView2;

    private boolean isLoadingAdded = false;
    private boolean isFavouriteActivity = false;

    private PaginationAdapterCallback mCallback;

    private String errorMsg;

    public AdapterAllEventSearch(Context context) {
        this.context = context;
        allEventResults = new ArrayList<>();
    }

    public AdapterAllEventSearch(List<FavouriteEventDatabaseTable> favouriteEventDatabaseTables, Context context, boolean isFavouriteActivity, CardView empty, TextView textView, TextView textView2) {
        this.favouriteEventDatabaseTables = favouriteEventDatabaseTables;
        this.context = context;
        this.isFavouriteActivity = isFavouriteActivity;
        this.empty = empty;
        this.textView = textView;
        this.textView2 = textView2;
        notifyDataSetChanged();
    }

    public List<AllEventResult> getAllProfileResults() {
        return allEventResults;
    }

    public void setMovies(List<AllEventResult> allProfileResults) {
        this.allEventResults = allProfileResults;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View viewItem = inflater.inflate(R.layout.list_item_event_search_item, parent, false);
        viewHolder = new AllEventVH(viewItem);

        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        SharedPreferences userPref = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        String userId = userPref.getString("id",String.valueOf(0));
        final AllEventVH allEventVH = (AllEventVH) holder;
        MyDatabase myDatabase = MyDatabase.getInstance(context);

        if(isFavouriteActivity){
            FavouriteEventDatabaseTable favouriteEventDatabaseTable = favouriteEventDatabaseTables.get(position);

            allEventVH.eventTitle.setText(favouriteEventDatabaseTable.getName());
            allEventVH.eventLocation.setText(favouriteEventDatabaseTable.getLocation());
            allEventVH.eventDate.setText(favouriteEventDatabaseTable.getDate());
            allEventVH.eventGoing.setText(favouriteEventDatabaseTable.getPrice()+"");
            allEventVH.bLike.setImageResource(R.drawable.ic_bookmarked);

            Glide.with(context)
                    .load(ApiURL.IMAGE_BASE+favouriteEventDatabaseTable.getImage())
                    .placeholder(R.drawable.loading)
                    .error(Glide.with(context)
                            .load("https://i.pinimg.com/originals/a7/46/df/a746dfd74e09d8c7cbcdfa7be02a6250.gif"))
                    .transforms(new CenterCrop(),new RoundedCorners(16))
                    .into(allEventVH.eventImage);

            allEventVH.bLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myDatabase.favouriteEventDatabaseDao().delete(favouriteEventDatabaseTable);
                    favouriteEventDatabaseTables.remove(position);
                    notifyItemRemoved(position);
                    if(favouriteEventDatabaseTables.isEmpty()){
                        empty.setVisibility(View.VISIBLE);
                        textView2.setText("Tap The Bookmark Icon And Save it Bookmark List");
                        textView.setText("No Bookmarked Item Yet");
                    }else {
                        empty.setVisibility(View.GONE);
                    }
                }
            });

            allEventVH.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(Integer.parseInt(userId) == Integer.parseInt(favouriteEventDatabaseTable.getHost())){
                        Intent intent;
                        intent =  new Intent(context, YourEventDetailsActivity.class);
                        intent.putExtra("eventId",favouriteEventDatabaseTable.getId());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }else{
                        Intent intent;
                        intent =  new Intent(context, EventDetailsActivity.class);
                        intent.putExtra("eventId",favouriteEventDatabaseTable.getId());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                }
            });
        }else {

            AllEventResult allEventResult = allEventResults.get(position);


            allEventVH.eventTitle.setText(allEventResult.getTitle());
            allEventVH.eventLocation.setText(allEventResult.getLocation());
            allEventVH.eventDate.setText(allEventResult.getDate());
            allEventVH.eventGoing.setText(allEventResult.getGoing().size()+" People Going");

            Glide.with(context)
                    .load(ApiURL.IMAGE_BASE+allEventResult.getImage())
                    .placeholder(R.drawable.loading)
                    .error(Glide.with(context)
                            .load("https://i.pinimg.com/originals/a7/46/df/a746dfd74e09d8c7cbcdfa7be02a6250.gif"))
                    .transforms(new CenterCrop(),new RoundedCorners(16))
                    .into(allEventVH.eventImage);

            // check favourite or not
            if (myDatabase.favouriteEventDatabaseDao().isAddToCart(allEventResult.getId()) == 1){
                allEventVH.bLike.setImageResource(R.drawable.ic_bookmarked);
            }else {
                allEventVH.bLike.setImageResource(R.drawable.ic_un_bookmark);
            }

            allEventVH.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(Integer.parseInt(userId) == allEventResult.getHost()){
                        Intent intent;
                        intent =  new Intent(context, YourEventDetailsActivity.class);
                        intent.putExtra("eventId",allEventResult.getId());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }else{
                        Intent intent;
                        intent =  new Intent(context, EventDetailsActivity.class);
                        intent.putExtra("eventId",allEventResult.getId());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                }
            });

        }
    }

    protected static class AllEventVH extends RecyclerView.ViewHolder {
        RoundedImageView eventImage;
        TextView eventDate,eventTitle,eventGoing,eventLocation;
        ImageButton bLike;
        CardView cardView;

        public AllEventVH(View itemView) {
            super(itemView);
            eventDate = itemView.findViewById(R.id.eventDate);
            eventTitle = itemView.findViewById(R.id.eventTitle);
            eventGoing = itemView.findViewById(R.id.eventTotalGoing);
            eventImage = itemView.findViewById(R.id.eventImage);
            eventLocation = itemView.findViewById(R.id.eventLocation);
            cardView = itemView.findViewById(R.id.searchUserCardView);
            bLike = itemView.findViewById(R.id.favouriteItemImageButton);

        }
    }


    @Override
    public int getItemCount() {
        if(isFavouriteActivity){
            return favouriteEventDatabaseTables == null ? 0 : favouriteEventDatabaseTables.size();
        }else{
            return allEventResults == null ? 0 : allEventResults.size();
        }
    }

    @Override
    public int getItemViewType(int position) {

        if(isFavouriteActivity){
            return (position == favouriteEventDatabaseTables.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
        }else{
            return (position == allEventResults.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
        }

    }


    public void add(AllEventResult r) {
        allEventResults.add(r);
        notifyItemInserted(allEventResults.size() - 1);
    }

    public void addAll(List<AllEventResult> profileResults) {
        for (AllEventResult profile : profileResults) {
            add(profile);
        }
    }

    public void remove(AllEventResult r) {
        int position = allEventResults.indexOf(r);
        if (position > -1) {
            allEventResults.remove(position);
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
        add(new AllEventResult());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = allEventResults.size() - 1;
        AllEventResult profile = getItem(position);

        if (profile != null) {
            allEventResults.remove(position);
            notifyItemRemoved(position);
        }
    }

    public AllEventResult getItem(int position) {
        return allEventResults.get(position);
    }


    public void showRetry(boolean show, @Nullable String errorMsg) {
       // retryPageLoad = show;
        notifyItemChanged(allEventResults.size() - 1);

        if (errorMsg != null) this.errorMsg = errorMsg;
    }
}