package com.company.usedado.Java.items;

import android.net.Uri;


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

    public Offer_Offer_Item(Uri image, String title, int originalPrice, int offerdPrice, String additionalComments, OfferState state, String userID, String offerID, String ID) {
        this.image = image;
        this.title = title;
        this.originalPrice = originalPrice;
        this.offerdPrice = offerdPrice;
        this.additionalComments = additionalComments;
        this.state = state;
        UserID = userID;
        OfferID = offerID;
        this.ID = ID;
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
}
