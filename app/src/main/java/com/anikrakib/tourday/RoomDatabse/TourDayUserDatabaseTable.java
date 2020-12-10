package com.anikrakib.tourday.RoomDatabse;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "user_info")
public class TourDayUserDatabaseTable implements Serializable {
    @PrimaryKey
    private int id;

    @ColumnInfo(name = "username")
    public String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
