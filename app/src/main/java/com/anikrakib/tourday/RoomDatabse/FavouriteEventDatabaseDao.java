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

//    @Query("SELECT * FROM FavouriteEvent")
//    public List<FavouriteEventDatabaseTable> getData();
//
    @Query("SELECT EXISTS (SELECT 1 FROM favourite_event WHERE user_id=:userId AND event_id=:eventID)")
    public int addByUserId(String userId, int eventID);

    @Query("SELECT EXISTS (SELECT 1 FROM user_info WHERE id=:id)")
    public int addUser(int id);

    @Query("DELETE FROM favourite_event WHERE user_id=:userId AND event_id=:eventID")
    int delete(String userId, String eventID);

    @Insert(onConflict = REPLACE)
    void insert(FavouriteEventDatabaseTable favouriteEventDatabaseTable);

    @Insert(onConflict = REPLACE)
    void insert(TourDayUserDatabaseTable tourDayUserDatabaseTable);

    @Delete
    void deleteAll(List<FavouriteEventDatabaseTable> favouriteEventDatabaseTables);

    @Query("SELECT * FROM favourite_event WHERE user_id=:userId")
    List<FavouriteEventDatabaseTable> getAll(String userId);

}
