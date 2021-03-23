package com.company.usedado.Java.items;

import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.company.usedado.createAutctionActivity;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.type.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AuctionItem {

    private String name;
    private String auctionID;
    private String startTime;
    private String endTime;
    private int startPrice;
    private int recentPrice;
    private String Describtion;
    private String Image;
    private FirebaseUser firebaseUser;
    private FirebaseUser recentBidUser;

    public AuctionItem(String name, String auctionID, String startTime, String endTime, int startPrice, String describtion, String image, FirebaseUser user) {
        this.name = name;
        this.auctionID = auctionID;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startPrice = startPrice;
        this.recentPrice = startPrice;
        Describtion = describtion;
        Image = image;
        firebaseUser = user;
    }

    public AuctionItem(String name, String auctionID, String startTime, String endTime, int startPrice, int recentPrice, String describtion, String image, FirebaseUser recentBidUser) {
        this.name = name;
        this.auctionID = auctionID;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startPrice = startPrice;
        this.recentPrice = recentPrice;
        Describtion = describtion;
        Image = image;
        this.recentBidUser = recentBidUser;
    }

    public void setRecentPrice(int recentPrice, FirebaseUser user) {
        this.recentPrice = recentPrice;
        this.recentBidUser = user;
    }

    public String getAuctionID() {
        return auctionID;
    }

    public boolean checkValidation(int price){
        Date date = new Date();

        Date startDates = new Date();
        Date endDates = new Date();


        try {
            startDates =  new SimpleDateFormat("yyyy/MM/dd/hh:mm").parse(startTime);
            endDates =  new SimpleDateFormat("yyyy/MM/dd/hh:mm").parse(endTime);
        } catch (ParseException e) {
        }


        if(date.after(startDates) && date.before(endDates) && price > recentPrice){
            return true;
        }
        return false;
    }


    public String getStartTime() {
        return startTime.toString();
    }



    public int getStartPrice() {
        return startPrice;
    }

    public int getRecentPrice() {
        return recentPrice;
    }

    public String getDescribtion() {
        return Describtion;
    }

    public String getImage() {
        return Image;
    }

    public String getRecentBidUser() {
        return recentBidUser.getUid();
    }

    public String getName() {
        return name;
    }

    public String getEndTime() {
        return endTime;
    }



    @Exclude
    public Map<String,Object> toMap(){

        Map<String,Object> recentUser = new HashMap<>();
        recentUser.put("UID",firebaseUser.getUid());
        recentUser.put("NAME",firebaseUser.getDisplayName());
        recentUser.put("PIC",firebaseUser.getPhotoUrl().toString());

        HashMap<String, Object> result = new HashMap<>();
        result.put("auctionID", auctionID);
        result.put("startTime", startTime);
        result.put("endTime", endTime);
        result.put("startPrice", startPrice);
        result.put("recentPrice", recentPrice);
        result.put("Image", Image.toString());
        result.put("Describtion", Describtion);
        result.put("Name", name);
        result.put("firebaseUser", recentUser);
        //result.put("recentBidUser", recentBidUser.getUid());

        return result;
    }
}
