package com.anikrakib.tourday.RoomDatabse;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities={FavouriteEventDatabaseTable.class,TourDayUserDatabaseTable.class,ProductWishListDatabaseTable.class},version = 6,exportSchema = false)
public abstract class MyDatabase extends RoomDatabase {
    private static MyDatabase myDatabase;
    private static final String DATABASE_NAME= "tourday";
    public abstract DatabaseDao favouriteEventDatabaseDao();

    public synchronized static MyDatabase getInstance(Context context){
        if(myDatabase == null){
            myDatabase = Room.databaseBuilder(context.getApplicationContext()
                    ,MyDatabase.class,DATABASE_NAME).allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return myDatabase;
    }
}
