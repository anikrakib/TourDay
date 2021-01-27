package com.anikrakib.tourday.Fragment.Shop;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
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

import com.anikrakib.tourday.Activity.Shop.CheckoutActivity;
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
    CardView notFound,checkOut;
    TextView emptyPostTextView1,emptyPostTextView2,totalPrice,subTotal,deliveryCharge,cartItemAmount,totalItem;
    ImageView removeAll;
    public boolean isLoggedIn;

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
        checkOut = view.findViewById(R.id.checkout);

        myDatabase = MyDatabase.getInstance(getContext());


        shopCartTables = myDatabase.favouriteEventDatabaseDao().getAllCartProduct();

        View view2 = requireActivity().findViewById(R.id.count);
        if( view2 instanceof TextView ) {
            cartItemAmount = (TextView) view2;
        }

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterAllCartList = new AdapterAllCartList(shopCartTables,getContext(),subTotal,deliveryCharge,totalPrice,cartItemAmount,totalItem,checkOutLayout,notFound);
        recyclerView.setAdapter(adapterAllCartList);

        setPrice();

        if(shopCartTables.isEmpty()){
            checkOutLayout.setVisibility(View.GONE);
            notFound.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else{
            checkOutLayout.setVisibility(View.VISIBLE);
            notFound.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

        checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CheckoutActivity.class));
            }
        });

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