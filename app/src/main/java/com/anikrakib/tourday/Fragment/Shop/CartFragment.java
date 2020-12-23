package com.anikrakib.tourday.Fragment.Shop;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anikrakib.tourday.Activity.Shop.ShopActivity;
import com.anikrakib.tourday.Adapter.Shop.AdapterAllCartList;
import com.anikrakib.tourday.Adapter.Shop.AdapterAllWishList;
import com.anikrakib.tourday.R;
import com.anikrakib.tourday.RoomDatabse.MyDatabase;
import com.anikrakib.tourday.RoomDatabse.ProductWishListDatabaseTable;
import com.anikrakib.tourday.RoomDatabse.ShopCartTable;

import java.util.List;
import java.util.Objects;


public class CartFragment extends Fragment {
    RecyclerView recyclerView;
    AdapterAllCartList adapterAllCartList;
    LinearLayout checkOutLayout;
    List<ShopCartTable> shopCartTables;
    MyDatabase myDatabase;
    CardView notFound;
    TextView emptyPostTextView1,emptyPostTextView2,totalPrice,subTotal,deliveryCharge,cartItemAmount,totalItem;
    ImageView removeAll;
    public boolean checkInternet,isLoggedIn;
    Dialog postDialog ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        recyclerView = view.findViewById(R.id.cartRecyclerview);
        emptyPostTextView1 = view.findViewById(R.id.emptyPostTextView);
        emptyPostTextView2 = view.findViewById(R.id.emptyPostTextView2);
        notFound = view.findViewById(R.id.emptyCardView);
        removeAll = view.findViewById(R.id.removeAllCartListItem);
        checkOutLayout = view.findViewById(R.id.checkOutLayout);
        subTotal = view.findViewById(R.id.subTotalAmount);
        deliveryCharge = view.findViewById(R.id.deliveryChargeAmount);
        totalPrice = view.findViewById(R.id.totalAmount);
        totalItem = view.findViewById(R.id.totalItem);

        myDatabase = MyDatabase.getInstance(getContext());


        shopCartTables = myDatabase.favouriteEventDatabaseDao().getAllCartProduct();

        View view2 = requireActivity().findViewById(R.id.count);
        if( view2 instanceof TextView ) {
            cartItemAmount = (TextView) view2;
        }

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterAllCartList = new AdapterAllCartList(shopCartTables,getContext(),subTotal,deliveryCharge,totalPrice,cartItemAmount,totalItem);
        recyclerView.setAdapter(adapterAllCartList);

        setPrice();


        if(shopCartTables.isEmpty()){
            checkOutLayout.setVisibility(View.GONE);
        }else{
            checkOutLayout.setVisibility(View.VISIBLE);
        }

        return view;
    }

    @SuppressLint("SetTextI18n")
    private void setPrice() {
        subTotal.setText("৳ "+adapterAllCartList.grandTotal());
        deliveryCharge.setText("৳ 0");
        totalPrice.setText("৳ "+adapterAllCartList.grandTotal());
        totalItem.setText("Total "+adapterAllCartList.totalItem()+" items and ৳ "+adapterAllCartList.grandTotal());
    }

    @Override
    public void onResume() {
        super.onResume();
        cartItemAmount.setText(String.valueOf(myDatabase.favouriteEventDatabaseDao().countCart()));
        setPrice();
    }
}