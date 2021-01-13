package com.anikrakib.tourday.Adapter.Shop;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anikrakib.tourday.R;
import com.anikrakib.tourday.RoomDatabse.MyDatabase;
import com.anikrakib.tourday.RoomDatabse.ShopCartTable;

import java.util.List;

public class AdapterProductOrderList extends RecyclerView.Adapter<AdapterProductOrderList.ViewHolder> {
    private List<ShopCartTable> shopCartTables;
    private Context context;

    public AdapterProductOrderList(List<ShopCartTable> shopCartTables, Context context) {
        this.shopCartTables = shopCartTables;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_order_list_item,parent,false);
        return new AdapterProductOrderList.ViewHolder(view);    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MyDatabase myDatabase = MyDatabase.getInstance(context);
        ShopCartTable shopCartTable = shopCartTables.get(position);

        holder.name.setText(shopCartTable.getProductName());
        holder.price.setText("৳ "+shopCartTable.getProductPrice());
        holder.quantity.setText(""+shopCartTable.getProductQuantity());
        holder.amount.setText("৳ "+shopCartTable.getProductQuantity()*shopCartTable.getProductPrice());
    }

    @Override
    public int getItemCount() {
        return shopCartTables.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,price,quantity,amount;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.productName);
            price = itemView.findViewById(R.id.price);
            quantity = itemView.findViewById(R.id.quantiity);
            amount = itemView.findViewById(R.id.amount);
        }
    }
}
