package com.company.usedado.Java.items;

import android.net.Uri;

import java.util.function.Function;


public class Offer_Offer_Item {
    public enum OfferState{
        accepted,
        rejected,
        pending,
        asked
    }

    private Uri image;
    private String title;
    private int originalPrice;
    private int offerdPrice;
    private String additionalComments;
    private OfferState state;
    private String UserID;
    private String OfferID;
    private String ID;
    private String activityID;
    private String Method;
    private String address;
    private String PayAddress;

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPayAddress(String payAddress) {
        PayAddress = payAddress;
    }


    public String getAddress() {
        return address;
    }

    public String getPayAddress() {
        return PayAddress;
    }

    public Offer_Offer_Item(Uri image, String title, int originalPrice, int offerdPrice, String additionalComments, OfferState state, String userID, String offerID, String ID, String method) {
        this.image = image;
        this.title = title;
        this.originalPrice = originalPrice;
        this.offerdPrice = offerdPrice;
        this.additionalComments = additionalComments;
        this.state = state;
        UserID = userID;
        OfferID = offerID;
        this.ID = ID;
        this.Method = method;
    }

    public Uri getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public int getOriginalPrice() {
        return originalPrice;
    }

    public int getOfferdPrice() {
        return offerdPrice;
    }

    public String getAdditionalComments() {
        return additionalComments;
    }

    public OfferState getState() {
        return state;
    }

    public String getUserID() {
        return UserID;
    }

    public String getOfferID() {
        return OfferID;
    }

    public String getID() {
        return ID;
    }

    public void setState(OfferState state) {
        this.state = state;
    }

    public String getMethod() {
        return Method;
    }

    public String getActivityID() {
        return activityID;
    }

    public void setActivityID(String activityID) {
        this.activityID = activityID;
    }
}
