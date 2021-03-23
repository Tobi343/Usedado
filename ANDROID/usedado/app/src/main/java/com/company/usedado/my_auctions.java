package com.company.usedado;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.health.UidHealthStats;
import android.view.View;
import android.widget.Toast;

import com.company.usedado.Java.activitys.Add_offer;
import com.company.usedado.Java.activitys.my_offers;
import com.company.usedado.Java.adapter.AuctionCardAdapter;
import com.company.usedado.Java.adapter.YourOfferAdapter;
import com.company.usedado.Java.items.AuctionItem;
import com.company.usedado.Java.items.DashboardBigCardItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Map;

public class my_auctions extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FirebaseUser user;
    private ArrayList<AuctionItem> items1;
    private AuctionCardAdapter adapter;
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_auctions);
        recyclerView = findViewById(R.id.your_auction_recycler_view);
        user = FirebaseAuth.getInstance().getCurrentUser();
        GetData();
    }

    public void GetData()  {
        items1 = new ArrayList<AuctionItem>();
        if (user != null) {
            ref.child("Auction").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()) {
                        DataSnapshot snapshot = task.getResult();
                        Map<String, Map<String, Object>> items = (Map<String, Map<String, Object>>) snapshot.getValue();
                        items1.clear();



                        for (String s : items.keySet()) {
                            Map<String, Object> value = items.get(s);

                        try {
                            Map<String,Object> owner = (Map<String, Object>) value.get("firebaseUser");
                            if(owner.get("UID").toString().equals(user.getUid())){
                                items1.add(new AuctionItem(value.get("Name").toString(), s,
                                        value.get("startTime").toString(),
                                        value.get("endTime").toString()
                                        , Integer.parseInt(value.get("startPrice").toString()), Integer.parseInt(value.get("recentPrice").toString()), value.get("Describtion").toString(), value.get("Image").toString(), null));

                            }
//    public AuctionItem(String name, String auctionID, String startTime, String endTime, int startPrice, int recentPrice, String describtion, String image, FirebaseUser recentBidUser) {
                        }catch (Exception e){
                            continue;
                        }


                        }

                        adapter = new AuctionCardAdapter(items1);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                    } else {

                    }
                }
            });
        }
    }

    DashboardBigCardItem deletedAdd = null;

}