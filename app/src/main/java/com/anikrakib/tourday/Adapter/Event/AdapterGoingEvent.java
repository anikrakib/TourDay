package com.anikrakib.tourday.Adapter.Event;

import android.annotation.SuppressLint;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.anikrakib.tourday.Activity.Event.EventDetailsActivity;
import com.anikrakib.tourday.Activity.Event.YourEventDetailsActivity;
import com.anikrakib.tourday.Models.Event.AllEventResult;
import com.anikrakib.tourday.R;
import com.anikrakib.tourday.Utils.ApiURL;
import com.anikrakib.tourday.Utils.PaginationAdapterCallback;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

public class AdapterGoingEvent extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    // View Types
    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private List<AllEventResult> allEventResults;
    private Context context;

    private boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;

    private PaginationAdapterCallback mCallback;

    private String errorMsg;

    public AdapterGoingEvent(Context context) {
        this.context = context;
        allEventResults = new ArrayList<>();
    }

    public List<AllEventResult> getAllEventResults() {
        return allEventResults;
    }

    public void setEvent(List<AllEventResult> allEventResults) {
        this.allEventResults = allEventResults;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View viewItem = inflater.inflate(R.layout.going_event_list_item, parent, false);
        viewHolder = new AdapterGoingEvent.EventVH(viewItem);

        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        AllEventResult result = allEventResults.get(position);

        final AdapterGoingEvent.EventVH eventVH = (AdapterGoingEvent.EventVH) holder;

        SharedPreferences userPref = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        String userId = userPref.getString("id","");

        eventVH.eventTitle.setText(result.getTitle());
        eventVH.eventLocation.setText(result.getLocation());
        eventVH.eventDate.setText(result.getDate());
        if(Integer.parseInt(userId) == result.getHost()){
//            //eventVH.itemView.setVisibility(View.GONE);
//            eventVH.goingEventLayout.setVisibility(View.GONE);
            //remove(result);
            int a = result.getGoing().size()-1;
            if(a == 0){
                eventVH.totalGoing.setText("Only You Going");
            }else{
                eventVH.totalGoing.setText("You and "+(result.getGoing().size()-1)+" others going");
            }
        }

        // load event thumbnail
        Glide.with(context)
                .load(ApiURL.IMAGE_BASE+result.getImage()) // set the img book Url
                .placeholder(R.drawable.loading)
                .error(Glide.with(context)
                        .load("https://i.pinimg.com/originals/a7/46/df/a746dfd74e09d8c7cbcdfa7be02a6250.gif"))
                .transforms(new CenterCrop(),new RoundedCorners(16))
                .into(eventVH.eventImage); // destination path

        eventVH.goingEventLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
    }

    protected static class EventVH extends RecyclerView.ViewHolder {
        public TextView eventTitle, eventDate, eventLocation, totalGoing;
        public ImageButton shareEvent;
        public ImageView eventImage;
        public CardView goingEventLayout;

        public EventVH(View itemView) {
            super(itemView);

            eventImage = itemView.findViewById(R.id.eventImage);
            eventTitle = itemView.findViewById(R.id.eventTitle);
            eventDate = itemView.findViewById(R.id.eventDate);
            eventLocation = itemView.findViewById(R.id.eventLocation);
            totalGoing = itemView.findViewById(R.id.totalGoing);
            shareEvent = itemView.findViewById(R.id.shareEvent);
            goingEventLayout = itemView.findViewById(R.id.goingEventItemLayout);
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
}