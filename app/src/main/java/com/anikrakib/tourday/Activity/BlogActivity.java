package com.anikrakib.tourday.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.Html;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.anikrakib.tourday.Adapter.DivisionAdapter;
import com.anikrakib.tourday.Adapter.ViewBlogPagerAdapter;
import com.anikrakib.tourday.Models.DivisionModelItem;
import com.anikrakib.tourday.R;
import com.anikrakib.tourday.WebService.RetrofitClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.marozzi.roundbutton.RoundButton;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.richeditor.RichEditor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import petrov.kristiyan.colorpicker.ColorPicker;
import retrofit2.Call;
import retrofit2.Callback;

public class BlogActivity extends AppCompatActivity {

    ViewPager2 viewPager;
    DivisionAdapter adapter;
    List<DivisionModelItem> models;
    ViewBlogPagerAdapter viewBlogPagerAdapter;
    ViewPager viewPagerBlog;
    TabLayout tabLayoutBlog;
    FloatingActionButton createBlog;
    Dialog myDialog,previewDialog;
    EditText blogPopUpTitle;
    Resources resources;
    String[] division;
    InputStream blogPostInputStream;
    ImageView blogImageView;
    TextView preview;
    private static String data;
    private static final int INTENT_REQUEST_CODE = 100;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);

        viewPager = findViewById(R.id.viewPager);
        createBlog = findViewById(R.id.fabButtonCreateBlog);



        resources= getResources();
        division = resources.getStringArray(R.array.bdDivision);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        myDialog = new Dialog(this);
        previewDialog = new Dialog(this);



        /////*     initialize view   */////
        viewPagerBlog = (ViewPager) findViewById(R.id.viewPagerBlog);

        /////*     initialize ViewPager   */////
        viewBlogPagerAdapter = new ViewBlogPagerAdapter(getSupportFragmentManager());

        /////*     add adapter to ViewPager  */////
        viewPagerBlog.setAdapter(viewBlogPagerAdapter);
        tabLayoutBlog = (TabLayout) findViewById(R.id.slidingTabsBlog);
        tabLayoutBlog.setupWithViewPager(viewPagerBlog);
        tabLayoutBlog.setTabRippleColor(null);


        models = new ArrayList<>();

        models.add(new DivisionModelItem(R.drawable.sylhet,"Sylhet"));
        models.add(new DivisionModelItem(R.drawable.dhaka,"Dhaka"));
        models.add(new DivisionModelItem(R.drawable.chattogram,"Chattogram"));
        models.add(new DivisionModelItem(R.drawable.rangpur,"Rangpur"));
        models.add(new DivisionModelItem(R.drawable.rajshahi,"Rajshahi"));
        models.add(new DivisionModelItem(R.drawable.mymensingh,"Mymensingh"));
        models.add(new DivisionModelItem(R.drawable.barishal,"Barishal"));
        models.add(new DivisionModelItem(R.drawable.khulna,"Khulna"));


        adapter = new DivisionAdapter(this,models);

        viewPager.setAdapter(adapter);

        viewPager.setClipToPadding(false);
        viewPager.setClipChildren(false);
        viewPager.setOffscreenPageLimit(3);
        viewPager.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(10));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1- Math.abs(position);
                page.setScaleY(.80f + r * 0.20f);
            }
        });
        viewPager.setPageTransformer(compositePageTransformer);



        createBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createEventPopUp();


                if (0 != (getApplication().getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE)) {
                    WebView.setWebContentsDebuggingEnabled(true);
                }

            }
        });

    }


    private void createEventPopUp() {
        ImageButton postCloseButton;
        ImageView descriptionPreview;
        Animation top_to_bottom;
        RichEditor blogTextEditor;
        CircleImageView userProfilePicturePopUP;
        Spinner divisionSpinner;
        TextView popUpBlogLocationTextView,textPreview;
        RoundButton createBlog;

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
        textPreview = myDialog.findViewById(R.id.text);


        blogTextEditor.setEditorFontColor(R.color.color_primary_text);
        blogTextEditor.setPlaceholder("Write Here your Blog .....");


        //set Animation
        top_to_bottom = AnimationUtils.loadAnimation(this, R.anim.top_to_bottom);

        // Retrieve and set Event Title and Description from SharedPreferences when again open CreateEvent PopUp
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String userProfilePicture = userPref.getString("userProfilePicture","");
        String blogTitle = userPref.getString("blogTitle","");
        String blogDescription = userPref.getString("blogDescription","");


        //set data in element
        blogPopUpTitle.setText(blogTitle);
        blogTextEditor.setHtml(blogDescription);
        Picasso.get().load("https://tourday.team"+userProfilePicture).into(userProfilePicturePopUP);
        // set value in district spinner
        ArrayAdapter<String> arrayAdapterDivision = new ArrayAdapter<String>(this,R.layout.custom_district_spinner_item,R.id.districtNameTextView,division);
        divisionSpinner.setAdapter(arrayAdapterDivision);


        findVieByIdPopUpMethod(myDialog,blogTextEditor,getApplicationContext());


        // onClick Listener
        blogImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(BlogActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, 100);
                } else {
                    ActivityCompat.requestPermissions(BlogActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 44);
                }

            }
        });

        descriptionPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), blogTextEditor.getHtml(), Toast.LENGTH_LONG).show();

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
                String blogDescriptionString = Html.fromHtml(blogTextEditor.getHtml()).toString();
                editor.putString("blogDescription",blogDescriptionString);
                editor.putString("blogTitle",blogPopUpTitle.getText().toString());
                editor.apply();

                myDialog.dismiss();
            }
        });

        createBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    createBlog(getBytes(blogPostInputStream),blogTextEditor.getHtml(),blogPopUpTitle.getText().toString(),popUpBlogLocationTextView.getText().toString());
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

    public void findVieByIdPopUpMethod(Dialog myDialog, RichEditor blogTextEditor, Context context){

        myDialog.findViewById(R.id.action_redo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blogTextEditor.redo();
            }
        });

        myDialog.findViewById(R.id.action_undo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blogTextEditor.undo();
            }
        });

        myDialog.findViewById(R.id.action_bold).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blogTextEditor.setBold();
            }
        });

        myDialog.findViewById(R.id.action_Italic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blogTextEditor.setItalic();
            }
        });

        myDialog.findViewById(R.id.action_subscript).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blogTextEditor.setSubscript();
            }
        });

        myDialog.findViewById(R.id.action_superscript).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blogTextEditor.setSuperscript();
            }
        });

        myDialog.findViewById(R.id.action_strikethrough).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blogTextEditor.setStrikeThrough();
            }
        });

        myDialog.findViewById(R.id.action_underline).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blogTextEditor.setUnderline();
            }
        });

        myDialog.findViewById(R.id.action_h1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blogTextEditor.setHeading(1);
            }
        });

        myDialog.findViewById(R.id.action_h2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blogTextEditor.setHeading(2);
            }
        });

        myDialog.findViewById(R.id.action_h3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blogTextEditor.setHeading(3);
            }
        });

        myDialog.findViewById(R.id.action_h4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blogTextEditor.setHeading(4);
            }
        });

        myDialog.findViewById(R.id.action_h5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blogTextEditor.setHeading(5);
            }
        });

        myDialog.findViewById(R.id.action_h6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blogTextEditor.setHeading(6);
            }
        });

        myDialog.findViewById(R.id.action_color).setOnClickListener(new View.OnClickListener() {
            private boolean isChanged;

            @Override
            public void onClick(View v) {
                final ColorPicker colorPicker = new ColorPicker(BlogActivity.this);
                ArrayList <String> colors = new ArrayList<>();
                colors.add("#258174");
                colors.add("#3C8D2F");
                colors.add("#20724f");
                colors.add("#6a3ab2");
                colors.add("#323299");
                colors.add("#808000");
                colors.add("#b77231");
                colors.add("#966d37");
                colors.add("#FFFFFF");
                colors.add("#000000");
                colorPicker.setColors(colors)
                        .setColumns(5)
                        .setRoundColorButton(true)
                        .setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
                            @Override
                            public void onChooseColor(int position, int color) {
                                blogTextEditor.setTextColor(isChanged ? Color.TRANSPARENT : color);
                                isChanged = !isChanged;
                            }

                            @Override
                            public void onCancel() {

                            }
                        })
                        .show();
            }
        });

        myDialog.findViewById(R.id.backgroundColor).setOnClickListener(new View.OnClickListener() {
            private boolean isChanged;

            @Override
            public void onClick(View v) {
                final ColorPicker colorPicker = new ColorPicker(BlogActivity.this);
                ArrayList <String> colors = new ArrayList<>();
                colors.add("#258174");
                colors.add("#3C8D2F");
                colors.add("#20724f");
                colors.add("#6a3ab2");
                colors.add("#323299");
                colors.add("#808000");
                colors.add("#b77231");
                colors.add("#966d37");
                colors.add("#FFFFFF");
                colors.add("#000000");
                colorPicker.setColors(colors)
                        .setColumns(5)
                        .setRoundColorButton(true)
                        .setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
                            @Override
                            public void onChooseColor(int position, int color) {
                                blogTextEditor.setTextBackgroundColor(isChanged ? Color.TRANSPARENT : color);
                                isChanged = !isChanged;
                            }

                            @Override
                            public void onCancel() {

                            }
                        })
                        .show();
            }

        });

        myDialog.findViewById(R.id.action_indent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blogTextEditor.setIndent();
            }
        });

        myDialog.findViewById(R.id.action_outdent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blogTextEditor.setOutdent();
            }
        });

        myDialog.findViewById(R.id.action_align_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blogTextEditor.setAlignLeft();
            }
        });

        myDialog.findViewById(R.id.action_align_center).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blogTextEditor.setAlignCenter();
            }
        });

        myDialog.findViewById(R.id.action_align_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blogTextEditor.setAlignRight();
            }
        });

        myDialog.findViewById(R.id.action_blockQuote).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blogTextEditor.setBlockquote();
            }
        });

        myDialog.findViewById(R.id.action_unordered_numbered).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blogTextEditor.setBullets();
            }
        });

        myDialog.findViewById(R.id.action_ordered_numbered).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blogTextEditor.setNumbers();
            }
        });

        myDialog.findViewById(R.id.action_insert_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blogTextEditor.insertImage("https://raw.githubusercontent.com/wasabeef/art/master/chip.jpg",
                        "dachshund", 320);
            }
        });

        myDialog.findViewById(R.id.action_insert_link).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blogTextEditor.insertYoutubeVideo("https://www.youtube.com/embed/pS5peqApgUA");
            }
        });

        myDialog.findViewById(R.id.action_insert_link).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blogTextEditor.insertLink("https://github.com/wasabeef", "wasabeef");
            }
        });
    }

    public void createBlog(byte[] imageBytes,String blogDescription,String blogTitle,String blogDivision){
        SharedPreferences userPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        String token = userPref.getString("token","");

        RequestBody requestFile = RequestBody.create(MediaType.parse("image"), imageBytes);
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"),blogDescription );
        RequestBody location = RequestBody.create(MediaType.parse("text/plain"),blogDivision );
        RequestBody title = RequestBody.create(MediaType.parse("text/plain"),blogTitle );

        MultipartBody.Part postImage = MultipartBody.Part.createFormData("image", "image.jpg", requestFile);

        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .createBlog("Token "+token,title,description,postImage,location);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    DynamicToast.makeSuccess(getApplicationContext(), "Blog Created").show();
                    myDialog.dismiss();
                    // post description shared pref removed
                    SharedPreferences userPref =getApplicationContext().getSharedPreferences("user", MODE_PRIVATE);
                    @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = userPref.edit();
                    editor.putString("blogDescription","");
                    editor.putString("blogTitle","");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INTENT_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Uri selectedImage = data.getData();
                try {
                    blogPostInputStream = getContentResolver().openInputStream(data.getData());
                    blogImageView.setImageURI(selectedImage);
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