package com.anikrakib.tourday.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anikrakib.tourday.Activity.EventDetailsActivity;
import com.anikrakib.tourday.Activity.YourEventDetailsActivity;
import com.anikrakib.tourday.Models.PostEvent;
import com.anikrakib.tourday.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class AdapterYourEvent extends RecyclerView.Adapter<AdapterYourEvent.ViewHolder> {
    private LayoutInflater mInflater;
    private List<PostEvent> mData;
    private Context mContext;

    public AdapterYourEvent(Context context,List<PostEvent> Data) {
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

        PostEvent item = mData.get(position);
        viewHolder.txTitle.setText(item.Title);
        viewHolder.txtBody.setText(item.Body);
        viewHolder.eventLocation.setText(item.location);
//        viewHolder.startTime.setText(item.startTime);
//        viewHolder.endTime.setText(item.endTime);
        viewHolder.startDate.setText(item.startDate);
        viewHolder.endDate.setText(item.endDate);
        viewHolder.eventImage.setImageResource(item.imageUrl);

        viewHolder.yourEventLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent;
                intent =  new Intent(mContext, YourEventDetailsActivity.class);
                mContext.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txTitle, txtBody, startDate, endDate, startTime, endTime, eventLocation;
        public RoundedImageView eventImage;
        LinearLayout yourEventLinearLayout;


        public ViewHolder(View v) {
            super(v);
            txTitle = v.findViewById(R.id.titleEvent);
            txtBody = v.findViewById(R.id.eventDescription);
            startDate = v.findViewById(R.id.eventStartDate);
            endDate = v.findViewById(R.id.eventEndDate);
//            startTime = v.findViewById(R.id.eventStartTime);
//            endTime = v.findViewById(R.id.eventEndTime);
            eventLocation = v.findViewById(R.id.eventLocation);
            eventImage = v.findViewById(R.id.eventImage);
            yourEventLinearLayout = v.findViewById(R.id.yourEventLinearLayout);
        }
    }
}
