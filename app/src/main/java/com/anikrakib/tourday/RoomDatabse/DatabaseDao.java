package com.anikrakib.tourday.RoomDatabse;

import android.icu.text.Replaceable;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import retrofit2.http.DELETE;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface DatabaseDao {

    @Query("SELECT EXISTS (SELECT 1 FROM user_info WHERE id=:id)")
    public int addUser(int id);

    @Insert(onConflict = REPLACE)
    void insert(TourDayUserDatabaseTable tourDayUserDatabaseTable);

    ///////////////////////////////////////////////////////TourDay Event\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    @Query("SELECT EXISTS (SELECT 1 FROM favourite_event WHERE user_id=:userId AND event_id=:eventID)")
    public int addByUserId(String userId, int eventID);

    @Query("DELETE FROM favourite_event WHERE user_id=:userId AND event_id=:eventID")
    int delete(String userId, String eventID);

    @Insert(onConflict = REPLACE)
    void insert(FavouriteEventDatabaseTable favouriteEventDatabaseTable);

    @Delete
    void deleteAll(List<FavouriteEventDatabaseTable> favouriteEventDatabaseTables);

    @Query("SELECT * FROM favourite_event WHERE user_id=:userId")
    List<FavouriteEventDatabaseTable> getAll(String userId);

    ////////////////////////////////////////////////TourDay Shop\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    @Query("SELECT EXISTS (SELECT 1 FROM shop_cart WHERE product_id=:productId)")
    public int addProductCartListByUserId(int productId);

    @Query("SELECT EXISTS (SELECT 1 FROM wish_list_table WHERE user_id=:userId AND product_id=:productId)")
    public int addProductWishListByUserId(String userId, int productId);

    @Query("DELETE FROM wish_list_table WHERE user_id=:userId AND product_id=:productId")
    int deleteFromProductWishList(String userId, int productId);

    @Query("DELETE FROM shop_cart WHERE product_id=:productId")
    int deleteFromProductCartList(int productId);

    @Insert(onConflict = REPLACE)
    void insert(ProductWishListDatabaseTable productWishListDatabaseTable);

    @Insert(onConflict = REPLACE)
    void insert(ShopCartTable shopCartTable);

    @Query("SELECT cart_product_quantity FROM shop_cart WHERE product_id=:productID")
    public int getQuantityById(int productID);

    @Delete
    void deleteAllWishListProduct(List<ProductWishListDatabaseTable> productWishListDatabaseTables);

    @Delete
    void deleteAllCartListProduct(List<ShopCartTable> shopCartTables);

    @Update
    void updateCartItem(ShopCartTable... shopCartTable);

    @Query("SELECT * FROM wish_list_table WHERE user_id=:userId")
    List<ProductWishListDatabaseTable> getAllWishProduct(String userId);

    @Query("SELECT * FROM shop_cart")
    List<ShopCartTable> getAllCartProduct();

    @Query("SELECT COUNT (*) FROM shop_cart")
    int countCart();
}
