package com.anikrakib.tourday.Activity.Blog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.anikrakib.tourday.Models.Blog.DeleteBlogResponse;
import com.anikrakib.tourday.Models.Blog.UpdateBlogRequest;
import com.anikrakib.tourday.R;
import com.anikrakib.tourday.WebService.RetrofitClient;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.marozzi.roundbutton.RoundButton;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.richeditor.RichEditor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class YourBlogDetailsActivity extends AppCompatActivity {

    /* Initialize variable */
    ImageButton yourBlogDetailsBackButton;
    Intent intent;
    KenBurnsView yourBlogImageKenBurnsView;
    TextView yourBlogDetailsTitleTextView,yourBlogDetailsDescriptionTextView,yourBlogDetailsDivisionTextView,yourBlogDetailsDateTextView;
    FloatingActionButton editBlog,deleteBlog;
    Dialog myDialog,previewDialog;
    Resources resources;
    String[] division;
    InputStream blogPostInputStream;
    EditText blogPopUpTitle;
    ImageView blogImageView;
    private static final int INTENT_REQUEST_CODE = 100;
    String yourBlogTitle,yourBlogImage,yourBlogDescription,yourBlogDivision,yourBlogDate,yourBlogId;
    Uri selectedImage;
    ImageButton postCloseButton;
    ImageView descriptionPreview;
    Animation top_to_bottom;
    RichEditor blogTextEditor;
    CircleImageView userProfilePicturePopUP;
    Spinner divisionSpinner;
    TextView popUpBlogLocationTextView;
    RoundButton createBlog;


    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_blog_details);

        /* Initialize view */
        yourBlogImageKenBurnsView = findViewById(R.id.yourBlogDetailsImage);
        yourBlogDetailsTitleTextView = findViewById(R.id.yourBlogDetailsTitleTextView);
        yourBlogDetailsDivisionTextView = findViewById(R.id.yourBlogDetailsLocationTextView);
        yourBlogDetailsDescriptionTextView = findViewById(R.id.yourBlogDetailsDescriptionTextView);
        yourBlogDetailsDateTextView = findViewById(R.id.yourBlogDetailsDate);
        yourBlogDetailsBackButton = findViewById(R.id.backButtonBlogDetails);
        editBlog = findViewById(R.id.yourBlogEditFloatingActionButton);
        deleteBlog = findViewById(R.id.yourBlogDeleteFloatingActionButton);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        if (Build.VERSION.SDK_INT >= 23) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        intent = getIntent();
        Bundle extras = intent.getExtras();
        resources= getResources();
        division = resources.getStringArray(R.array.bdDivision);
        myDialog = new Dialog(this);
        previewDialog = new Dialog(this);

        assert extras != null;
        yourBlogId = String.valueOf(extras.getInt("yourBlogId"));

        /* get Signed in user post details from Api using user blogID */
        getYourPostDetails(Integer.parseInt(yourBlogId));

        /* On click Listener */
        yourBlogDetailsBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        deleteBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteBlogPopUp(yourBlogId);
            }
        });
        editBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateBlogPopUp();
            }
        });

    }

    /* This Method delete specific Blog from server using blogId
      API->(DELETE) https://www.tourday.team/api/blog/delete/post_id
      Token is required, Request Token who create a post. Add token with request header.
      Request:
        {
            "Authorization": "Token 93bc86220b144548e5bb507851b6ef7c2a5e1a14",
        }
        Response:
            "Delete Successfully."
    */
    public void deleteBlog(String getId){
        SharedPreferences userPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        String token = userPref.getString("token","");

        Call<DeleteBlogResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .deleteBlog("Token "+token,getId);

        call.enqueue(new Callback<DeleteBlogResponse>() {
            @Override
            public void onResponse(Call<DeleteBlogResponse> call, Response<DeleteBlogResponse> response) {
                if(response.isSuccessful()){
                    String success = response.body().getSuccess();
                    DynamicToast.makeSuccess(getApplicationContext(), success).show();

                }else{
                    DynamicToast.makeError(getApplicationContext(), "Something Wrong !!").show();
                }
            }

            @Override
            public void onFailure(Call<DeleteBlogResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    @SuppressLint("SetTextI18n")
    private void updateBlogPopUp() {


        final ConstraintLayout createEventLayout;
        SharedPreferences userPref =getApplicationContext().getSharedPreferences("user", MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = userPref.edit();


        myDialog.setContentView(R.layout.create_blog);
        postCloseButton = myDialog.findViewById(R.id.createBlogCloseButton);

        createEventLayout = myDialog.findViewById(R.id.createBlogLayout);
        blogPopUpTitle = myDialog.findViewById(R.id.popupBlogTitle);
        blogTextEditor = myDialog.findViewById(R.id.popUpBlogTextEditor);
        userProfilePicturePopUP = myDialog.findViewById(R.id.createBlogPopUpUserProfilePicture);
        divisionSpinner = myDialog.findViewById(R.id.blogLocationSpinner);
        blogImageView = myDialog.findViewById(R.id.postImageView);
        popUpBlogLocationTextView = myDialog.findViewById(R.id.popUpBlogLocationTextView);
        createBlog = myDialog.findViewById(R.id.createBlogButton);
        descriptionPreview = myDialog.findViewById(R.id.descriptionPreView);


        blogTextEditor.setEditorFontColor(R.color.color_primary_text);
        blogTextEditor.setPlaceholder("Write Here your Blog .....");


        //set Animation
        top_to_bottom = AnimationUtils.loadAnimation(this, R.anim.top_to_bottom);



        //set data in element
        createBlog.setText("Update blog");
        String userProfilePicture = userPref.getString("userProfilePicture","");
        Picasso.get().load("https://tourday.team"+userProfilePicture).into(userProfilePicturePopUP);
        getYourPostDetailsInUpdateBlogPopUp(Integer.parseInt(yourBlogId));
        // set value in district spinner
        ArrayAdapter<String> arrayAdapterDivision = new ArrayAdapter<String>(this,R.layout.custom_spinner_item,R.id.districtNameTextView,division);
        divisionSpinner.setAdapter(arrayAdapterDivision);


        BlogActivity blogActivity = new BlogActivity();
        blogActivity.findVieByIdPopUpMethod(myDialog,blogTextEditor,getApplicationContext());


        // onClick Listener
        blogImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(YourBlogDetailsActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, 100);
                } else {
                    ActivityCompat.requestPermissions(YourBlogDetailsActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 44);
                }

            }
        });

        descriptionPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPreview(blogTextEditor.getHtml());

            }
        });

        divisionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View view, int position, long id) {
                popUpBlogLocationTextView.setText(divisionSpinner.getSelectedItem().toString());
            }
            public void onNothingSelected(AdapterView<?> arg0) { }
        });

        postCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myDialog.dismiss();
            }
        });

        createBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    updateBlog(getBytes(blogPostInputStream),yourBlogId,blogTextEditor.getHtml(),blogPopUpTitle.getText().toString(),popUpBlogLocationTextView.getText().toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        createEventLayout.startAnimation(top_to_bottom);

        myDialog.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        myDialog.getWindow().getAttributes().gravity = Gravity.TOP;
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.setCancelable(false);
        myDialog.show();
    }

    /* This Method update specific Blog blogId
      API->(PUT) https://www.tourday.team/api/blog/edit/post_id
      Token is required, Request Token who create a post. Add token with request header.
      Send all required field to update it.
      Request:
          {
              "Authorization": "Token 93bc86220b144548e5bb507851b6ef7c2a5e1a14",
          }
      Response:
          {
               "title": "Blog Post title Here",
               "description": "description here",
               "image": "attach your BLOG image",
               "division": "your division here, Ex. Rajshahi",
          }
    */
    public void updateBlog(byte[] imageBytes,String yourBlogId,String blogDescription,String blogTitle,String blogDivision){
        SharedPreferences userPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        String token = userPref.getString("token","");

        //File file = new File(bitmapToString(bitmap));

        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), imageBytes);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", "image.jpg", reqFile);
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"),blogDescription );
        RequestBody location = RequestBody.create(MediaType.parse("text/plain"),blogDivision );
        RequestBody title = RequestBody.create(MediaType.parse("text/plain"),blogTitle );


        HashMap<String,RequestBody> map = new HashMap<>();
        map.put("description",description);
        map.put("division",location);
        map.put("title",title);


        Call<UpdateBlogRequest> call = RetrofitClient
                .getInstance()
                .getApi()
                .updateBlog("Token "+token,yourBlogId,map,body);
        call.enqueue(new Callback<UpdateBlogRequest>() {
            @Override
            public void onResponse(Call<UpdateBlogRequest> call, retrofit2.Response<UpdateBlogRequest> response) {
                if (response.isSuccessful()) {
                    DynamicToast.makeSuccess(getApplicationContext(), "Blog Updated").show();
                    myDialog.dismiss();
                    yourBlogImageKenBurnsView.setImageURI(selectedImage);
                    yourBlogDetailsTitleTextView.setText(blogTitle);
                    yourBlogDetailsDivisionTextView.setText(blogDescription);
                    yourBlogDetailsDivisionTextView.setText(blogDivision);
                } else {
                    DynamicToast.makeError(getApplicationContext(), "Something Wrong").show();
                }
            }
            @Override
            public void onFailure(Call<UpdateBlogRequest> call, Throwable t) {
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
                selectedImage = data.getData();
                try {
                    blogPostInputStream = getContentResolver().openInputStream(data.getData());
                    blogImageView.setImageURI(selectedImage);
                } catch (IOException e) {
                    e.printStackTrace();
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

    // this method convert user Blog description to HTML and show a custom Pop up Dialog
    public void showPreview(String data){
        TextView previewText;
        previewDialog.setContentView(R.layout.preview_blog_description);

        previewText = previewDialog.findViewById(R.id.previewDescription);

        previewText.setText(data);

        previewDialog.setCancelable(true);
        previewDialog.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        previewDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        previewDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        previewDialog.show();

    }

    /* This Method get specific Blog details from server using blogId
       API-> (GET) https://www.tourday.team/api/blog/user/username
       Response specific user post.
       Response:
           {
                "count": "post count",
                "next": "next page url",
                "previous": "previous page url",
                "results":
                {
                "id": 1,
                "slug": "author name"
                "title": "title name",
                "date": "2020-10-02",
                "description": "Blog post description",
                "image": "/media/blog_pics/about-2.jpg",
                "division":"Rajshahi"
           }
     */
    public void getYourPostDetails(int postId){
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .getPostDetails(postId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    try {
                        JSONObject jsonObject =new JSONObject( response.body().string());

                        int id  = jsonObject.getInt("id");
                        String slug  = jsonObject.getString("slug");
                        String date  = jsonObject.getString("date");
                        String division  = jsonObject.getString("division");
                        String description  = jsonObject.getString("description");
                        String title  = jsonObject.getString("title");
                        String image  = jsonObject.getString("image");
                        Picasso.get().load("https://www.tourday.team"+image).into(yourBlogImageKenBurnsView);
                        yourBlogDetailsTitleTextView.setText(title);
                        yourBlogDetailsDivisionTextView.setText(division);
                        yourBlogDetailsDateTextView.setText(date);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            yourBlogDetailsDescriptionTextView.setText(Html.fromHtml(description, Html.FROM_HTML_MODE_COMPACT));
                        } else {
                            yourBlogDetailsDescriptionTextView.setText(Html.fromHtml(description));
                        }


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }else{
                    Toast.makeText(getApplicationContext(),postId+" Try Again",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Fail!",Toast.LENGTH_LONG).show();

            }
        });
    }

    /* when user want to update Blog then this method retrieved data from server
      and set value in popup  */
    public void getYourPostDetailsInUpdateBlogPopUp(int postId){
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .getPostDetails(postId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    try {
                        JSONObject jsonObject =new JSONObject( response.body().string());

                        String division  = jsonObject.getString("division");
                        String description  = jsonObject.getString("description");
                        String title  = jsonObject.getString("title");
                        String image  = jsonObject.getString("image");

                        blogPopUpTitle.setText(title);
                        blogTextEditor.setHtml(description);
                        popUpBlogLocationTextView.setText(division);
                        Picasso.get().load("https://www.tourday.team"+image).into(blogImageView);

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }else{
                    Toast.makeText(getApplicationContext(),postId+" Try Again",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Fail!",Toast.LENGTH_LONG).show();

            }
        });
    }

    // this method for show custom pop user wants to delete pop up or not
    public void showDeleteBlogPopUp(String blogId) {
        Button yesButton,noButton;
        myDialog.setContentView(R.layout.custom_delete_blog_pop_up);
        yesButton = myDialog.findViewById(R.id.yesButton);
        noButton = myDialog.findViewById(R.id.noButton);

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteBlog(blogId);
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