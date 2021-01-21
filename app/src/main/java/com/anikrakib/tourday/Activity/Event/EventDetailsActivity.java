package com.anikrakib.tourday.Activity.Event;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
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

import com.anikrakib.tourday.Activity.Authentication.SignInActivity;
import com.anikrakib.tourday.Activity.FavouriteActivity;
import com.anikrakib.tourday.Activity.MainActivity;
import com.anikrakib.tourday.Adapter.Event.AdapterGoingEvent;
import com.anikrakib.tourday.Adapter.Event.AdapterGoingUserEvent;
import com.anikrakib.tourday.Adapter.Event.AdapterSuggestedEvent;
import com.anikrakib.tourday.Models.Event.AllEventResponse;
import com.anikrakib.tourday.Models.Event.AllEventResult;
import com.anikrakib.tourday.Models.Event.GoingUser;
import com.anikrakib.tourday.Models.Event.PendingPayment;
import com.anikrakib.tourday.Models.Profile.EventPayment;
import com.anikrakib.tourday.R;
import com.anikrakib.tourday.RoomDatabse.FavouriteEventDatabaseTable;
import com.anikrakib.tourday.RoomDatabse.MyDatabase;
import com.anikrakib.tourday.Utils.ApiURL;
import com.anikrakib.tourday.Utils.Loader;
import com.anikrakib.tourday.Utils.PaginationScrollListener;
import com.anikrakib.tourday.Utils.Share;
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
    RecyclerView goingUserRecyclerView,suggestedEventRecyclerView;
    private LinearLayoutManager layoutManager;
    private AdapterSuggestedEvent adapterSuggestedEvent;
    AdapterGoingUserEvent adapterGoingUserEvent;
    LinearLayout goingLinearLayout,pendingLinearLayout,suggestedEvent;
    RelativeLayout joinNow;
    Dialog myDialog;
    TextView eventDetailsTitleTextView,eventLocationTextView,eventTotalGoingTextView,eventTotalPendingTextView,eventTotalCapacityTextView,eventAvailableTextView,eventJoinTextView,hostUserName,eventPriceTextView;
    SocialTextView eventDetailsTextView;
    Intent intent;
    KenBurnsView eventDetailsImage;
    ImageButton backButton,favouriteButton,shareButton;
    String[] paymentType;
    Resources resources;
    String pay1Method,pay2Method,pay1,pay2;
    int eventCost,eventId,eventHost;
    List<GoingUser> goingUserList;
    boolean cancel =false;
    Circle curve;
    EditText txIdEditText;
    Spinner paymentTypeSpinner;
    SocialTextView hostUserEmail;
    CircleImageView hostUserImage;
    String currentUserId,eventImage,eventDate;
    public static ArrayList<Integer> listPending,listGoing;

    MyDatabase myDatabase;

    private static final int LIMIT = 10;
    private static final int OFFSET = 0;
    private boolean isLoadingAllEvent = false;
    private boolean isLastPageAllEvent = false;
    private static int TOTAL_PAGES_ALL_EVENT;
    private int currentOffset = OFFSET;
    String userName;
    boolean isLoggedIn;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        goingUserRecyclerView = findViewById(R.id.eventGoingRecyclerView);
        suggestedEventRecyclerView = findViewById(R.id.suggestedEventRecyclerView);
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
        favouriteButton = findViewById(R.id.favouriteButton);
        shareButton = findViewById(R.id.shareEventImageButton);
        suggestedEvent = findViewById(R.id.suggestedEventLAyout);


        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        SharedPreferences userPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        currentUserId = userPref.getString("id","");
        isLoggedIn = userPref.getBoolean("isLoggedIn",false);

        resources= getResources();
        paymentType = resources.getStringArray(R.array.paymentType);
        myDialog = new Dialog(this);
        intent = getIntent();
        Bundle bundle = intent.getExtras();
        myDatabase = MyDatabase.getInstance(this);

        assert bundle != null;
        eventId = bundle.getInt("eventId");
        listGoing = new ArrayList<>();
        listPending = new ArrayList<>();

        adapterSuggestedEvent = new AdapterSuggestedEvent(getApplicationContext(),eventId);
        suggestedEventRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        suggestedEventRecyclerView.setItemAnimator(new DefaultItemAnimator());
        suggestedEventRecyclerView.setLayoutManager(layoutManager);
        suggestedEventRecyclerView.setAdapter(adapterSuggestedEvent);

        //Loader.start(EventDetailsActivity.this);
        //set Data
        getEventAllData();

        if (myDatabase.favouriteEventDatabaseDao().addByUserId(currentUserId,eventId) == 1){
            favouriteButton.setImageResource(R.drawable.ic_bookmarked);
        }else {
            favouriteButton.setImageResource(R.drawable.ic_un_bookmark);
        }


        suggestedEventRecyclerView.addOnScrollListener(new PaginationScrollListener(layoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoadingAllEvent = true;
                currentOffset += 10;
                getAllEventNextPage();
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES_ALL_EVENT;
            }

            @Override
            public boolean isLastPage() {
                return isLastPageAllEvent;
            }

            @Override
            public boolean isLoading() {
                return isLoadingAllEvent;
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
        joinNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int orientation = getApplicationContext().getResources().getConfiguration().orientation;
                if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                    // code for portrait mode
                    if(eventJoinTextView.getText().toString().equals("Join Now")) {
                        showPaymentPopUp();
                    }
                    else if(eventJoinTextView.getText().toString().equals("Sign In")) {
                        startActivity(new Intent(EventDetailsActivity.this, SignInActivity.class));
                        finish();
                    } else {
                        snackBar("You are already "+eventJoinTextView.getText().toString(),R.color.white);
                    }
                } else {
                    // code for landscape mode
                    //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    snackBar("If You Want to Payment, Make sure your screen is Portrait Mode",R.color.white);
                }
            }
        });

        favouriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isLoggedIn){
                    favouriteButton.setImageResource(R.drawable.ic_bookmarked);
                    FavouriteEventDatabaseTable favouriteEventDatabaseTable = new FavouriteEventDatabaseTable();
                    favouriteEventDatabaseTable.setEventId(String.valueOf(eventId));
                    favouriteEventDatabaseTable.setImage(eventImage);
                    favouriteEventDatabaseTable.setName(eventDetailsTitleTextView.getText().toString().trim());
                    favouriteEventDatabaseTable.setDate(eventDate);
                    favouriteEventDatabaseTable.setUser_id(currentUserId);
                    favouriteEventDatabaseTable.setLocation(eventLocationTextView.getText().toString().trim());
                    favouriteEventDatabaseTable.setPrice(String.valueOf(eventCost));
                    favouriteEventDatabaseTable.setHost(String.valueOf(eventHost));

                    if (myDatabase.favouriteEventDatabaseDao().addByUserId(currentUserId,eventId)!=1){
                        myDatabase.favouriteEventDatabaseDao().insert(favouriteEventDatabaseTable);
                        snackBar("Event Bookmarked ",R.color.white);
                    }else {
                        snackBar("It Already Bookmarked!",R.color.white);
                    }
                }else {
                    snackBar("Sign In Required !!",R.color.white);
                }
            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Share.shareLink(getApplicationContext(),"event/"+eventId);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // going user list
        goingUserList = new ArrayList<>();
        goingUserList(eventId);

    }

    private List<AllEventResult> fetchResultsAllEvent(Response<AllEventResponse> response) {
        AllEventResponse allEventResponse = response.body();
        assert allEventResponse != null;
        return allEventResponse.getResults();
    }

    private void getAllEvent(String location) {
        Call<AllEventResponse> popular = RetrofitClient
                .getInstance()
                .getApi()
                .getAllSearchEvent(location,10,0);
        popular.enqueue(new Callback<AllEventResponse>() {
            @Override
            public void onResponse(Call<AllEventResponse> call, retrofit2.Response<AllEventResponse> response) {
                if (response.isSuccessful()) {
                    SharedPreferences userPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
                    String userId = userPref.getString("id","");
                    List<AllEventResult> results = fetchResultsAllEvent(response);
                    for (int i = 0 ; i<results.size() ; i++) {
                        if(!(results.get(i).getId() == eventId)){
                            adapterSuggestedEvent.add(results.get(i));
                        }
                    }
                    // check event suggested or not
                    if (adapterSuggestedEvent.isEmpty()){
                        suggestedEvent.setVisibility(View.GONE);
                    }else {
                        suggestedEvent.setVisibility(View.VISIBLE);
                    }
                }
            }
            @Override
            public void onFailure(Call<AllEventResponse> call, Throwable t) {
                t.printStackTrace();
                //showErrorView(t);
            }
        });
    }

    private void getAllEventNextPage() {
        Call<AllEventResponse> popular = RetrofitClient
                .getInstance()
                .getApi()
                .getAllSearchEvent(eventLocationTextView.getText().toString(),LIMIT,currentOffset);
        popular.enqueue(new Callback<AllEventResponse>() {
            @Override
            public void onResponse(Call<AllEventResponse> call, retrofit2.Response<AllEventResponse> response) {
                if (response.isSuccessful()) {
                    isLoadingAllEvent = false;
                    List<AllEventResult> results = fetchResultsAllEvent(response);
                    for (int i = 0 ; i<results.size() ; i++) {
                        if(!(results.get(i).getId() == eventId)){
                            adapterSuggestedEvent.add(results.get(i));
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<AllEventResponse> call, Throwable t) {
                t.printStackTrace();
                //showErrorView(t);
            }
        });
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

                if(isLoggedIn){
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
                }else{
                    eventJoinTextView.setText("Sign In");
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
                eventCost = allEventResult.getCost();
                eventHost = allEventResult.getHost();
                eventImage = allEventResult.getImage();
                eventDate = allEventResult.getDate();
                getAllEvent(allEventResult.getLocation());
                //Loader.off();
            }

            @Override
            public void onFailure(@NonNull Call<AllEventResult> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(),t.toString(),Toast.LENGTH_LONG).show();
                //Loader.off();
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
        eventPrice.setText(String.valueOf(eventCost));

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
