package com.anikrakib.tourday.Activity.Event;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.anikrakib.tourday.Adapter.Event.AdapterGoingUserEvent;
import com.anikrakib.tourday.Models.Event.AllEventResult;
import com.anikrakib.tourday.Models.Event.GoingUser;
import com.anikrakib.tourday.Models.Event.PendingPayment;
import com.anikrakib.tourday.Models.Profile.EventPayment;
import com.anikrakib.tourday.R;
import com.anikrakib.tourday.Utils.ApiURL;
import com.anikrakib.tourday.Utils.TapToProgress.Circle;
import com.anikrakib.tourday.Utils.TapToProgress.CircleAnimation;
import com.anikrakib.tourday.WebService.RetrofitClient;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.google.android.material.snackbar.Snackbar;
import com.kishandonga.csbx.CustomSnackbar;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;
import com.squareup.picasso.Picasso;
import com.tylersuehr.socialtextview.SocialTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventDetailsActivity extends AppCompatActivity implements Animation.AnimationListener {
    RecyclerView goingUserRecyclerView;
    AdapterGoingUserEvent adapterGoingUserEvent;
    LinearLayout goingLinearLayout,pendingLinearLayout;
    RelativeLayout joinNow;
    Dialog myDialog;
    TextView eventDetailsTitleTextView,eventLocationTextView,eventTotalGoingTextView,eventTotalPendingTextView,eventTotalCapacityTextView,eventAvailableTextView,eventJoinTextView,hostUserName,eventPriceTextView;
    SocialTextView eventDetailsTextView;
    Intent intent;
    KenBurnsView eventDetailsImage;
    ImageButton backButton;
    String[] paymentType;
    Resources resources;
    String pay1Method,pay2Method,pay1,pay2;
    int cost,eventId;
    List<GoingUser> goingUserList;
    boolean cancel =false;
    Circle curve;
    EditText txIdEditText;
    Spinner paymentTypeSpinner;
    SocialTextView hostUserEmail;
    CircleImageView hostUserImage;
    String currentUserId;
    public static ArrayList<Integer> listPending,listGoing;




    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        goingUserRecyclerView = findViewById(R.id.eventGoingRecyclerView);
        goingLinearLayout = findViewById(R.id.goingLinearLayout);
        pendingLinearLayout = findViewById(R.id.pendingLinearLayout);
        joinNow = findViewById(R.id.joinNowRelativeLayOut);
        eventDetailsImage = findViewById(R.id.eventDetailsImage);
        eventDetailsTitleTextView = findViewById(R.id.eventDetailsTitleTextView);
        eventLocationTextView = findViewById(R.id.eventDetailsLocationTextView);
        eventTotalCapacityTextView = findViewById(R.id.eventDetailsTotalCapacityTextView);
        eventTotalGoingTextView = findViewById(R.id.eventDetailsTotalGoingTextView);
        eventTotalPendingTextView = findViewById(R.id.eventDetailsTotalPendingTextView);
        eventDetailsTextView = findViewById(R.id.eventDetailsDescriptionTextView);
        eventAvailableTextView = findViewById(R.id.eventDetailsAvailableTextView);
        backButton = findViewById(R.id.backButtonEvent);
        eventJoinTextView = findViewById(R.id.eventJoinTextView);
        hostUserEmail = findViewById(R.id.hostUserEmail);
        hostUserName = findViewById(R.id.hostUserName);
        hostUserImage = findViewById(R.id.hostUserImage);
        eventPriceTextView = findViewById(R.id.eventPriceTextView);


        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        SharedPreferences userPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        currentUserId = userPref.getString("id","");

        resources= getResources();
        paymentType = resources.getStringArray(R.array.paymentType);
        myDialog = new Dialog(this);
        intent = getIntent();
        Bundle bundle = intent.getExtras();

        assert bundle != null;
        eventId = bundle.getInt("eventId");
        listGoing = new ArrayList<>();
        listPending = new ArrayList<>();

        //set Data
        getEventAllData();
        checkUserEventAction();

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
        joinNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int orientation = getApplicationContext().getResources().getConfiguration().orientation;
                if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                    // code for portrait mode
                    if(eventJoinTextView.getText().toString().equals("Join Now")) showPaymentPopUp();
                    else snackBar("You are already "+eventJoinTextView.getText().toString(),R.color.white);

                } else {
                    // code for landscape mode
                    //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    snackBar("If You Want to Payment, Make sure your screen is Portrait Mode",R.color.white);
                }
            }
        });

        // going user list
        goingUserList = new ArrayList<>();
        goingUserList(eventId);

    }

    @SuppressLint("SetTextI18n")
    private void checkUserEventAction() {

    }

    private void getEventAllData() {
        Call<AllEventResult> resultCall = RetrofitClient
                .getInstance()
                .getApi()
                .getEventDetails(eventId);
        resultCall.enqueue(new Callback<AllEventResult>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<AllEventResult> call, @NonNull Response<AllEventResult> response) {
                AllEventResult allEventResult = response.body();

                assert allEventResult != null;
                listPending = (ArrayList<Integer>) allEventResult.getPending();
                listGoing = (ArrayList<Integer>) allEventResult.getGoing();

                for (int i = 0; i<listGoing.size();i++){
                    if(Integer.parseInt(currentUserId) == listGoing.get(i)){
                        eventJoinTextView.setText("Going");
                    }else{
                        eventJoinTextView.setText("Join Now");
                    }
                }

                for (int i = 0; i<listPending.size();i++){
                    if(Integer.parseInt(currentUserId) == listPending.get(i)){
                        eventJoinTextView.setText("Pending");
                    }else{
                        eventJoinTextView.setText("Join Now");
                    }
                }
                eventDetailsTextView.setLinkText(allEventResult.getDetails());
                eventDetailsTitleTextView.setText(allEventResult.getTitle());
                eventLocationTextView.setText(allEventResult.getLocation());
                eventTotalPendingTextView.setText(String.valueOf(allEventResult.getPending().size()));
                eventTotalGoingTextView.setText(String.valueOf(allEventResult.getGoing().size()));
                eventTotalCapacityTextView.setText(String.valueOf(allEventResult.getCapacity()));
                eventPriceTextView.setText(String.valueOf(allEventResult.getCost()));
                if(!(allEventResult.getCapacity() - allEventResult.getGoing().size() == 0)){
                    eventAvailableTextView.setText(String.valueOf(allEventResult.getCapacity() - allEventResult.getGoing().size()));
                }else{
                    eventAvailableTextView.setText("No Seat");
                }
                Picasso.get().load(ApiURL.IMAGE_BASE + allEventResult.getImage()).fit().centerInside().into(eventDetailsImage);
                setHostData(allEventResult.getHost());
                // setPayment Info
                pay1 = allEventResult.getPay1();
                pay2 = allEventResult.getPay2();
                pay1Method = allEventResult.getPay1Method();
                pay2Method = allEventResult.getPay1Method();
                cost = allEventResult.getCost();
            }

            @Override
            public void onFailure(@NonNull Call<AllEventResult> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(),t.toString(),Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setHostData(int hostId) {
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .getUserInfoByUserId(hostId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    JSONObject jsonObject = null;
                    try {
                        assert response.body() != null;
                        jsonObject = new JSONObject(response.body().string());
                        JSONObject profile = jsonObject.getJSONObject("profile");

                        Picasso.get().load(ApiURL.IMAGE_BASE + profile.getString("picture")).fit().centerInside().into(hostUserImage);
                        hostUserName.setText(profile.getString("name"));
                        hostUserEmail.setLinkText(profile.getString("email"));

                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"Host Id Not Correct",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(),"Timed Out",Toast.LENGTH_LONG).show();
            }
        });
    }

    @SuppressLint("SetTextI18n,ClickableViewAccessibility")


    private void showPaymentPopUp() {
        TextView payment1,payment2,eventPrice;
        LinearLayout taplayout;

        //android:screenOrientation = "portrait"

        myDialog.setContentView(R.layout.custom_payment_pop_up);
        payment1 = myDialog.findViewById(R.id.payment1);
        payment2 = myDialog.findViewById(R.id.payment2);
        paymentTypeSpinner = myDialog.findViewById(R.id.paymentTypeSpinner);
        txIdEditText = myDialog.findViewById(R.id.txIdEditText);
        eventPrice = myDialog.findViewById(R.id.eventPriceTextView);
        curve = myDialog.findViewById(R.id.curve);
        taplayout = myDialog.findViewById(R.id.layouttap);


        CircleAnimation circleAnimation = new CircleAnimation(curve, 130.0f);
        curve.setCurveColor(ContextCompat.getColor(getApplicationContext(), R.color.tap_light), ContextCompat.getColor(getApplicationContext(), R.color.tap_dark));
        circleAnimation.setDuration(1500);
        circleAnimation.setAnimationListener(this);

        taplayout.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case 0:
                        curve.startAnimation(circleAnimation);
                        cancel = false;
                        return true;
                    case 1:
                    case 3:
                        circleAnimation.cancel();
                        cancel = true;
                        return true;
                    default:
                        return false;
                }
            }
        });


        payment1.setText(pay1Method+" - "+pay1);
        payment2.setText(pay2Method+" - "+pay2);
        eventPrice.setText(String.valueOf(cost));

        // set value in district spinner
        ArrayAdapter<String> arrayAdapterPaymentType = new ArrayAdapter<String>(this,R.layout.custom_spinner_item,R.id.districtNameTextView,paymentType);
        paymentTypeSpinner.setAdapter(arrayAdapterPaymentType);


        Objects.requireNonNull(myDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
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
                adapterGoingUserEvent = new AdapterGoingUserEvent(EventDetailsActivity.this,goingUserList);
                goingUserRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
                goingUserRecyclerView.setAdapter(adapterGoingUserEvent);
            }

            @Override
            public void onFailure(@NonNull Call<List<GoingUser>> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(),"Fail!",Toast.LENGTH_LONG).show();
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

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if(cancel){
            DynamicToast.makeWarning(getApplicationContext(),"Hold Long To Succeed").show();
        }else{
            curve.setCurveColor(ContextCompat.getColor(getApplicationContext(), R.color.tap_light), ContextCompat.getColor(getApplicationContext(), R.color.tap_dark));
            if(txIdEditText.getText().toString().isEmpty()){
                snackBar("Invalid Transaction Id !!",R.color.dark_red);
            }else{
                eventPayment();
                myDialog.cancel();
            }
            //Toast.makeText(getApplicationContext(),"complete",Toast.LENGTH_LONG).show();
        }
    }

    public void eventPayment(){
        SharedPreferences userPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        String token = userPref.getString("token","");

        Call<EventPayment> call = RetrofitClient
                .getInstance()
                .getApi()
                .eventPayment("Token "+token,eventId,paymentTypeSpinner.getSelectedItem().toString(),txIdEditText.getText().toString());
        call.enqueue(new Callback<EventPayment>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<EventPayment> call, @NonNull retrofit2.Response<EventPayment> response) {
                if (response.isSuccessful()) {
                    EventPayment eventPayment = response.body();
                    if(eventPayment.getStatus() == 200){
                        snackBar("Payment Completed Successfully",R.color.white);
                        eventJoinTextView.setText("Pending");
                    }else{
                        snackBar("You Already Completed Your Payment",R.color.dark_red);
                    }
                } else {
                    DynamicToast.makeError(getApplicationContext(), "Something Wrong").show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<EventPayment> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
    public void snackBar(String text,int color){
        CustomSnackbar sb = new CustomSnackbar(this);
        sb.message(text);
        sb.padding(15);
        sb.textColorRes(color);
        sb.backgroundColorRes(R.color.colorPrimaryDark);
        sb.cornerRadius(15);
        sb.duration(Snackbar.LENGTH_LONG);
        sb.show();
    }
}
