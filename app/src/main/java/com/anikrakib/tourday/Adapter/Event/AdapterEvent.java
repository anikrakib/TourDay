package com.anikrakib.tourday.Adapter.Event;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anikrakib.tourday.Activity.Event.EventDetailsActivity;
import com.anikrakib.tourday.Models.Event.PostEvent;
import com.anikrakib.tourday.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class AdapterEvent  extends RecyclerView.Adapter<AdapterEvent.ViewHolder> {
    private LayoutInflater mInflater;
    private List<PostEvent> mData;
    private Context mContext;


    public AdapterEvent(Context context,List<PostEvent> Data) {
        mData = Data;
        mContext=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_event_item, viewGroup, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        PostEvent item = mData.get(position);
        viewHolder.txTitle.setText(item.Title);
        viewHolder.txtBody.setText(item.Body);
        viewHolder.eventLocation.setText(item.location);
        viewHolder.startDate.setText(item.startDate);
        viewHolder.endDate.setText(item.endDate);
        viewHolder.eventImage.setImageResource(item.imageUrl);
        // check if it was liked or no
        if (item.like) {
            viewHolder.bLike.setImageResource(R.drawable.ic_like);
        } else {
            viewHolder.bLike.setImageResource(R.drawable.ic_unlike);
        }

        viewHolder.linearLayOutEventItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent;
                intent =  new Intent(mContext, EventDetailsActivity.class);
                mContext.startActivity(intent);

            }
        });
        //set Animation in recyclerView Item
        viewHolder.linearLayOutEventItem.setAnimation(AnimationUtils.loadAnimation(mContext,R.anim.fade_scale_animation));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txTitle, txtBody, startDate, endDate,eventLocation;
        public ImageButton bLike;
        public RoundedImageView eventImage;
        LinearLayout linearLayOutEventItem;


        public ViewHolder(View v) {
            super(v);
            txTitle = v.findViewById(R.id.titleEvent);
            txtBody = v.findViewById(R.id.eventDescription);
            startDate = v.findViewById(R.id.eventStartDate);
            endDate = v.findViewById(R.id.eventEndDate);
            bLike = v.findViewById(R.id.eventInterestedLikeImage);
            eventLocation = v.findViewById(R.id.eventLocation);
            eventImage = v.findViewById(R.id.eventImage);
            linearLayOutEventItem = v.findViewById(R.id.linearLayOutEventItem);

        }
    }
}
