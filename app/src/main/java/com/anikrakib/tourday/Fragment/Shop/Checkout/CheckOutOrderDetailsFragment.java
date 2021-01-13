package com.anikrakib.tourday.Fragment.Shop.Checkout;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anikrakib.tourday.Adapter.Shop.AdapterAllCartList;
import com.anikrakib.tourday.Adapter.Shop.AdapterProductOrderList;
import com.anikrakib.tourday.R;
import com.anikrakib.tourday.RoomDatabse.MyDatabase;
import com.anikrakib.tourday.RoomDatabse.ShopCartTable;

import java.util.List;

public class CheckOutOrderDetailsFragment extends Fragment {
    RecyclerView orderListRecyclerView;
    AdapterProductOrderList adapterProductOrderList;
    AdapterAllCartList adapterAllCartList;
    List<ShopCartTable> shopCartTables;
    MyDatabase myDatabase;
    TextView subTotal;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_check_out_order_details, container, false);

        orderListRecyclerView = view.findViewById(R.id.productOrderListRecyclerView);
        subTotal = view.findViewById(R.id.tvTotal);

        myDatabase = MyDatabase.getInstance(getContext());

        shopCartTables = myDatabase.favouriteEventDatabaseDao().getAllCartProduct();

        subTotal.setText("৳ "+grandTotal());

        orderListRecyclerView.setHasFixedSize(true);
        orderListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterProductOrderList = new AdapterProductOrderList(shopCartTables,getContext());
        orderListRecyclerView.setAdapter(adapterProductOrderList);

        return view;
    }

    public int grandTotal() {
        int totalPrice = 0;
        for (int i = 0; i < shopCartTables.size(); i++) {
            totalPrice += (shopCartTables.get(i).getProductPrice()*shopCartTables.get(i).getProductQuantity());
        }
        return totalPrice;
    }
}