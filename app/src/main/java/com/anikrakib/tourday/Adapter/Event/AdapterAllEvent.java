package com.anikrakib.tourday.Adapter.Event;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.anikrakib.tourday.Activity.Event.EventDetailsActivity;
import com.anikrakib.tourday.Activity.Event.YourEventDetailsActivity;
import com.anikrakib.tourday.Activity.Profile.ChangePasswordActivity;
import com.anikrakib.tourday.Models.Event.AllEventResult;
import com.anikrakib.tourday.R;
import com.anikrakib.tourday.RoomDatabse.FavouriteEventDatabaseTable;
import com.anikrakib.tourday.RoomDatabse.MyDatabase;
import com.anikrakib.tourday.Utils.ApiURL;
import com.anikrakib.tourday.Utils.PaginationAdapterCallback;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.google.android.material.snackbar.Snackbar;
import com.kishandonga.csbx.CustomSnackbar;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

public class AdapterAllEvent extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // View Types
    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private List<AllEventResult> allEventResults;
    private Context context;

    private boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;

    private PaginationAdapterCallback mCallback;

    private String errorMsg;

    public AdapterAllEvent(Context context) {
        this.context = context;
        allEventResults = new ArrayList<>();
    }

    public List<AllEventResult> getAllEventResults() {
        return allEventResults;
    }

    public void setMovies(List<AllEventResult> allEventResults) {
        this.allEventResults = allEventResults;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View viewItem = inflater.inflate(R.layout.list_event_item, parent, false);
        viewHolder = new EventVH(viewItem);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        AllEventResult result = allEventResults.get(position);
        MyDatabase myDatabase = MyDatabase.getInstance(context);

        final EventVH eventVH = (EventVH) holder;
        SharedPreferences userPref = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        String userId = userPref.getString("id",String.valueOf(0));

        // check favourite or not
        if (myDatabase.favouriteEventDatabaseDao().isAddToCart(result.getId()) == 1){
            eventVH.bLike.setImageResource(R.drawable.ic_bookmarked);
        }else {
            eventVH.bLike.setImageResource(R.drawable.ic_un_bookmark);
        }


        eventVH.txTitle.setText(result.getTitle());
        eventVH.txtBody.setText(result.getDetails());
        eventVH.eventLocation.setText(result.getLocation());
        eventVH.eventDate.setText(result.getDate());
        eventVH.eventCost.setText(String.valueOf(result.getCost()));

        // load event thumbnail
        Glide.with(context)
                .load(ApiURL.IMAGE_BASE+result.getImage()) // set the img book Url
                .placeholder(R.drawable.loading)
                .error(Glide.with(context)
                        .load("https://i.pinimg.com/originals/a7/46/df/a746dfd74e09d8c7cbcdfa7be02a6250.gif"))
                .transforms(new CenterCrop(),new RoundedCorners(16))
                .into(eventVH.eventImage); // destination path

        eventVH.linearLayOutEventItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assert userId != null;
                if(Integer.parseInt(userId) == result.getHost()){
                    Intent intent;
                    intent =  new Intent(context, YourEventDetailsActivity.class);
                    intent.putExtra("eventId",result.getId());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }else{
                    Intent intent;
                    intent =  new Intent(context, EventDetailsActivity.class);
                    intent.putExtra("eventId",result.getId());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
        });

        eventVH.bLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventVH.bLike.setImageResource(R.drawable.ic_bookmarked);
                FavouriteEventDatabaseTable favouriteEventDatabaseTable = new FavouriteEventDatabaseTable();
                favouriteEventDatabaseTable.setId(result.getId());
                favouriteEventDatabaseTable.setImage(result.getImage());
                favouriteEventDatabaseTable.setName(result.getTitle());
                favouriteEventDatabaseTable.setDate(result.getDate());
                favouriteEventDatabaseTable.setLocation(result.getLocation());
                favouriteEventDatabaseTable.setPrice(String.valueOf(result.getCost()));
                favouriteEventDatabaseTable.setHost(String.valueOf(result.getHost()));

                if (myDatabase.favouriteEventDatabaseDao().isAddToCart(result.getId())!=1){
                    myDatabase.favouriteEventDatabaseDao().insert(favouriteEventDatabaseTable);
                    snackBar("Event Bookmarked ",R.color.white);
                }else {
                    snackBar("It Already Bookmarked!",R.color.white);
                }
            }
        });

        //set Animation in recyclerView Item
        eventVH.linearLayOutEventItem.setAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_scale_animation));

    }

    protected static class EventVH extends RecyclerView.ViewHolder {
        public TextView txTitle, txtBody, eventDate, eventLocation, eventCost;
        public ImageButton bLike;
        public RoundedImageView eventImage;
        LinearLayout linearLayOutEventItem;

        public EventVH(View itemView) {
            super(itemView);

            eventImage = itemView.findViewById(R.id.eventImage);
            txTitle = itemView.findViewById(R.id.titleEvent);
            txtBody = itemView.findViewById(R.id.eventDescription);
            eventDate = itemView.findViewById(R.id.eventDate);
            eventCost = itemView.findViewById(R.id.eventCost);
            bLike = itemView.findViewById(R.id.eventInterestedLikeImage);
            eventLocation = itemView.findViewById(R.id.eventLocation);
            eventImage = itemView.findViewById(R.id.eventImage);
            linearLayOutEventItem = itemView.findViewById(R.id.linearLayOutEventItem);
        }
    }


    @Override
    public int getItemCount() {
        return allEventResults == null ? 0 : allEventResults.size();
    }

    @Override
    public int getItemViewType(int position) {

        return (position == allEventResults.size() - 1 && isLoadingAdded) ? LOADING : ITEM;

    }


    public void add(AllEventResult r) {
        allEventResults.add(r);
        notifyItemInserted(allEventResults.size() - 1);
    }

    public void addAll(List<AllEventResult> moveResults) {
        for (AllEventResult result : moveResults) {
            add(result);
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
        AllEventResult result = getItem(position);

        if (result != null) {
            allEventResults.remove(position);
            notifyItemRemoved(position);
        }
    }

    public AllEventResult getItem(int position) {
        return allEventResults.get(position);
    }


    public void showRetry(boolean show, @Nullable String errorMsg) {
        retryPageLoad = show;
        notifyItemChanged(allEventResults.size() - 1);

        if (errorMsg != null) this.errorMsg = errorMsg;
    }
    public void snackBar(String text,int color){
        CustomSnackbar sb = new CustomSnackbar(context);
        sb.message(text);
        sb.padding(15);
        sb.textColorRes(color);
        sb.backgroundColorRes(R.color.colorPrimaryDark);
        sb.cornerRadius(15);
        sb.duration(Snackbar.LENGTH_LONG);
        sb.show();
    }
}