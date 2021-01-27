package com.anikrakib.tourday.Adapter.Shop;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.anikrakib.tourday.Models.Shop.ProductResult;
import com.anikrakib.tourday.R;
import com.anikrakib.tourday.RoomDatabse.MyDatabase;
import com.anikrakib.tourday.RoomDatabse.ProductWishListDatabaseTable;
import com.anikrakib.tourday.RoomDatabse.ShopCartTable;
import com.anikrakib.tourday.Utils.ApiURL;
import com.anikrakib.tourday.WebService.RetrofitClient;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterAllCartList extends RecyclerView.Adapter<AdapterAllCartList.ViewHolder> {

    private List<ShopCartTable> shopCartTables;
    private Context context;
    TextView cartItemAmount,subTotal,deliveryCharge,totalPrice,totalItems;
    LinearLayout checkOutLayout;
    CardView cardView;

    public AdapterAllCartList(List<ShopCartTable> shopCartTables, Context context, TextView subTotal, TextView deliveryCharge, TextView totalPrice, TextView cartItemAmount, TextView totalItems, LinearLayout checkOutLayout, CardView cardView) {
        this.shopCartTables = shopCartTables;
        this.context = context;
        this.cartItemAmount = cartItemAmount;
        this.subTotal = subTotal;
        this.deliveryCharge = deliveryCharge;
        this.totalPrice = totalPrice;
        this.totalItems = totalItems;
        this.checkOutLayout = checkOutLayout;
        this.cardView = cardView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_cart_item,parent,false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MyDatabase myDatabase = MyDatabase.getInstance(context);
        ShopCartTable shopCartTable = shopCartTables.get(position);

        Glide.with(context)
                .load(ApiURL.IMAGE_BASE+shopCartTable.getProductImage())
                .error(Glide.with(context)
                        .load("https://i.pinimg.com/originals/a7/46/df/a746dfd74e09d8c7cbcdfa7be02a6250.gif"))
                .transforms(new CenterCrop(),new RoundedCorners(16))
                .into(holder.imageView);
        holder.name.setText(shopCartTable.getProductName());
        holder.category.setText("Category : "+shopCartTable.getProductType());
        holder.price.setText("৳ "+shopCartTable.getProductPrice());
        holder.qty.setText(""+shopCartTable.getProductQuantity());
        holder.txtQty.setText(" X "+shopCartTable.getProductQuantity());
        holder.totalPrice.setText("৳ "+shopCartTable.getProductQuantity()*shopCartTable.getProductPrice());

        holder.removeCartItem.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                myDatabase.favouriteEventDatabaseDao().deleteFromProductCartList(shopCartTable.getProduct_id());
                shopCartTables.remove(position);
                notifyItemRemoved(position);
                notifyDataSetChanged();
                updateFragmentView();
                if(shopCartTables.isEmpty()){
                    cardView.setVisibility(View.VISIBLE);
                }else {
                    
                }
            }
        });

        holder.increaseQty.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                int quantity = Integer.parseInt(holder.qty.getText().toString());
                quantity++;
                holder.qty.setText(quantity+"");
                holder.txtQty.setText(" X "+quantity);
                holder.totalPrice.setText("৳ "+quantity*shopCartTable.getProductPrice());
                ShopCartTable shopCartTable =shopCartTables.get(position);
                shopCartTable.setProductQuantity(quantity);
                myDatabase.favouriteEventDatabaseDao().updateCartItem(shopCartTable);

                updateFragmentView();
            }
        });
        holder.decreaseQty.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                int quantity = Integer.parseInt(holder.qty.getText().toString());
                quantity--;
                if(quantity == 0){
                    myDatabase.favouriteEventDatabaseDao().deleteFromProductCartList(shopCartTable.getProduct_id());
                    shopCartTables.remove(position);
                    notifyItemRemoved(position);
                    notifyDataSetChanged();
                }else{
                    holder.qty.setText(quantity+"");
                    holder.txtQty.setText(" X "+quantity);
                    holder.totalPrice.setText("৳ "+quantity*shopCartTable.getProductPrice());
                    ShopCartTable shopCartTable =shopCartTables.get(position);
                    shopCartTable.setProductQuantity(quantity);
                    myDatabase.favouriteEventDatabaseDao().updateCartItem(shopCartTable);
                }
                updateFragmentView();

            }
        });

    }

    @SuppressLint("SetTextI18n")
    private void updateFragmentView() {
        if(shopCartTables.isEmpty()){
            checkOutLayout.setVisibility(View.GONE);
        }else {
            cartItemAmount.setText(getItemCount()+"");
            subTotal.setText("৳ "+grandTotal());
            deliveryCharge.setText("৳ 0");
            totalPrice.setText("৳ "+grandTotal());
            totalItems.setText("Total "+totalItem()+" items and ৳ "+grandTotal());
        }
    }

    public int grandTotal() {
        int totalPrice = 0;
        for (int i = 0; i < shopCartTables.size(); i++) {
            totalPrice += (shopCartTables.get(i).getProductPrice()*shopCartTables.get(i).getProductQuantity());
        }
        return totalPrice;
    }

    public int totalItem() {
        int totalItem = 0;
        for (int i = 0; i < shopCartTables.size(); i++) {
            totalItem += shopCartTables.get(i).getProductQuantity();
        }
        return totalItem;
    }

    @Override
    public int getItemCount() {
        return shopCartTables.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView,removeCartItem;
        TextView name,price,category,qty,totalPrice,txtQty;
        ImageButton increaseQty,decreaseQty;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.productName);
            imageView = itemView.findViewById(R.id.imgIndicator);
            price = itemView.findViewById(R.id.txtprice);
            qty = itemView.findViewById(R.id.txtQuantity);
            category = itemView.findViewById(R.id.category);
            removeCartItem = itemView.findViewById(R.id.imgFav);
            totalPrice = itemView.findViewById(R.id.totalPrice);
            txtQty = itemView.findViewById(R.id.txtQty);
            increaseQty = itemView.findViewById(R.id.btnaddqty);
            decreaseQty = itemView.findViewById(R.id.btnminusqty);

        }
    }
}
