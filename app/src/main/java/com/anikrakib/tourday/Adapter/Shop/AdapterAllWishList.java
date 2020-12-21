package com.anikrakib.tourday.Adapter.Shop;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.anikrakib.tourday.Models.Shop.ProductResult;
import com.anikrakib.tourday.R;
import com.anikrakib.tourday.RoomDatabse.FavouriteEventDatabaseTable;
import com.anikrakib.tourday.RoomDatabse.MyDatabase;
import com.anikrakib.tourday.RoomDatabse.ProductWishListDatabaseTable;
import com.anikrakib.tourday.Utils.ApiURL;
import com.anikrakib.tourday.WebService.RetrofitClient;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import org.w3c.dom.Text;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterAllWishList extends RecyclerView.Adapter<AdapterAllWishList.ViewHolder> {

    private List<ProductWishListDatabaseTable> productWishListDatabaseTables;
    private Context context;
    CardView notFound;
    TextView emptyPostTextView1,emptyPostTextView2;

    public AdapterAllWishList(List<ProductWishListDatabaseTable> productWishListDatabaseTables, Context context, CardView notFound, TextView emptyPostTextView1, TextView emptyPostTextView2) {
        this.productWishListDatabaseTables = productWishListDatabaseTables;
        this.context = context;
        this.emptyPostTextView1 = emptyPostTextView1;
        this.emptyPostTextView2 = emptyPostTextView2;
        this.notFound = notFound;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_all_product_wish_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SharedPreferences userPref = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        String userId = userPref.getString("id",String.valueOf(0));
        boolean isLoggedIn = userPref.getBoolean("isLoggedIn",false);
        MyDatabase myDatabase = MyDatabase.getInstance(context);
        ProductWishListDatabaseTable productWishListDatabaseTable = productWishListDatabaseTables.get(position);

        getProductDetails(productWishListDatabaseTable.getProductId(),holder.imageView,holder.name,holder.category,holder.stock,holder.price);

        holder.removeWishList.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                myDatabase.favouriteEventDatabaseDao().deleteFromProductWishList(userId,productWishListDatabaseTable.productId);
                productWishListDatabaseTables.remove(position);
                notifyItemRemoved(position);
                notifyDataSetChanged();
                if(productWishListDatabaseTables.isEmpty()){
                    notFound.setVisibility(View.VISIBLE);
                    emptyPostTextView2.setText("Tap the heart shape and add wish list");
                    emptyPostTextView1.setText("You have no wish list item yet !!");
                }else {
                    notFound.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return productWishListDatabaseTables.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView,removeWishList;
        TextView name,price,category,stock;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.productName);
            imageView = itemView.findViewById(R.id.imgIndicator);
            price = itemView.findViewById(R.id.txtprice);
            stock = itemView.findViewById(R.id.tvStock);
            category = itemView.findViewById(R.id.category);
            removeWishList = itemView.findViewById(R.id.imgFav
            );
        }
    }

    public void getProductDetails(int productId, ImageView imageView, TextView nameTv, TextView categoryTv, TextView stockTv, TextView priceTv){
        Call<ProductResult> call = RetrofitClient
                .getInstance()
                .getApi()
                .getProductView(productId);
        call.enqueue(new Callback<ProductResult>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<ProductResult> call, Response<ProductResult> response) {
                ProductResult productResult = response.body();

//                // load event thumbnail
                Glide.with(context)
                        .load(ApiURL.IMAGE_BASE+productResult.getImage())
                        .error(Glide.with(context)
                                .load("https://i.pinimg.com/originals/a7/46/df/a746dfd74e09d8c7cbcdfa7be02a6250.gif"))
                        .transforms(new CenterCrop(),new RoundedCorners(16))
                        .into(imageView);

                nameTv.setText(productResult.getName());
                categoryTv.setText("Category : "+productResult.getProductType());
                if(!productResult.getDigital()){
                    stockTv.setText("In Stock");
                    stockTv.setBackgroundResource(R.drawable.in_stock_bg);
                }else {
                    stockTv.setText("Out of Stock");
                    stockTv.setBackgroundResource(R.drawable.out_stock_bg);
                }

                priceTv.setText("৳ "+productResult.getPrice());

            }

            @Override
            public void onFailure(Call<ProductResult> call, Throwable t) {

            }
        });
    }
}
