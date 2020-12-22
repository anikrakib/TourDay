package com.anikrakib.tourday.RoomDatabse;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "shop_cart")
public class ShopCartTable implements Serializable {
    @PrimaryKey()
    private int product_id;

    @ColumnInfo(name = "cart_product_name")
    public String productName;

    @ColumnInfo(name = "cart_product_image")
    public String productImage;

    @ColumnInfo(name = "cart_product_quantity")
    public int productQuantity;

    @ColumnInfo(name = "cart_product_price")
    public int productPrice;

    @ColumnInfo(name = "cart_product_type")
    public String productType;

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }
}
