package com.anikrakib.tourday.Adapter.Event;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anikrakib.tourday.Activity.Event.YourEventDetailsActivity;
import com.anikrakib.tourday.Models.Event.PostEvent;
import com.anikrakib.tourday.Models.Event.YourEventModel;
import com.anikrakib.tourday.R;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterYourEvent extends RecyclerView.Adapter<AdapterYourEvent.ViewHolder> {
    private LayoutInflater mInflater;
    private List<YourEventModel> mData;
    private Context mContext;

    public AdapterYourEvent(Context context,List<YourEventModel> Data) {
        mData = Data;
        mContext=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_edit_your_event_item, viewGroup, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        YourEventModel item = mData.get(position);
        viewHolder.txTitle.setText(item.getTitle());
        viewHolder.txtBody.setText(item.getDetails());
        viewHolder.eventLocation.setText(item.getLocation());
        viewHolder.date.setText(item.getDate());
        viewHolder.totalPrice.setText(String.valueOf(item.getCost()));

        Picasso.get().load("https://www.tourday.team/"+item.getEventImageUrl()).fit().centerInside().into(viewHolder.eventImage);


        viewHolder.yourEventLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent;
                intent =  new Intent(mContext, YourEventDetailsActivity.class);
                intent.putExtra("eventId",item.getEventId());
                intent.putExtra("eventTitle",item.getTitle());
                intent.putExtra("eventLocation",item.getLocation());
                intent.putExtra("eventDate",item.getDate());
                intent.putExtra("eventDetails",item.getDetails());
                intent.putExtra("eventPay1",item.getPay1());
                intent.putExtra("eventPay1Method",item.getPay1Method());
                intent.putExtra("eventPay2",item.getPay2());
                intent.putExtra("eventPay2Method",item.getPay2Method());
                intent.putExtra("eventImageUrl",item.getEventImageUrl());
                intent.putExtra("eventCapacity",item.getCapacity());
                intent.putExtra("eventCost",item.getCost());
                intent.putExtra("eventHostId",item.getHostId());
                intent.putExtra("eventTotalGoing",item.getTotalGoing());
                intent.putExtra("eventTotalPending",item.getTotalPending());
                intent.putExtra("list",item.getTotalPendingUserList());
                mContext.startActivity(intent);

            }
        });

        //set Animation in recyclerView Item
        viewHolder.yourEventLinearLayout.setAnimation(AnimationUtils.loadAnimation(mContext,R.anim.fade_scale_animation));

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txTitle, txtBody, date,eventLocation,totalPrice;
        public RoundedImageView eventImage;
        LinearLayout yourEventLinearLayout;


        public ViewHolder(View v) {
            super(v);
            txTitle = v.findViewById(R.id.titleEvent);
            txtBody = v.findViewById(R.id.eventDescription);
            date = v.findViewById(R.id.eventDate);
            eventLocation = v.findViewById(R.id.eventLocation);
            eventImage = v.findViewById(R.id.eventImage);
            totalPrice = v.findViewById(R.id.eventPrice);
            yourEventLinearLayout = v.findViewById(R.id.yourEventLinearLayout);
        }
    }
}
