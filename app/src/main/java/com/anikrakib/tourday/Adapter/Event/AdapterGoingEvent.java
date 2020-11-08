package com.anikrakib.tourday.Adapter.Event;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anikrakib.tourday.Models.Event.GoingEvent;
import com.anikrakib.tourday.Models.Event.GoingUser;
import com.anikrakib.tourday.R;
import com.anikrakib.tourday.Utils.ApiURL;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterGoingEvent extends RecyclerView.Adapter<AdapterGoingEvent.MyViewHolder>{

    Context context ;
    List<GoingUser> mData;


    public AdapterGoingEvent(Context context, List<GoingUser> mData) {
        this.context = context;
        this.mData = mData;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.going_user_list_item, parent, false);
        return new MyViewHolder(v);


        }

    @Override
    public void onBindViewHolder( MyViewHolder myViewHolder, int i) {

        String userFullName = mData.get(i).getName();
        assert userFullName != null;
        String[] arr = userFullName.split(" ", 2);

        myViewHolder.categoryName.setText(arr[0]);
        Glide.with(context)
                .load(ApiURL.IMAGE_BASE+mData.get(i).getPicture())
                .placeholder(R.drawable.loading)
                .error(Glide.with(context)
                        .load("https://i.pinimg.com/originals/a7/46/df/a746dfd74e09d8c7cbcdfa7be02a6250.gif"))
                .transforms(new CenterCrop(),new RoundedCorners(16))
                .into(myViewHolder.goingUserImage);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView categoryName;
        private final CircleImageView goingUserImage;


        public MyViewHolder(final View itemView) {

            super(itemView);
            categoryName = itemView.findViewById(R.id.goingUserName);
            goingUserImage = itemView.findViewById(R.id.goingUserProfilePic);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //categoryItemClickListener.onCategoryClick(mData.get(getAdapterPosition()));



                }
            });

        }
    }
}
