package com.anikrakib.tourday.Fragment.Shop;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.anikrakib.tourday.Adapter.Search.AdapterAllEventSearch;
import com.anikrakib.tourday.Adapter.Shop.AdapterAllWishList;
import com.anikrakib.tourday.R;
import com.anikrakib.tourday.RoomDatabse.MyDatabase;
import com.anikrakib.tourday.RoomDatabse.ProductWishListDatabaseTable;
import com.anikrakib.tourday.Utils.CheckInternet;

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
    public boolean checkInternet,isLoggedIn;
    Dialog postDialog ;
    String userId;
    LottieAnimationView lottieAnimationView;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint({"SetTextI18n", "CutPasteId"})
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
        lottieAnimationView = view.findViewById(R.id.noResultGifView);

        myDatabase = MyDatabase.getInstance(getContext());
        checkInternet = CheckInternet.isConnected(getContext());
        postDialog = new Dialog(getActivity());


        SharedPreferences userPref = requireContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        userId = userPref.getString("id","");
        isLoggedIn = userPref.getBoolean("isLoggedIn",false);

        getAllProductWishList(userId);
        checkInternetAndLoggedInOrNot();


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

    @SuppressLint("SetTextI18n")
    private void checkInternetAndLoggedInOrNot(){
        if(!checkInternet){

            notFound.setVisibility(View.VISIBLE);
            lottieAnimationView.setAnimation(R.raw.no_internet);
            emptyPostTextView2.setText("Check You Data Connection or Wifi Connection");
            emptyPostTextView1.setText("Sorry Internet Not Connected");
            wishListMainLayout.setVisibility(View.GONE);


        }else{
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
        }
    }

    public void getAllProductWishList(String currentUserId){

        productWishListDatabaseTables = myDatabase.favouriteEventDatabaseDao().getAllWishProduct(currentUserId);

    }
}