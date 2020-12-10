package com.anikrakib.tourday.RoomDatabse;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "favourite_event", indices = @Index(value = {"event_id"}, unique = true))
public class FavouriteEventDatabaseTable implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "event_id")
    public String eventId;

    @ColumnInfo(name = "event_name")
    public String name;

    @ColumnInfo(name = "event_image")
    public String image;

    @ColumnInfo(name = "event_price")
    public String price;

    @ColumnInfo(name = "user_id")
    public String user_id;

    @ColumnInfo(name = "event_location")
    public String location;

    @ColumnInfo(name = "event_date")
    public String date;

    @ColumnInfo(name = "event_host")
    public String host;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String capacity) {
        this.date = capacity;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
