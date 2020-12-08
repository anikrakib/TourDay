package com.anikrakib.tourday.Adapter.Search;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.anikrakib.tourday.Activity.Blog.YourBlogDetailsActivity;
import com.anikrakib.tourday.Activity.Shop.ProductDetails;
import com.anikrakib.tourday.Models.Shop.ProductResult;
import com.anikrakib.tourday.R;
import com.anikrakib.tourday.Utils.ApiURL;
import com.anikrakib.tourday.Utils.PaginationAdapterCallback;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import java.util.ArrayList;
import java.util.List;

public class AdapterAllProductSearch extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // View Types
    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private List<ProductResult> productResults;
    private Context context;

    private boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;

    private PaginationAdapterCallback mCallback;

    private String errorMsg;

    public AdapterAllProductSearch(Context context) {
        this.context = context;
        productResults = new ArrayList<>();
    }

    public List<ProductResult> getAllProductResults() {
        return productResults;
    }

    public void setMovies(List<ProductResult> productResults) {
        this.productResults = productResults;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View viewItem = inflater.inflate(R.layout.list_shop_product_search_item, parent, false);
        viewHolder = new ProductVH(viewItem);

        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ProductResult productResult = productResults.get(position);

        final ProductVH productVH = (ProductVH) holder;
        SharedPreferences userPref = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        String userId = userPref.getString("id",String.valueOf(0));

        productVH.productPrice.setText("৳ "+productResult.getPrice());
        productVH.productName.setText(productResult.getName());
        Glide.with(context)
                .load(ApiURL.IMAGE_BASE+productResult.getImg())
                .placeholder(R.drawable.loading)
                .error(Glide.with(context)
                        .load("https://i.pinimg.com/originals/a7/46/df/a746dfd74e09d8c7cbcdfa7be02a6250.gif"))
                .transforms(new CenterCrop(),new RoundedCorners(16))
                .into(productVH.productImage);
        if(productResult.getDigital()){
            productVH.productStockOrNot.setBackgroundResource(R.drawable.out_stock_bg);
            productVH.productStockOrNot.setText("Out of Stock");
        }else{
            productVH.productStockOrNot.setText("In Stock");
            productVH.productStockOrNot.setBackgroundResource(R.drawable.in_stock_bg);
        }

        productVH.productLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent;
                intent =  new Intent(context, ProductDetails.class);
                intent.putExtra("name",productResult.getName());
                intent.putExtra("imageUrl",productResult.getImg());
                intent.putExtra("price",productResult.getPrice());
                intent.putExtra("stock",productResult.getDigital());
                intent.putExtra("category",productResult.getProductType());
                intent.putExtra("qty","0");
                intent.putExtra("details",R.string.bio);
                context.startActivity(intent);
            }
        });
    }

    protected static class ProductVH extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName,productPrice,addToCart,buyNow,productStockOrNot;
        CardView productLayout;

        public ProductVH(View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            addToCart = itemView.findViewById(R.id.addToCart);
            buyNow = itemView.findViewById(R.id.buyNow);
            productLayout = itemView.findViewById(R.id.productItemLayout);
            productStockOrNot = itemView.findViewById(R.id.productStockORNot);
        }
    }


    @Override
    public int getItemCount() {
        return productResults == null ? 0 : productResults.size();
    }

    @Override
    public int getItemViewType(int position) {

        return (position == productResults.size() - 1 && isLoadingAdded) ? LOADING : ITEM;

    }


    public void add(ProductResult r) {
        productResults.add(r);
        notifyItemInserted(productResults.size() - 1);
    }

    public void addAll(List<ProductResult> profileResults) {
        for (ProductResult productResult : profileResults) {
            add(productResult);
        }
    }

    public void remove(ProductResult r) {
        int position = productResults.indexOf(r);
        if (position > -1) {
            productResults.remove(position);
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
        add(new ProductResult());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = productResults.size() - 1;
        ProductResult productResult = getItem(position);

        if (productResult != null) {
            productResults.remove(position);
            notifyItemRemoved(position);
        }
    }

    public ProductResult getItem(int position) {
        return productResults.get(position);
    }


    public void showRetry(boolean show, @Nullable String errorMsg) {
        retryPageLoad = show;
        notifyItemChanged(productResults.size() - 1);

        if (errorMsg != null) this.errorMsg = errorMsg;
    }
}