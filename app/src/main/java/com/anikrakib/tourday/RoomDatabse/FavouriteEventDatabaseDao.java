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
public interface FavouriteEventDatabaseDao {

//    @Insert
//    public void addToCart(FavouriteEventDatabaseTable favouriteEventDatabaseTable);
//
//    @Query("SELECT * FROM FavouriteEvent")
//    public List<FavouriteEventDatabaseTable> getData();
//
    @Query("SELECT EXISTS (SELECT 1 FROM favourite_event WHERE id=:id)")
    public int isAddToCart(int id);
//
//    @Query("select COUNT (*) from FavouriteEvent")
//    int countCart();
//
//    @Query("DELETE FROM FavouriteEvent WHERE id=:id ")
//    int deleteItem(int id);

    @Insert(onConflict = REPLACE)
    void insert(FavouriteEventDatabaseTable favouriteEventDatabaseTable);

    @Delete
    void delete(FavouriteEventDatabaseTable favouriteEventDatabaseTable);

    @Delete
    void deleteAll(List<FavouriteEventDatabaseTable> favouriteEventDatabaseTables);

    @Query("SELECT * FROM favourite_event")
    List<FavouriteEventDatabaseTable> getAll();

}
