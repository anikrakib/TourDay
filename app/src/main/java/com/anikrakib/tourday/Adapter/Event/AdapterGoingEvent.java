package com.anikrakib.tourday.Adapter.Event;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.anikrakib.tourday.Models.Event.GoingEvent;
import com.anikrakib.tourday.R;

import java.util.List;

public class AdapterGoingEvent extends RecyclerView.Adapter<AdapterGoingEvent.MyViewHolder>{

    Context context ;
    List<GoingEvent> mData;


    public AdapterGoingEvent(Context context, List<GoingEvent> mData) {
        this.context = context;
        this.mData = mData;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.going_user_list_item, parent, false);
        return new MyViewHolder(v);


        }

    @Override
    public void onBindViewHolder( MyViewHolder myViewHolder, int i) {


        myViewHolder.categoryName.setText(mData.get(i).getCategoryName());


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {


        private TextView categoryName;


        public MyViewHolder(final View itemView) {

            super(itemView);
            categoryName = itemView.findViewById(R.id.goingUserName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //categoryItemClickListener.onCategoryClick(mData.get(getAdapterPosition()));



                }
            });

        }
    }
}
