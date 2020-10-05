package com.anikrakib.tourday.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.anikrakib.tourday.Models.DistrictModelItem;
import com.anikrakib.tourday.Models.PostEvent;
import com.anikrakib.tourday.R;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.squareup.picasso.Picasso;

import java.util.List;


public class DistrictAdapter extends RecyclerView.Adapter<DistrictAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private List<DistrictModelItem> mData;
    private Context mContext;

    public DistrictAdapter(Context context,List<DistrictModelItem> Data) {
        mData = Data;
        mContext=context;
    }

    @NonNull
    @Override
    public DistrictAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.district_blog_item, viewGroup, false);
        DistrictAdapter.ViewHolder vh = new DistrictAdapter.ViewHolder(view);
        return vh;

    }

    @Override
    public void onBindViewHolder(@NonNull DistrictAdapter.ViewHolder viewHolder, int position) {
        viewHolder.setData(mData.get(position));

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        KenBurnsView kenBurnsView;
        TextView textView;

        public ViewHolder(View v) {
            super(v);

            kenBurnsView = v.findViewById(R.id.kbvDistrict);
            textView=v.findViewById(R.id.txtDistrict);
        }

        public void setData(DistrictModelItem districtModelItem){
            Picasso.get().load(districtModelItem.getImageUrl()).into(kenBurnsView);
            textView.setText(districtModelItem.getDistrictName());
        }
    }
}

//public class DistrictAdapter extends PagerAdapter {
//
//    private List<DistrictModelItem> models;
//    private LayoutInflater layoutInflater;
//    private Context context;
//
//    public DistrictAdapter(List<DistrictModelItem> models, Context context) {
//        this.models = models;
//        this.context = context;
//    }
//
//    @Override
//    public int getCount() {
//        return models.size();
//    }
//
//    @Override
//    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
//        return view.equals(object);
//    }
//
//    @NonNull
//    @Override
//    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
//        layoutInflater = LayoutInflater.from(context);
//        View view = layoutInflater.inflate(R.layout.district_blog_item, container, false);
//
//        ImageView imageView;
//        TextView title;
//
//        imageView = view.findViewById(R.id.image);
//        title = view.findViewById(R.id.title);
//
//        imageView.setImageResource(models.get(position).getImageUrl());
//        title.setText(models.get(position).getDistrictName());
//
//
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Intent intent = new Intent(context, DetailActivity.class);
////                intent.putExtra("param", models.get(position).getTitle());
////                context.startActivity(intent);
//                // finish();
//            }
//        });
//
//        container.addView(view, 0);
//        return view;
//    }
//
//    @Override
//    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//        container.removeView((View)object);
//    }
//}
