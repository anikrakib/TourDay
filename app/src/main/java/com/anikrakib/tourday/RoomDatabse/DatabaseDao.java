package com.anikrakib.tourday.RoomDatabse;

import android.icu.text.Replaceable;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import retrofit2.http.DELETE;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface DatabaseDao {

//    @Query("SELECT * FROM FavouriteEvent")
//    public List<FavouriteEventDatabaseTable> getData();
//
    @Query("SELECT EXISTS (SELECT 1 FROM favourite_event WHERE user_id=:userId AND event_id=:eventID)")
    public int addByUserId(String userId, int eventID);

    @Query("SELECT EXISTS (SELECT 1 FROM wish_list_table WHERE user_id=:userId AND product_id=:productId)")
    public int addProductWishListByUserId(String userId, int productId);

    @Query("SELECT EXISTS (SELECT 1 FROM user_info WHERE id=:id)")
    public int addUser(int id);

    @Query("DELETE FROM favourite_event WHERE user_id=:userId AND event_id=:eventID")
    int delete(String userId, String eventID);

    @Query("DELETE FROM wish_list_table WHERE user_id=:userId AND product_id=:productId")
    int deleteFromProductWishList(String userId, int productId);

    @Insert(onConflict = REPLACE)
    void insert(FavouriteEventDatabaseTable favouriteEventDatabaseTable);

    @Insert(onConflict = REPLACE)
    void insert(TourDayUserDatabaseTable tourDayUserDatabaseTable);

    @Insert(onConflict = REPLACE)
    void insert(ProductWishListDatabaseTable productWishListDatabaseTable);

    @Delete
    void deleteAll(List<FavouriteEventDatabaseTable> favouriteEventDatabaseTables);

    @Delete
    void deleteAllWishListProduct(List<ProductWishListDatabaseTable> productWishListDatabaseTables);

    @Query("SELECT * FROM favourite_event WHERE user_id=:userId")
    List<FavouriteEventDatabaseTable> getAll(String userId);

    @Query("SELECT * FROM wish_list_table WHERE user_id=:userId")
    List<ProductWishListDatabaseTable> getAllWishProduct(String userId);
}
