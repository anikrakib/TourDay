package com.anikrakib.tourday.RoomDatabse;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "favourite_event")
public class FavouriteEventDatabaseTable implements Serializable {
    @PrimaryKey
    private int id;

    @ColumnInfo(name = "event_name")
    public String name;

    @ColumnInfo(name = "event_image")
    public String image;

    @ColumnInfo(name = "event_price")
    public String price;

    @ColumnInfo(name = "event_details")
    public String details;

    @ColumnInfo(name = "event_location")
    public String location;

    @ColumnInfo(name = "event_date")
    public String date;

    @ColumnInfo(name = "event_host")
    public String host;

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

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
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
}
