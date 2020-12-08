package com.anikrakib.tourday.Activity.Shop;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.anikrakib.tourday.R;
import com.anikrakib.tourday.Utils.ApiURL;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

public class ProductDetails extends AppCompatActivity {
    ImageView imageView;
    TextView nameTv,priceTv,stockTv,categoryTv,qtyTv,detailsTv;
    ImageButton inc,dec;
    Intent intent;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        imageView = findViewById(R.id.productImage);
        nameTv = findViewById(R.id.txtproductname);
        priceTv = findViewById(R.id.txtprice);
        stockTv = findViewById(R.id.tvStock);
        categoryTv = findViewById(R.id.category);
        qtyTv = findViewById(R.id.txtqty);
        detailsTv = findViewById(R.id.productDetails);
        inc = findViewById(R.id.btnaddqty);
        dec = findViewById(R.id.btnminusqty);

        if(loadNightModeState()){
            getWindow().setStatusBarColor(getResources().getColor(R.color.black));
        }

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        intent = getIntent();
        Bundle bundle = intent.getExtras();

        String name = bundle.getString("name");
        String category = bundle.getString("category");
        String qty = bundle.getString("qty");
        String details = bundle.getString("details");
        String imageUrl = bundle.getString("imageUrl");

        int price = bundle.getInt("price");
        boolean stock = bundle.getBoolean("stock");

        // load event thumbnail
        Glide.with(getApplicationContext())
                .load(ApiURL.IMAGE_BASE+imageUrl)
                .error(Glide.with(getApplicationContext())
                        .load("https://i.pinimg.com/originals/a7/46/df/a746dfd74e09d8c7cbcdfa7be02a6250.gif"))
                .transforms(new CenterCrop(),new RoundedCorners(16))
                .into(imageView);

        nameTv.setText(name);
        categoryTv.setText("Category : "+category);
        if(!stock){
            stockTv.setBackgroundResource(R.drawable.in_stock_bg);
        }else {
            stockTv.setText("Out of Stock");
            stockTv.setBackgroundResource(R.drawable.out_stock_bg);
        }

        detailsTv.setText(R.string.bio+"\n\n\nThis T-Shirt for Men's comfortable and can be worn for regular use. It is a perfect wear for men like you. You will love to wear this luxurious and colorful shirt just for its versatile usability and diversified fashion sense. It is generally made of a light, great quality cotton fabrics and is easy to clean. It is perfect to wear with jeans and gabardine pant. Longsleevedesign with a regular fit for men. It is very versatile because it is useful on formal as well as casual occasion. It is designed to be comfortable and durable.");
        priceTv.setText("৳ "+price);
        qtyTv.setText(qty);

        inc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = Integer.parseInt(qtyTv.getText().toString());
                quantity++;
                qtyTv.setText(quantity+"");
            }
        });

        dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = Integer.parseInt(qtyTv.getText().toString());
                quantity--;
                if(quantity <0){
                    qtyTv.setText("0");
                }else{
                    qtyTv.setText(quantity+"");
                }
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

    // this method will load the Night Mode State
    public Boolean loadNightModeState (){
        SharedPreferences userPref = getApplicationContext().getSharedPreferences("nightMode", Context.MODE_PRIVATE);
        return userPref.getBoolean("night_mode",false);
    }
}