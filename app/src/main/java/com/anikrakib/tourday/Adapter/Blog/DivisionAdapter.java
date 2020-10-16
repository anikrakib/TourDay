package com.anikrakib.tourday.Adapter.Blog;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.anikrakib.tourday.Activity.Blog.BlogDetailsActivity;
import com.anikrakib.tourday.Activity.Blog.DivisionBlogActivity;
import com.anikrakib.tourday.Models.Blog.BlogItem;
import com.anikrakib.tourday.Models.Blog.DivisionModelItem;
import com.anikrakib.tourday.R;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.squareup.picasso.Picasso;

import java.util.List;


public class DivisionAdapter extends RecyclerView.Adapter<DivisionAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private List<DivisionModelItem> mData;
    private Context mContext;

    public DivisionAdapter(Context context, List<DivisionModelItem> Data) {
        mData = Data;
        mContext=context;
    }

    @NonNull
    @Override
    public DivisionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.district_blog_item, viewGroup, false);
        DivisionAdapter.ViewHolder vh = new DivisionAdapter.ViewHolder(view);
        return vh;

    }

    @Override
    public void onBindViewHolder(@NonNull DivisionAdapter.ViewHolder viewHolder, int position) {
        final DivisionModelItem currentItem = mData.get(position);


        Picasso.get().load(currentItem.getImageUrl()).into(viewHolder.kenBurnsView);
        viewHolder.textView.setText(currentItem.getDistrictName());

        viewHolder.divisionBlogItemCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent;
                intent =  new Intent(mContext, DivisionBlogActivity.class);
                intent.putExtra("divisionName",currentItem.getDistrictName());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        KenBurnsView kenBurnsView;
        TextView textView;
        CardView divisionBlogItemCardView;

        public ViewHolder(View v) {
            super(v);

            kenBurnsView = v.findViewById(R.id.kbvDistrict);
            textView=v.findViewById(R.id.txtDistrict);
            divisionBlogItemCardView = v.findViewById(R.id.divisionBlogItemCardView);
        }



    }
}
