package com.company.usedado.Java.items;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DashboardBigCardItem implements Serializable {

    private String imageResource;
    private ArrayList<String> images;
    private String name;
    private String topic; //Categorie
    private String price;
    private String delivery;
    private String user;
    private String userID;
    private String offerID;
    private String describtion;
    private int aufrufe;
    private ArrayList<String> allowedPayments;


    public DashboardBigCardItem( ArrayList<String> images, String name, String topic, String price, String delivery, String user, String userID, String offerID, String describtion, int aufrufe, ArrayList<String> allowedPayments) {
        this.imageResource = images.get(0);
        this.images = images;
        this.name = name;
        this.topic = topic;
        this.price = price;
        this.delivery = delivery;
        this.user = user;
        this.userID = userID;
        this.offerID = offerID;
        this.describtion = describtion;
        this.aufrufe = aufrufe;
        this.allowedPayments = allowedPayments;
    }


    public String getImageResource() {
        return imageResource;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public String getofferID() {
        return offerID;
    }

    public String getName() {
        return name;
    }

    public String getTopic() {
        return topic;
    }

    public String getPrice() {
        return price;
    }
    public String getDeliveryPrice() {
        return delivery;
    }


    public String getUser() {
        return user;
    }

    public String getUID() {
        return userID;
    }

    public int getAufrufe() {
        return aufrufe;
    }

    public String getDescribtion() {
        return describtion;
    }

    public ArrayList<String> getAllowedPayments() {
        return allowedPayments;
    }

}
