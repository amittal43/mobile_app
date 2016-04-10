package com.build2gether.fx.OOP;

/**
 * Created by Tina on 4/7/2016.
 */
public class Notification {

    String offer;
    String theirId;
    String itemId;
    String status;

    public Notification() {}

    public Notification(String offer, String theirId, String itemId, String status) {
        this.offer = offer;
        this.theirId = theirId;
        this.itemId = itemId;
        this.status = status;
    }

    public String getOffer() {
        return offer;
    }


    public String getTheirId() {
        return theirId;
    }

    public String getItemId() {
        return itemId;
    }

    public String getStatus() {
        return status;
    }
}
