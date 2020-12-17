package com.anikrakib.tourday.Fragment.Shop;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    LinearLayoutManager layoutManager;
    List<ProductWishListDatabaseTable> productWishListDatabaseTables;
    MyDatabase myDatabase;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wish_list, container, false);

        recyclerView = view.findViewById(R.id.wishListRecyclerView);

        myDatabase = MyDatabase.getInstance(getContext());

        SharedPreferences userPref = requireContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        String userId = userPref.getString("id","");
        boolean isLoggedIn = userPref.getBoolean("isLoggedIn",false);

        getAllProductWishList(userId);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterAllWishList = new AdapterAllWishList(productWishListDatabaseTables,getContext());
        recyclerView.setAdapter(adapterAllWishList);

        return view;
    }

    public void getAllProductWishList(String currentUserId){

        productWishListDatabaseTables = myDatabase.favouriteEventDatabaseDao().getAllWishProduct(currentUserId);

    }
}