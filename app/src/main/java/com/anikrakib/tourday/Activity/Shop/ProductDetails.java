package com.anikrakib.tourday.Activity.Shop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.anikrakib.tourday.Adapter.Search.AdapterAllProductSearch;
import com.anikrakib.tourday.Models.Shop.ProductResponse;
import com.anikrakib.tourday.Models.Shop.ProductResult;
import com.anikrakib.tourday.R;
import com.anikrakib.tourday.RoomDatabse.MyDatabase;
import com.anikrakib.tourday.RoomDatabse.ProductWishListDatabaseTable;
import com.anikrakib.tourday.RoomDatabse.ShopCartTable;
import com.anikrakib.tourday.Utils.ApiURL;
import com.anikrakib.tourday.Utils.CheckInternet;
import com.anikrakib.tourday.Utils.Share;
import com.anikrakib.tourday.WebService.RetrofitClient;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.google.android.material.snackbar.Snackbar;
import com.kishandonga.csbx.CustomSnackbar;

import java.util.List;

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
    String currentUserId,productType,productImage;
    int productPrice;
    boolean isLoggedIn,availableOrNot,connectToInternet;
    MyDatabase myDatabase;
    LinearLayout addToCart,noInternetLayout,similarProductLayout,shareLayout;
    RecyclerView similarProduct;
    AdapterAllProductSearch adapterAllSimilarProduct;

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
        addToCart = findViewById(R.id.addToCart);
        noInternetLayout = findViewById(R.id.noInternetLayout);
        similarProduct = findViewById(R.id.similarProductRecyclerView);
        similarProductLayout = findViewById(R.id.similarProductLayout);
        shareLayout = findViewById(R.id.lytshare);

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
        connectToInternet = CheckInternet.isConnected(getApplicationContext());

        assert bundle != null;
        productId = bundle.getInt("productId");

        adapterAllSimilarProduct = new AdapterAllProductSearch(getApplicationContext(),true);
        similarProduct.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        similarProduct.setItemAnimator(new DefaultItemAnimator());
        similarProduct.setLayoutManager(linearLayoutManager);
        similarProduct.setAdapter(adapterAllSimilarProduct);
        //similarProduct.setNestedScrollingEnabled(false);

        getProductDetails(productId);
        getAllSimilarProductFirstPage(productType);

//        // check event suggested or not
//        if (adapterAllSimilarProduct.isEmpty()){
//            Toast.makeText(getApplicationContext(),productType+"",Toast.LENGTH_LONG).show();
//        }else {
//            Toast.makeText(getApplicationContext(),"Not Empty",Toast.LENGTH_LONG).show();
//        }

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

        shareLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Share.shareLink(getApplicationContext(),"shop/#"+productId);
            }
        });

        wishList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductWishListDatabaseTable productWishListDatabaseTable = new ProductWishListDatabaseTable();
                productWishListDatabaseTable.setProductId(productId);
                productWishListDatabaseTable.setUser_id(currentUserId);

                if(isLoggedIn){
                    if(myDatabase.favouriteEventDatabaseDao().addProductWishListByUserId(currentUserId,productId)!=1){
                        myDatabase.favouriteEventDatabaseDao().insert(productWishListDatabaseTable);
                        wishList.setImageResource(R.drawable.ic_like);
                        snackBar("Product Added WishList",R.color.white);
                    }else{
                        myDatabase.favouriteEventDatabaseDao().deleteFromProductWishList(currentUserId,productId);
                        wishList.setImageResource(R.drawable.ic_unlike);
                        snackBar("Product Removed WishList",R.color.white);
                    }
                }else{
                    snackBar("Sign In Required",R.color.white);
                }
            }
        });

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(connectToInternet){
                    if(!availableOrNot){
                        ShopCartTable shopCartTable = new ShopCartTable();
                        shopCartTable.setProduct_id(productId);
                        shopCartTable.setProductName(nameTv.getText().toString());
                        shopCartTable.setProductQuantity(Integer.parseInt(qtyTv.getText().toString()));
                        shopCartTable.setProductPrice(productPrice);
                        shopCartTable.setProductType(productType);
                        shopCartTable.setProductImage(productImage);

                        myDatabase.favouriteEventDatabaseDao().insert(shopCartTable);
                        snackBar("Product Added In Cart",R.color.white);
                    }else{
                        snackBar("Product Not Available",R.color.white);
                    }
                }else{
                    snackBar("Internet Not Connected !!",R.color.dark_red);
                }

            }
        });
    }

    public void getProductDetails(int productId){
        if(!connectToInternet){
            if(loadNightModeState()){
                setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
                getWindow().setStatusBarColor(getResources().getColor(R.color.backgroundColor));
            }else{
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
            noInternetLayout.setVisibility(View.VISIBLE);
        }else{
            noInternetLayout.setVisibility(View.GONE);
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
                    productPrice = productResult.getPrice();
                    productType = productResult.getProductType();
                    productImage = productResult.getImage();
                    categoryTv.setText("Category : "+productResult.getProductType());
                    availableOrNot = productResult.getDigital();

                    if(!productResult.getDigital()){
                        stockTv.setBackgroundResource(R.drawable.in_stock_bg);
                        stockTv.setText("In Stock");

                    }else {
                        stockTv.setText("Out of Stock");
                        stockTv.setBackgroundResource(R.drawable.out_stock_bg);
                    }

                    detailsTv.setText(productResult.getDescription());
                    priceTv.setText("৳ "+productResult.getPrice());

                    if(myDatabase.favouriteEventDatabaseDao().getQuantityById(productId) == 0){
                        qtyTv.setText(1+"");
                    }else{
                        qtyTv.setText(myDatabase.favouriteEventDatabaseDao().getQuantityById(productId)+"");
                    }

                    progressBar.setVisibility(View.GONE);

                    if (myDatabase.favouriteEventDatabaseDao().addProductWishListByUserId(currentUserId,productId) == 1){
                        wishList.setImageResource(R.drawable.ic_like);
                    }else {
                        wishList.setImageResource(R.drawable.ic_unlike);
                    }

                    getAllSimilarProductFirstPage(productType);

                }

                @Override
                public void onFailure(Call<ProductResult> call, Throwable t) {

                }
            });
        }
    }

    private List<ProductResult> fetchResultsAllUser(Response<ProductResponse> response) {
        ProductResponse productResponse = response.body();
        assert productResponse != null;
        return productResponse.getProfiles();
    }

    private void getAllSimilarProductFirstPage(String productType) {
        Call<ProductResponse> popular = RetrofitClient
                .getInstance()
                .getApi()
                .getAllSearchProduct(productType,10,0);
        popular.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, retrofit2.Response<ProductResponse> response) {
                if (response.isSuccessful()) {
                    List<ProductResult> productResults = fetchResultsAllUser(response);
                    for (int i = 0 ; i<productResults.size() ; i++) {
                        if(!(productResults.get(i).getId() == productId)){
                            adapterAllSimilarProduct.add(productResults.get(i));
                        }
                    }
                    // check event suggested or not
                    if (adapterAllSimilarProduct.isEmpty()){
                        similarProductLayout.setVisibility(View.GONE);
                    }else {
                        similarProductLayout.setVisibility(View.VISIBLE);
                    }

                }
            }
            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                t.printStackTrace();
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