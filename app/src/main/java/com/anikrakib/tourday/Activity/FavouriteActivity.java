package com.anikrakib.tourday.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.anikrakib.tourday.Adapter.Search.AdapterAllEventSearch;
import com.anikrakib.tourday.Models.Event.AllEventResult;
import com.anikrakib.tourday.R;
import com.anikrakib.tourday.RoomDatabse.FavouriteEventDatabaseTable;
import com.anikrakib.tourday.RoomDatabse.MyDatabase;
import com.github.ybq.android.spinkit.SpinKitView;

import java.util.ArrayList;
import java.util.List;

public class FavouriteActivity extends AppCompatActivity {

    RecyclerView eventSearchAllRecyclerView;
    AdapterAllEventSearch adapterAllEventSearchSearch;
    SwipeRefreshLayout swipeRefreshLayout;
    CardView notFound;
    ImageButton backButton,deleteAll;
    TextView emptyPostTextView1,emptyPostTextView2;

    List<FavouriteEventDatabaseTable> allEventResults = new ArrayList<>();
    MyDatabase myDatabase;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        eventSearchAllRecyclerView = findViewById(R.id.searchAllUserRecyclerView);
        swipeRefreshLayout = findViewById(R.id.searchAllUserSwipeRefreshLayout);
        notFound = findViewById(R.id.emptyCardView);
        deleteAll = findViewById(R.id.deleteAllFavourite);
        backButton = findViewById(R.id.backButtonFavourite);
        emptyPostTextView1 = findViewById(R.id.emptyPostTextView);
        emptyPostTextView2 = findViewById(R.id.emptyPostTextView2);

        if(loadNightModeState()){
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(getResources().getColor(R.color.backgroundColor));
        }else{
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        SharedPreferences userPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        String userId = userPref.getString("id","");
        boolean isLoggedIn = userPref.getBoolean("isLoggedIn",false);

        myDatabase = MyDatabase.getInstance(this);

        if(isLoggedIn){
            assert userId != null;
            allEventResults = myDatabase.favouriteEventDatabaseDao().getAll(userId);
            if(allEventResults.isEmpty()){
                notFound.setVisibility(View.VISIBLE);
                emptyPostTextView2.setText("Tap The Bookmark Icon And Save it Bookmark List");
                emptyPostTextView1.setText("No Bookmarked Item Yet");
            }
            else notFound.setVisibility(View.GONE);
        }else{
            notFound.setVisibility(View.VISIBLE);
            emptyPostTextView2.setText("If you have no account, then create an account");
            emptyPostTextView1.setText("Sign In Required");
        }

        eventSearchAllRecyclerView.setHasFixedSize(true);
        eventSearchAllRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapterAllEventSearchSearch = new AdapterAllEventSearch(allEventResults,getApplicationContext(),true,notFound,emptyPostTextView1,emptyPostTextView2);
        eventSearchAllRecyclerView.setAdapter(adapterAllEventSearchSearch);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(isLoggedIn){
                    notFound.setVisibility(View.GONE);
                    allEventResults = myDatabase.favouriteEventDatabaseDao().getAll(userId);
                    if(allEventResults.isEmpty()) {
                        notFound.setVisibility(View.VISIBLE);
                        emptyPostTextView2.setText("Tap The Bookmark Icon And Save it Bookmark List");
                        emptyPostTextView1.setText("No Bookmarked Item Yet");
                    }
                    else notFound.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);
                }else{
                    notFound.setVisibility(View.VISIBLE);
                    emptyPostTextView2.setText("If you have no account, then create an account");
                    emptyPostTextView1.setText("Sign In Required");
                }

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDatabase.favouriteEventDatabaseDao().deleteAll(allEventResults);
                allEventResults.clear();
                allEventResults.addAll(myDatabase.favouriteEventDatabaseDao().getAll(userId));
                adapterAllEventSearchSearch = new AdapterAllEventSearch(allEventResults,getApplicationContext(),true,notFound,emptyPostTextView1,emptyPostTextView2);
                eventSearchAllRecyclerView.setAdapter(adapterAllEventSearchSearch);
                adapterAllEventSearchSearch.notifyDataSetChanged();
                if(allEventResults.isEmpty()){
                    notFound.setVisibility(View.VISIBLE);
                    emptyPostTextView2.setText("Tap The Bookmark Icon And Save it Bookmark List");
                    emptyPostTextView1.setText("No Bookmarked Item Yet");
                }
                else notFound.setVisibility(View.GONE);
            }
        });
    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams winParams = window.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        window.setAttributes(winParams);
    }
    public Boolean loadNightModeState (){
        SharedPreferences userPref = getApplicationContext().getSharedPreferences("nightMode", Context.MODE_PRIVATE);
        return userPref.getBoolean("night_mode",false);
    }
}