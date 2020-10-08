package com.anikrakib.tourday.Activity;
import androidx.appcompat.app.AlertDialog;
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
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.anikrakib.tourday.Adapter.ViewProfilePagerAdapter;
import com.anikrakib.tourday.R;
import com.anikrakib.tourday.WebService.RetrofitClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyProfileActivity extends AppCompatActivity {

    ImageButton profileBackButton;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewProfilePagerAdapter viewProfilePagerAdapter;
    ImageView chooseImage_ImageView,facebookLinkImageView,instagramLinkImageView,bangladeshImageView,editNameImageView;
    Dialog myDialog;
    FloatingActionButton floatingActionButtonCreatePost;
    Button uploadButton,saveButton;
    EditText socialMediaLinkEditText,postPopUpTitle,postPopUpDescription,nameEditTest;
    TextView userFullName,facebookLink,instagramLink;
    private static MyProfileActivity instance;
    CircleImageView userProfilePic;
    private static final int INTENT_REQUEST_CODE = 100;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99 ;
    private static final int CAPTURE_REQUEST_CODE = 0;
    private static final int SELECT_REQUEST_CODE = 1;



    /** Either on the constructor or the 'OnCreate' method, you should add: */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

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


        myDialog = new Dialog(this);
        instance = this;

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);


        showUserData();

        /////*     Click Listener     */////

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
                startActivity(new Intent(MyProfileActivity.this, ExploreActivity.class));

            }
        });
        editNameImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditNamePopUp(userFullName.getText().toString());
            }
        });

        /////*     initialize view   */////
        viewPager = findViewById(R.id.viewPager);

        /////*     initialize ViewPager   */////
        viewProfilePagerAdapter = new ViewProfilePagerAdapter(getSupportFragmentManager());

        /////*     add adapter to ViewPager  */////
        viewPager.setAdapter(viewProfilePagerAdapter);
        tabLayout = findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabRippleColor(null);

        /////*     Check SocialMediaLink is null or not   */////



    }

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
        startActivity(new Intent(MyProfileActivity.this, ExploreActivity.class));
        MyProfileActivity.this.finish();
    }

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

    public void createPostPopUp() {
        ImageButton postCloseButton;
        Animation top_to_bottom;
        CircleImageView userProfilePictureCircleImageView;
        final ConstraintLayout createPostLayout;
        final String[] postDescriptionSave = new String[1];

        myDialog.setContentView(R.layout.create_post);
        postCloseButton= myDialog.findViewById(R.id.postCloseButton);
        createPostLayout = myDialog.findViewById(R.id.createPostLayout);
        postPopUpDescription = myDialog.findViewById(R.id.popup_description);
        userProfilePictureCircleImageView = myDialog.findViewById(R.id.userProfilePicture);

        top_to_bottom = AnimationUtils.loadAnimation(this, R.anim.top_to_bottom);


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String postDescription = sharedPreferences.getString("postDescription","");
        SharedPreferences userPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        String userProfilePicture = userPref.getString("userProfilePicture","");

        postPopUpDescription.setText(postDescription);
        Picasso.get().load("https://tourday.team"+userProfilePicture).into(userProfilePictureCircleImageView);


        postCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Save Post Title and Description in SharedPreferences when close Post PopUp

                postDescriptionSave[0] = postPopUpDescription.getText().toString();
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("postDescription", postDescriptionSave[0]);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INTENT_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {
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

