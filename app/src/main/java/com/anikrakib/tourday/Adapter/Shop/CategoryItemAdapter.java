package com.anikrakib.tourday.Adapter.Shop;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.anikrakib.tourday.Activity.Profile.OthersUserProfile;
import com.anikrakib.tourday.Activity.Shop.ProductCategoryOrSearchActivity;
import com.anikrakib.tourday.Models.Shop.CategoryListItem;
import com.anikrakib.tourday.R;

import java.util.List;

public class CategoryItemAdapter extends RecyclerView.Adapter<CategoryItemAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private final List<CategoryListItem> mData;
    private final Context mContext;

    public CategoryItemAdapter(Context context, List<CategoryListItem> Data) {
        mData = Data;
        mContext=context;
    }

    @NonNull
    @Override
    public CategoryItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.category_item, viewGroup, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull CategoryItemAdapter.ViewHolder viewHolder, int position) {
        final CategoryListItem currentItem = mData.get(position);


        viewHolder.imageView.setImageResource(currentItem.getImageId());
        viewHolder.textView.setText(currentItem.getCategoryName());

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent;
                intent =  new Intent(mContext, ProductCategoryOrSearchActivity.class);
                if(currentItem.getCategoryName().equals("Gadget Accessories")){
                    intent.putExtra("keyword","Headphone Turbine Cable Watch Power Bank");
                }else {
                    intent.putExtra("keyword",currentItem.getCategoryName());
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;
        CardView cardView;

        public ViewHolder(View v) {
            super(v);

            imageView = v.findViewById(R.id.imgcategory);
            textView=v.findViewById(R.id.categoryName);
            cardView = v.findViewById(R.id.cardView);
        }



    }
}
