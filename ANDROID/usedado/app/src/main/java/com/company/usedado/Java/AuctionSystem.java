package com.company.usedado.Java;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.company.usedado.Java.items.AuctionItem;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuctionSystem {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Auction/");

    public List<AuctionItem> activeAuctions = new ArrayList<>();
    public List<AuctionItem> outgoingAuctions;

    public boolean setBid(String auctionID, int price, FirebaseUser user){
        //AuctionItem item = findAuction(auctionID);
        //if(item.checkValidation(price)){
          //  item.setRecentPrice(price,user);
            myRef.child(auctionID).child("recentPrice").setValue(price).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    System.out.println(e.getMessage());
                }
            });
            Map<String,Object> recentUser = new HashMap<>();
            recentUser.put("UID",user.getUid());
            recentUser.put("NAME",user.getDisplayName());
            recentUser.put("PIC",user.getPhotoUrl().toString());
        myRef.child(auctionID).child("recentBidUser").setValue(recentUser).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println(e.getMessage());
            }
        });
            return true;
        //}
       // return false;
    }

    public void auctionRunningOut(String auctionID){
        AuctionItem item = findAuction(auctionID);
        activeAuctions.remove(item);
        outgoingAuctions.add(item);
    }

    public AuctionItem findAuction(String auctionID){
        for (AuctionItem activeAuction : activeAuctions) {
            if(activeAuction.getAuctionID().equals(auctionID)) return activeAuction;
        }
        return null;
    }


}
