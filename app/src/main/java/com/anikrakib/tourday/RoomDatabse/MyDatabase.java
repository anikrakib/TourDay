package com.anikrakib.tourday.RoomDatabse;

import android.content.Context;

import androidx.cardview.widget.CardView;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities={FavouriteEventDatabaseTable.class},version = 1,exportSchema = false)
public abstract class MyDatabase extends RoomDatabase {
    private static MyDatabase myDatabase;
    private static String DATABASE_NAME= "tourady";
    public abstract FavouriteEventDatabaseDao favouriteEventDatabaseDao();

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
