package com.anikrakib.tourday.Activity.Profile;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.anikrakib.tourday.Adapter.Profile.ViewProfilePagerAdapter;
import com.anikrakib.tourday.R;
import com.anikrakib.tourday.WebService.RetrofitClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.marozzi.roundbutton.RoundButton;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyProfileActivity extends AppCompatActivity{

    /*     initialize variable   */
    ImageButton profileBackButton;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewProfilePagerAdapter viewProfilePagerAdapter;
    ImageView chooseImage_ImageView,facebookLinkImageView,instagramLinkImageView,bangladeshImageView,editNameImageView;
    Dialog myDialog;
    FloatingActionButton floatingActionButtonCreatePost;
    Button uploadButton,saveButton;
    EditText socialMediaLinkEditText,postPopUpTitle,postPopUpDescription,nameEditTest;
    TextView userFullName,facebookLink,instagramLink,createPostDate;
    private static MyProfileActivity instance;
    CircleImageView userProfilePic;
    private static final int INTENT_REQUEST_CODE = 100;
    Resources resources;
    String[] districtKeys;
    InputStream postInputStream;
    Boolean createPostImageClick = false;
    ImageView postImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        /*     initialize view   */
        profileBackButton = findViewById(R.id.profileBackButton);
        facebookLinkImageView = findViewById(R.id.facebookLinkImageView);
        instagramLinkImageView = findViewById(R.id.instagramLinkImageView);
        bangladeshImageView = findViewById(R.id.bangladeshImageView);
        floatingActionButtonCreatePost = findViewById(R.id.fabButtonCreatePost);
        userFullName = findViewById(R.id.userFullName);
        facebookLink = findViewById(R.id.facebookLinkTextView);
        instagramLink = findViewById(R.id.instagramLinkTextView);
        userProfilePic = findViewById(R.id.userProfilePic);
        editNameImageView = findViewById(R.id.editNameImageView);
        chooseImage_ImageView = findViewById(R.id.chooseImage_ImageView);
        viewPager = findViewById(R.id.viewPager);


        resources= getResources();
        districtKeys = resources.getStringArray(R.array.bdDistrict);

        myDialog = new Dialog(this);
        instance = this;

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);


        showUserData();

        /////*     initialize ViewPager   */////
        viewProfilePagerAdapter = new ViewProfilePagerAdapter(getSupportFragmentManager());

        /////*     add adapter to ViewPager  */////
        viewPager.setAdapter(viewProfilePagerAdapter);
        tabLayout = findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabRippleColor(null);

        /////*     Check SocialMediaLink is null or not   */////




        /*    On Click Listener     */

        chooseImage_ImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ActivityCompat.checkSelfPermission(MyProfileActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, 100);
                } else {
                    ActivityCompat.requestPermissions(MyProfileActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 44);
                }

            }
        });

        facebookLinkImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSocialMediaPopup(v.getId());
            }
        });
        instagramLinkImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSocialMediaPopup(v.getId());
            }
        });
        bangladeshImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBdMap();
            }
        });
        floatingActionButtonCreatePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPostPopUp();
            }
        });

        profileBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(MyProfileActivity.this, ExploreActivity.class));
                onBackPressed();
            }
        });
        editNameImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditNamePopUp(userFullName.getText().toString());
            }
        });

    }

    //this method show pop to edit user name
    private void showEditNamePopUp(String name) {
        ImageView close;

        myDialog.setContentView(R.layout.custom_edit_name_pop_up);
        nameEditTest = myDialog.findViewById(R.id.nameEditText);
        close = myDialog.findViewById(R.id.closeButtonEditNamePopUp);
        saveButton = myDialog.findViewById(R.id.saveButton);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        nameEditTest.setText(name);

        nameEditTest.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInputsEditName();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        saveButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                //rest of the work here about edit name
                if(!TextUtils.isEmpty(nameEditTest.getText().toString())){
                    updateName();
                    myDialog.dismiss();
                    userFullName.setText(nameEditTest.getText().toString());
                }else{
                    DynamicToast.makeError(getApplicationContext(), "Name Can't Empty!").show();
                }
            }
        });


        myDialog.setCancelable(false);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    /*
        Api-> (POST) "https://www.tourday.team/api/profile/"
        Token is required. Add token with request header. Send only specific filed to update it.
        Ex: If you want to update only name filed, just send name in the request. Keep other field blank.

        Response:
            {
            }
     */
    private void updateName() {
        SharedPreferences userPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        String token = userPref.getString("token","");
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .updateProfileName("Token "+token,nameEditTest.getText().toString().trim());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    DynamicToast.makeSuccess(getApplicationContext(), "Name Update Successfully").show();
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.body().string());
                        JSONObject profile = jsonObject.getJSONObject("profile");
                        userFullName.setText(profile.getString("name"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    DynamicToast.makeError(getApplicationContext(), "Something Wrong!").show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    //this method show pop to edit socialMedia Link
    public void showSocialMediaPopup(final int id) {
        ImageView close;

        myDialog.setContentView(R.layout.custom_social_media_link_pop_up);
        socialMediaLinkEditText = myDialog.findViewById(R.id.socialMediaLinkEditText);
        close = myDialog.findViewById(R.id.txtclose);
        uploadButton = myDialog.findViewById(R.id.uploadButton);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });


        socialMediaLinkEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInputsSocialMediaLink();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        uploadButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
                if(id == R.id.facebookLinkImageView){
                    facebookLinkImageView.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.dark_blue));
                }else if(id == R.id.instagramLinkImageView){
                    instagramLinkImageView.setColorFilter(ContextCompat.getColor(getApplicationContext(),  R.color.instagram_color));
                }

            }
        });

        if(id == R.id.facebookLinkImageView){
            socialMediaLinkEditText.setHint("Enter Your Facebook URl");
        }else if(id == R.id.instagramLinkImageView){
            socialMediaLinkEditText.setHint("Enter Your Instagram URl");
        }

        myDialog.setCancelable(false);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    //this method show pop to create post
    public void createPostPopUp() {
        ImageButton postCloseButton;
        RoundButton createPostButton;
        Animation top_to_bottom;
        CircleImageView userProfilePictureCircleImageView;
        final ConstraintLayout createPostLayout;
        Spinner districtSpinner;
        SharedPreferences userPref =getApplicationContext().getSharedPreferences("user", MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = userPref.edit();



        myDialog.setContentView(R.layout.create_post);
        postCloseButton= myDialog.findViewById(R.id.postCloseButton);
        createPostLayout = myDialog.findViewById(R.id.createPostLayout);
        postPopUpDescription = myDialog.findViewById(R.id.popup_description);
        userProfilePictureCircleImageView = myDialog.findViewById(R.id.userProfilePicture);
        districtSpinner = myDialog.findViewById(R.id.districtSpinner);
        createPostButton = myDialog.findViewById(R.id.createPostButton);
        createPostDate = myDialog.findViewById(R.id.createPostDate);
        postImageView = myDialog.findViewById(R.id.postImageView);

        // set animation in create post pop up
        top_to_bottom = AnimationUtils.loadAnimation(this, R.anim.top_to_bottom);


        //set user profile pic and draft description using Shared Preference
        String userProfilePicture = userPref.getString("userProfilePicture","");
        String postDescription = userPref.getString("postDescription","");

        postPopUpDescription.setText(postDescription);
        Picasso.get().load("https://tourday.team"+userProfilePicture).into(userProfilePictureCircleImageView);

        //get current date and set date textView
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = df.format(date);
        createPostDate.setText(formattedDate);

        // set value in district spinner
        ArrayAdapter<String> arrayAdapterDivision = new ArrayAdapter<String>(this,R.layout.custom_district_spinner_item,R.id.districtNameTextView,districtKeys);
        districtSpinner.setAdapter(arrayAdapterDivision);


        // onClick Listener
        createPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!districtHashMap(districtSpinner.getSelectedItem().toString()).isEmpty()){
                    try {
                        createPost(createPostDate.getText().toString(),postPopUpDescription.getText().toString(),districtHashMap(districtSpinner.getSelectedItem().toString()),getBytes(postInputStream));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    DynamicToast.makeWarning(getApplicationContext(), "Select District").show();
                }
            }
        });

        postImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPostImageClick = true;

                if (ActivityCompat.checkSelfPermission(MyProfileActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, 100);
                } else {
                    ActivityCompat.requestPermissions(MyProfileActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 44);
                }

            }
        });

        postCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save Post Title and Description in SharedPreferences when close Post PopUp

                editor.putString("postDescription",postPopUpDescription.getText().toString());
                editor.apply();

                myDialog.dismiss();
            }
        });


        createPostLayout.startAnimation(top_to_bottom);

        myDialog.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT,Toolbar.LayoutParams.WRAP_CONTENT);
        myDialog.getWindow().getAttributes().gravity = Gravity.TOP;
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.setCancelable(false);
        myDialog.show();

    }

    /*
        Api-> (POST) "https://www.tourday.team/api/post/"

        Use Location number instead of full location name.
        location key & Value
        {
            'Panchagarh' : 1,'Thakurgaon' : 2,'Dinajpur' : 4,'Nilphamari' : 0,'Lalmonirhat' : 3,'Kurigram' : 6,'Rangpur' : 5,'Gaibandha' : 7,'Joypurhat' : 16,'Bogra' : 11,'Naogaon' : 15,'Natore' : 12,
            'Chapainawabganj' : 10,'Pabna' : 9,'Rajshahi' : 13,'Sirajganj' : 14,'Jamalpur' : 66,'Mymensingh' : 67,'Netrokona ' : 68,'Sherpur' : 66a,'Habiganj' : 64,'Moulvibazar' : 65,'Sunamganj' : 62,
            'Sylhet' : 63,'Dhaka' : 73,'Faridpur' : 70,'Gazipur' : 76,'Gopalganj' : 71,'Kishoreganj' : 83,'Madaripur' : 75,'Manikganj' : 72,'Munshiganj' : 79,'Narayanganj' : 78,'Narsingdi' : 77,'Rajbari' : 69,
            'Shariatpur' : 74,'Tangail' : 84,'Jessore' : 19,'Jhenaidah' : 17,'Khulna' : 22,'Kushtia' : 23,'Magura' : 18,'Meherpur' : 24,'Narail' : 20,'Satkhira' : 21,'Bagerhat' : 26,'Chuadanga' : 25,'Barguna' : 32,
            'Barisal' : 31,'Bhola' : 37,'Jhalokati' : 30,'Patuakhali' : 33,'Pirojpur' : 29,'Chattogram' : 54,'Bandarban' : 56,'Brahmanbaria' : 41,'Chandpur' : 44,'Comilla' : 43,'Cox's Bazar' : 59,'Feni' : 51,
            'Khagrachhari' : 55,'Lakshmipur' : 45,'Noakhali' : 46,'Rangamati' : 57
        }

        Response:
        {
            "post": "post text here",
            "location": 1,
            "date": "yyyy-mm-dd",
            "image": image.jpg
        }

    */
    public void createPost(String posDate, String postDescription, String districtCode, byte[] imageBytes){
        SharedPreferences userPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        String token = userPref.getString("token","");

        RequestBody requestFile = RequestBody.create(MediaType.parse("image"), imageBytes);
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"),postDescription );
        RequestBody location = RequestBody.create(MediaType.parse("text/plain"),districtCode );
        RequestBody date = RequestBody.create(MediaType.parse("text/plain"),posDate );

        MultipartBody.Part postImage = MultipartBody.Part.createFormData("image", "image.jpg", requestFile);

        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .createPost("Token "+token,description,location,date,postImage);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    DynamicToast.makeSuccess(getApplicationContext(), "Post Created").show();
                    myDialog.dismiss();
                    // post description shared pref removed
                    SharedPreferences userPref =getApplicationContext().getSharedPreferences("user", MODE_PRIVATE);
                    @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = userPref.edit();
                    editor.putString("postDescription","");
                    editor.apply();
                } else {
                    DynamicToast.makeError(getApplicationContext(), "Something Wrong").show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public String districtHashMap(String districtKey){
        String districtCode = "";
        Resources resources = getResources();

        HashMap<String, String> meMap=new HashMap<String, String>();

        String[] keys = resources.getStringArray(R.array.bdDistrict);
        String[] values = resources.getStringArray(R.array.bdDistrictLocationCode);


        for(int i = 0; i < keys.length; i++){
            meMap.put(keys[i], values[i]);
        }

        for (String s : meMap.keySet()) {
            districtCode = meMap.get(districtKey);

        }
        return districtCode;
    }

    private void checkInputsSocialMediaLink() {
        if (!TextUtils.isEmpty(socialMediaLinkEditText.getText())) {
            uploadButton.setEnabled(true);
            uploadButton.setBackgroundResource(R.drawable.button_background);
        } else {
            uploadButton.setEnabled(false);
            uploadButton.setBackgroundResource(R.drawable.disable_button_background);
        }
    }

    private void checkInputsEditName() {
        if (!TextUtils.isEmpty(userFullName.getText())) {
            saveButton.setEnabled(true);
            saveButton.setBackgroundResource(R.drawable.button_background);
        } else {
            saveButton.setEnabled(false);
            saveButton.setBackgroundResource(R.drawable.disable_button_background);
        }
    }

    /*
        Api-> (GET) "https://www.tourday.team/api/profile/"
        Token is required. Add token with request header.
        Request:
            {
                "Authorization": "Token 93bc86220b144548e5bb507851b6ef7c2a5e1a14",
            }
            Response:
            {
                "username": "anikrakib",
                "profile": {
                    "name": "Anik Rakib",
                    "email": "anikrakib@gmail.com",
                    "fb": "anik.rakib",
                    "insta": "anik_rakib",
                    "city": "Dhaka",
                    "bio": "Android Developer"
                    "picture": "/media/user/pic.jpg"
                },
                "is_completed": true
            }
     */
    public void showUserData(){
        SharedPreferences userPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        String token = userPref.getString("token","");
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .userProfile("Token "+token);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    SharedPreferences.Editor editor = userPref.edit();
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.body().string());
                        JSONObject profile = jsonObject.getJSONObject("profile");
                        userFullName.setText(profile.getString("name"));
                        facebookLink.setText(profile.getString("fb"));
                        instagramLink.setText(profile.getString("insta"));
                        editor.putString("userProfilePicture",profile.getString("picture"));
                        editor.apply();
                        Picasso.get().load("https://tourday.team"+profile.getString("picture")).into(userProfilePic);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"Token Not Correct",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Fail!",Toast.LENGTH_LONG).show();

            }
        });
    }

    //show user bdMap Profile in Popup
    @SuppressLint("SetJavaScriptEnabled")
    public void showBdMap() {
        ImageView close;
        myDialog.setContentView(R.layout.custom_bd_map_pop_up);
        close = myDialog.findViewById(R.id.socialMediaClose);

        final WebView webView = myDialog.findViewById(R.id.webViewSocialMedia);

        SharedPreferences userPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        String username = userPref.getString("userName","");

        WebSettings webSettings = webView.getSettings();
        webView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        WebViewClient webViewClient = new WebViewClient();
        webView.setWebViewClient(webViewClient);

        webView.loadUrl("https://www.tourday.team/api/map/"+username);
        // set image scale to fit screen if larger than screen width
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) MyProfileActivity.this.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(displayMetrics);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.setCancelable(false);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    /*
        Api-> (POST) "https://www.tourday.team/api/profile/"
        Token is required. Add token with request header. Send only specific filed to update it.
        Ex: If you want to update only name filed, just send name in the request. Keep other field blank.

        Response:
            {
            }
     */
    private void updatePhoto(byte[] imageBytes) {
        SharedPreferences userPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        String token = userPref.getString("token","");

        RequestBody requestFile = RequestBody.create(MediaType.parse("image"), imageBytes);

        MultipartBody.Part body = MultipartBody.Part.createFormData("picture", "image.jpg", requestFile);
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .updateImage("Token "+token,body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {

                if (response.isSuccessful()) {
                    DynamicToast.makeSuccess(getApplicationContext(), "Image Updated").show();
                } else {
                    DynamicToast.makeError(getApplicationContext(), "Something Wrong").show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    // onActivityResult Method for retrieve image from gallery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INTENT_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if(createPostImageClick){
                    Uri selectedImage = data.getData();
                    try {
                        postInputStream = getContentResolver().openInputStream(data.getData());
                        postImageView.setImageURI(selectedImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    Uri selectedImage = data.getData();
                    try {
                        InputStream is = getContentResolver().openInputStream(data.getData());
                        userProfilePic.setImageURI(selectedImage);
                        updatePhoto(getBytes(is));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    // set bytes in InputStream
    public byte[] getBytes(InputStream is) throws IOException {
        ByteArrayOutputStream byteBuff = new ByteArrayOutputStream();

        int buffSize = 1024;
        byte[] buff = new byte[buffSize];

        int len = 0;
        while ((len = is.read(buff)) != -1) {
            byteBuff.write(buff, 0, len);
        }

        return byteBuff.toByteArray();
    }

}
