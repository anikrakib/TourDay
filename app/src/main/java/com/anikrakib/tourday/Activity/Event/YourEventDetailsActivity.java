package com.anikrakib.tourday.Activity.Event;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anikrakib.tourday.Adapter.Event.AdapterGoingUserEvent;
import com.anikrakib.tourday.Adapter.Event.AdapterPendingPayment;
import com.anikrakib.tourday.Models.Blog.DeleteBlogResponse;
import com.anikrakib.tourday.Models.Event.AllEventResult;
import com.anikrakib.tourday.Models.Event.DeleteEventResponse;
import com.anikrakib.tourday.Models.Event.GoingUser;
import com.anikrakib.tourday.Models.Event.PendingPayment;
import com.anikrakib.tourday.R;
import com.anikrakib.tourday.Utils.ApiURL;
import com.anikrakib.tourday.WebService.RetrofitClient;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;
import com.squareup.picasso.Picasso;
import com.tylersuehr.socialtextview.SocialTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class YourEventDetailsActivity extends AppCompatActivity {
    RelativeLayout pendingPaymentLinearLayout;
    RecyclerView pendingUserRecyclerView,goingUserRecyclerView;
    AdapterPendingPayment adapterPendingPayment;
    Intent intent;
    TextView eventDetailsTitleTextView,eventLocationTextView,eventTotalGoingTextView,eventTotalPendingTextView,eventTotalCapacityTextView;
    SocialTextView eventDetailsTextView;
    KenBurnsView eventDetailsImage;
    List<GoingUser> goingUserList;
    List<PendingPayment> pendingPayment;
    ImageButton backButton,deleteEventButton,bookmarkButton;
    AdapterGoingUserEvent adapterGoingUserEvent;
    LinearLayout goingLinearLayout;
    int eventId;
    Dialog myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_event_details);

        pendingPaymentLinearLayout = findViewById(R.id.upDownArrowPendingPaymentRelativeLayOut);
        eventDetailsImage = findViewById(R.id.eventDetailsImage);
        eventDetailsTitleTextView = findViewById(R.id.eventDetailsTitleTextView);
        eventLocationTextView = findViewById(R.id.eventDetailsLocationTextView);
        eventTotalCapacityTextView = findViewById(R.id.eventDetailsTotalCapacityTextView);
        eventTotalGoingTextView = findViewById(R.id.eventDetailsTotalGoingTextView);
        eventTotalPendingTextView = findViewById(R.id.eventDetailsTotalPendingTextView);
        eventDetailsTextView = findViewById(R.id.eventDetailsDescriptionTextView);
        pendingUserRecyclerView = findViewById(R.id.pendingUserRecyclerView);
        backButton = findViewById(R.id.backButtonEvent);
        goingUserRecyclerView = findViewById(R.id.eventGoingRecyclerView);
        goingLinearLayout = findViewById(R.id.goingYourEventLinearLayout);
        deleteEventButton = findViewById(R.id.deleteEvent);
        bookmarkButton = findViewById(R.id.favouriteButton);


        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        intent = getIntent();
        Bundle bundle = intent.getExtras();
        myDialog = new Dialog(this);
        deleteEventButton.setVisibility(View.VISIBLE);
        bookmarkButton.setVisibility(View.GONE);

        assert bundle != null;
        eventId = bundle.getInt("eventId");

        //set Data
        getEventAllDate();


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        deleteEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteBlogPopUp(eventId);
            }
        });

        goingLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(goingUserRecyclerView.getVisibility() == View.GONE){
                    goingUserRecyclerView.setVisibility(View.VISIBLE);
                }else{
                    goingUserRecyclerView.setVisibility(View.GONE);
                }
            }
        });

        pendingPaymentLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pendingUserRecyclerView.getVisibility() == View.GONE){
                    pendingUserRecyclerView.setVisibility(View.VISIBLE);
                }else{
                    pendingUserRecyclerView.setVisibility(View.GONE);
                }
            }
        });


        // goingUser List
        goingUserList = new ArrayList<>();
        pendingPayment = new ArrayList<>();
        goingUserList(eventId);
        getPendingList(eventId);


    }

    public void showDeleteBlogPopUp(int eventId) {
        Button yesButton,noButton;
        myDialog.setContentView(R.layout.custom_delete_blog_pop_up);
        yesButton = myDialog.findViewById(R.id.yesButton);
        noButton = myDialog.findViewById(R.id.noButton);

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteEvent(eventId);
                myDialog.dismiss();
                finish();
            }
        });
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.setCancelable(false);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();

    }

    private void deleteEvent(int eventId) {
        SharedPreferences userPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        String token = userPref.getString("token","");

        Call<DeleteEventResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .deleteEvent("Token "+token,eventId);

        call.enqueue(new Callback<DeleteEventResponse>() {
            @Override
            public void onResponse(@NonNull Call<DeleteEventResponse> call, @NonNull Response<DeleteEventResponse> response) {
                DeleteEventResponse message = response.body();
                if(response.isSuccessful()){
                    DynamicToast.makeSuccess(getApplicationContext(), message.getMessage()).show();
                }else{
                    DynamicToast.makeError(getApplicationContext(), message.getMessage()).show();
                }
            }

            @Override
            public void onFailure(Call<DeleteEventResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getEventAllDate() {
        Call<AllEventResult> resultCall = RetrofitClient
                .getInstance()
                .getApi()
                .getEventDetails(eventId);
        resultCall.enqueue(new Callback<AllEventResult>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<AllEventResult> call, @NonNull Response<AllEventResult> response) {
                AllEventResult allEventResult = response.body();

                try {
                    eventDetailsTextView.setLinkText(allEventResult.getDetails());
                    eventDetailsTitleTextView.setText(allEventResult.getTitle());
                    eventLocationTextView.setText(allEventResult.getLocation());
                    eventTotalPendingTextView.setText(String.valueOf(allEventResult.getPending().size()));
                    eventTotalGoingTextView.setText(String.valueOf(allEventResult.getGoing().size()));
                    eventTotalCapacityTextView.setText(String.valueOf(allEventResult.getCapacity()));
                    Picasso.get().load(ApiURL.IMAGE_BASE + allEventResult.getImage()).fit().centerInside().into(eventDetailsImage);
                }catch (Exception exception){
                    Toast.makeText(getApplicationContext(),"May be This Event Removed!!",Toast.LENGTH_LONG).show();
                    finish();
                }

            }

            @Override
            public void onFailure(@NonNull Call<AllEventResult> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getPendingList(int eventId){
        SharedPreferences userPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        String token = userPref.getString("token","");
        pendingPayment = new ArrayList<>();
        Call<List<PendingPayment>> call = RetrofitClient
                .getInstance()
                .getApi()
                .getAllPendingUser("Token "+token,eventId);
        call.enqueue(new Callback<List<PendingPayment>>() {
            @Override
            public void onResponse(@NonNull Call<List<PendingPayment>> call, @NonNull Response<List<PendingPayment>> response) {
                if (response.isSuccessful()) {
                    pendingPayment = response.body();
                }else{
                    Toast.makeText(getApplicationContext(),"Sign In Required",Toast.LENGTH_SHORT).show();
                }
                adapterPendingPayment = new AdapterPendingPayment(YourEventDetailsActivity.this,pendingPayment,eventTotalPendingTextView,eventTotalGoingTextView);
                pendingUserRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
                pendingUserRecyclerView.setAdapter(adapterPendingPayment);
            }

            @Override
            public void onFailure(@NonNull Call<List<PendingPayment>> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    public void goingUserList(int eventId){
        Call<List<GoingUser>> call = RetrofitClient
                .getInstance()
                .getApi()
                .getAllGoingUser(eventId);
        call.enqueue(new Callback<List<GoingUser>>() {
            @Override
            public void onResponse(@NonNull Call<List<GoingUser>> call, @NonNull Response<List<GoingUser>> response) {
                if (response.isSuccessful()) {
                    goingUserList = response.body();
                }
                adapterGoingUserEvent = new AdapterGoingUserEvent(YourEventDetailsActivity.this,goingUserList);
                goingUserRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
                goingUserRecyclerView.setAdapter(adapterGoingUserEvent);

            }

            @Override
            public void onFailure(@NonNull Call<List<GoingUser>> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();
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
}