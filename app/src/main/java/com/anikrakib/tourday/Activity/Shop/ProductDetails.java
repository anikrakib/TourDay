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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.anikrakib.tourday.Fragment.Search.ProductSearchAll;
import com.anikrakib.tourday.Models.Shop.ProductResult;
import com.anikrakib.tourday.R;
import com.anikrakib.tourday.RoomDatabse.MyDatabase;
import com.anikrakib.tourday.RoomDatabse.ProductWishListDatabaseTable;
import com.anikrakib.tourday.Utils.ApiURL;
import com.anikrakib.tourday.WebService.RetrofitClient;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.google.android.material.snackbar.Snackbar;
import com.kishandonga.csbx.CustomSnackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetails extends AppCompatActivity {
    ImageView imageView;
    TextView nameTv,priceTv,stockTv,categoryTv,qtyTv,detailsTv;
    ImageButton inc,dec;
    Intent intent;
    int productId;
    ProgressBar progressBar;
    ImageView wishList;
    String currentUserId;
    boolean isLoggedIn;
    MyDatabase myDatabase;


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
        progressBar = findViewById(R.id.progressBar);
        wishList = findViewById(R.id.imgFav);

        if(loadNightModeState()){
            getWindow().setStatusBarColor(getResources().getColor(R.color.black));
        }

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        SharedPreferences userPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        currentUserId = userPref.getString("id","");
        isLoggedIn = userPref.getBoolean("isLoggedIn",false);

        intent = getIntent();
        Bundle bundle = intent.getExtras();
        myDatabase = MyDatabase.getInstance(this);

        productId = bundle.getInt("productId");

        getProductDetails(productId);

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

        wishList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductWishListDatabaseTable productWishListDatabaseTable = new ProductWishListDatabaseTable();
                productWishListDatabaseTable.setProductId(productId);
                productWishListDatabaseTable.setUser_id(currentUserId);

                if(myDatabase.favouriteEventDatabaseDao().addProductWishListByUserId(currentUserId,productId)!=1){
                    myDatabase.favouriteEventDatabaseDao().insert(productWishListDatabaseTable);
                    wishList.setImageResource(R.drawable.ic_like);
                    snackBar("Product Added WishList",R.color.white);
                }else{
                    myDatabase.favouriteEventDatabaseDao().deleteFromProductWishList(currentUserId,productId);
                    wishList.setImageResource(R.drawable.ic_unlike);
                    snackBar("Product Removed WishList",R.color.white);
                }
            }
        });
    }

    public void getProductDetails(int productId){
        Call<ProductResult> call = RetrofitClient
                .getInstance()
                .getApi()
                .getProductView(productId);
        call.enqueue(new Callback<ProductResult>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<ProductResult> call, Response<ProductResult> response) {
                ProductResult productResult = response.body();

//                // load event thumbnail
                Glide.with(getApplicationContext())
                        .load(ApiURL.IMAGE_BASE+productResult.getImage())
                        .error(Glide.with(getApplicationContext())
                                .load("https://i.pinimg.com/originals/a7/46/df/a746dfd74e09d8c7cbcdfa7be02a6250.gif"))
                        .transforms(new CenterCrop(),new RoundedCorners(16))
                        .into(imageView);

                nameTv.setText(productResult.getName());
                categoryTv.setText("Category : "+productResult.getProductType());
                if(!productResult.getDigital()){
                    stockTv.setBackgroundResource(R.drawable.in_stock_bg);
                }else {
                    stockTv.setText("Out of Stock");
                    stockTv.setBackgroundResource(R.drawable.out_stock_bg);
                }

                detailsTv.setText(productResult.getDescription());
                priceTv.setText("৳ "+productResult.getPrice());
                qtyTv.setText(1+"");
                progressBar.setVisibility(View.GONE);

                if (myDatabase.favouriteEventDatabaseDao().addProductWishListByUserId(currentUserId,productId) == 1){
                    wishList.setImageResource(R.drawable.ic_like);
                }else {
                    wishList.setImageResource(R.drawable.ic_unlike);
                }

            }

            @Override
            public void onFailure(Call<ProductResult> call, Throwable t) {

            }
        });
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