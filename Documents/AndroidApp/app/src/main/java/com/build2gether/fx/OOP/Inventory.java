package com.build2gether.fx.OOP;

import java.util.Date;

/**
 * Created by Tulga on 3/8/16.
 */
public class Inventory {

    String description;
    Boolean exchange;
    String image;
    Boolean favor;
    String title;
    String date;
    long timeStamp;


    public Inventory( String title, String description, boolean favor, boolean exchange, String image, String date) {

        this.title = title;
        this.description = description;
        this.favor = favor;
        this.image = image;
        this.exchange = exchange;
        this.date = date;
        this.timeStamp = System.currentTimeMillis() / 1000L;

    }

    public Inventory() {

    }

    public long getTimeStamp() { return timeStamp; }

    public Boolean getExchange() {
        return exchange;
    }

    public Boolean getFavor() {
        return favor;
    }

    public String getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }
}
