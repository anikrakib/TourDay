package com.anikrakib.tourday.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageButton;
import com.anikrakib.tourday.Adapter.DivisionAdapter;
import com.anikrakib.tourday.Adapter.ViewBlogPagerAdapter;
import com.anikrakib.tourday.Models.DivisionModelItem;
import com.anikrakib.tourday.R;
import com.fiberlink.maas360.android.richtexteditor.RichEditText;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.richeditor.RichEditor;
import petrov.kristiyan.colorpicker.ColorPicker;

public class BlogActivity extends AppCompatActivity {

    ViewPager2 viewPager;
    DivisionAdapter adapter;
    List<DivisionModelItem> models;
    ViewBlogPagerAdapter viewBlogPagerAdapter;
    ViewPager viewPagerBlog;
    TabLayout tabLayoutBlog;
    FloatingActionButton createBlog;
    Dialog myDialog;
    EditText blogPopUpTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);

        viewPager = findViewById(R.id.viewPager);
        createBlog = findViewById(R.id.fabButtonCreateBlog);



        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        myDialog = new Dialog(this);



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
        Animation top_to_bottom;
        RichEditor blogTextEditor;
        CircleImageView userProfilePicturePopUP;
        final ConstraintLayout createEventLayout;
        SharedPreferences userPref =getApplicationContext().getSharedPreferences("user", MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = userPref.edit();


        myDialog.setContentView(R.layout.create_blog);
        postCloseButton = myDialog.findViewById(R.id.createBlogCloseButton);

        createEventLayout = myDialog.findViewById(R.id.createBlogLayout);
        blogPopUpTitle = myDialog.findViewById(R.id.popupBlogTitle);
        blogTextEditor = myDialog.findViewById(R.id.popUpBlogTextEditor);
        userProfilePicturePopUP = myDialog.findViewById(R.id.createBlogPopUpUserProfilePicture);

        blogTextEditor.setEditorFontColor(R.color.color_primary_text);
        blogTextEditor.setPlaceholder("Write Here your Blog .....");


        //set Animation
        top_to_bottom = AnimationUtils.loadAnimation(this, R.anim.top_to_bottom);

        // Retrieve and set Event Title and Description from SharedPreferences when again open CreateEvent PopUp
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String userProfilePicture = userPref.getString("userProfilePicture","");


        //set data in element
        Picasso.get().load("https://tourday.team"+userProfilePicture).into(userProfilePicturePopUP);

        findVieByIdPopUpMethod(myDialog,blogTextEditor,getApplicationContext());


        postCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                myDialog.dismiss();
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

}