package com.anikrakib.tourday.Fragment.Shop;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anikrakib.tourday.Adapter.Search.AdapterAllEventSearch;
import com.anikrakib.tourday.Adapter.Shop.AdapterAllWishList;
import com.anikrakib.tourday.R;
import com.anikrakib.tourday.RoomDatabse.MyDatabase;
import com.anikrakib.tourday.RoomDatabse.ProductWishListDatabaseTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WishListFragment extends Fragment {

    RecyclerView recyclerView;
    AdapterAllWishList adapterAllWishList;
    LinearLayout wishListMainLayout;
    List<ProductWishListDatabaseTable> productWishListDatabaseTables;
    MyDatabase myDatabase;
    CardView notFound;
    TextView emptyPostTextView1,emptyPostTextView2;
    ImageView removeAll;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wish_list, container, false);

        recyclerView = view.findViewById(R.id.wishListRecyclerView);
        emptyPostTextView1 = view.findViewById(R.id.emptyPostTextView);
        emptyPostTextView2 = view.findViewById(R.id.emptyPostTextView2);
        notFound = view.findViewById(R.id.emptyCardView);
        wishListMainLayout = view.findViewById(R.id.wishListMainLayout);
        removeAll = view.findViewById(R.id.removeAllWishListItem);

        myDatabase = MyDatabase.getInstance(getContext());

        SharedPreferences userPref = requireContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        String userId = userPref.getString("id","");
        boolean isLoggedIn = userPref.getBoolean("isLoggedIn",false);

        getAllProductWishList(userId);

        if(isLoggedIn){
            assert userId != null;
            productWishListDatabaseTables = myDatabase.favouriteEventDatabaseDao().getAllWishProduct(userId);
            if(productWishListDatabaseTables.isEmpty()){
                notFound.setVisibility(View.VISIBLE);
                wishListMainLayout.setVisibility(View.GONE);
                emptyPostTextView2.setText("Tap the heart shape and add wish list");
                emptyPostTextView1.setText("You have no wish list item yet !!");
            }
            else{
                notFound.setVisibility(View.GONE);
                wishListMainLayout.setVisibility(View.VISIBLE);
            }
        }else{
            notFound.setVisibility(View.VISIBLE);
            wishListMainLayout.setVisibility(View.GONE);
            emptyPostTextView2.setText("If you have no account, then create an account");
            emptyPostTextView1.setText("Sign In Required");
        }

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterAllWishList = new AdapterAllWishList(productWishListDatabaseTables,getContext(),notFound,emptyPostTextView1,emptyPostTextView2);
        recyclerView.setAdapter(adapterAllWishList);

        removeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDatabase.favouriteEventDatabaseDao().deleteAllWishListProduct(productWishListDatabaseTables);
                productWishListDatabaseTables.clear();
                if(productWishListDatabaseTables.isEmpty()){
                    notFound.setVisibility(View.VISIBLE);
                    emptyPostTextView2.setText("Tap the heart shape and add wish list");
                    emptyPostTextView1.setText("You have no wish list item yet !!");
                }else {
                    notFound.setVisibility(View.GONE);
                }
            }
        });

        return view;
    }

    public void getAllProductWishList(String currentUserId){

        productWishListDatabaseTables = myDatabase.favouriteEventDatabaseDao().getAllWishProduct(currentUserId);

    }
}