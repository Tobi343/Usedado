package com.company.usedado.Java.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.company.usedado.Java.Interfaces.AdapterOfferInter;
import com.company.usedado.Java.adapter.ViewPagerAdapter;
import com.company.usedado.Java.dialogs.dialog_finish;
import com.company.usedado.Java.dialogs.dialog_profile;
import com.company.usedado.Java.fragment.fragment_activities_answer;
import com.company.usedado.Java.fragment.fragment_activities_group;
import com.company.usedado.Java.fragment.fragment_activities_time;
import com.company.usedado.Java.items.DashboardBigCardItem;
import com.company.usedado.Java.items.Offer_Offer_Item;
import com.company.usedado.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class activity_offerActivitys extends AppCompatActivity implements dialog_finish.dialog_finish_Listener {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private BottomNavigationView bottomNavigationView;
    private ArrayList<Offer_Offer_Item> activites;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_activitys);

        tabLayout = (TabLayout) findViewById(R.id.offer_activitys_tabs);
        viewPager = (ViewPager) findViewById(R.id.offer_activitys_view);
        bottomNavigationView = findViewById(R.id.offer_activitys_bottom_navigation);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        activites = new ArrayList<Offer_Offer_Item>();

        CollectionReference reference = FirebaseFirestore.getInstance().collection("Activities");
        reference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                    Map<String,Object> data = queryDocumentSnapshot.getData();
                    if( data.get("OwnerID").toString().equals(FirebaseAuth.getInstance().getUid())){
                        Offer_Offer_Item item;
                        activites.add(item=new Offer_Offer_Item(
                                Uri.parse(data.get("Image").toString()),
                                data.get("Title").toString(),
                                Integer.parseInt(data.get("OriginalPrice").toString()),
                                Integer.parseInt(data.get("OfferdPrice").toString()),
                                data.get("AdditionalComments").toString(),
                                Offer_Offer_Item.OfferState.valueOf(data.get("State").toString()),
                                data.get("UserID").toString(),
                                data.get("OfferID").toString(),
                                data.get("OwnerID").toString(),
                                data.get("Method").toString()));

                        item.setAddress(data.get("DeliveryAddress").toString());
                        item.setActivityID(queryDocumentSnapshot.getId());

                    }

                }
                ArrayList<Offer_Offer_Item> list = new ArrayList<>();
                list.addAll(activites);
                ArrayList<Offer_Offer_Item> list1 = new ArrayList<>();
                list1.addAll(activites);
                viewPagerAdapter.AddFragment(new fragment_activities_time(list),"New Activities");
                viewPagerAdapter.AddFragment(new fragment_activities_group(list1),"Accepted");
                viewPagerAdapter.AddFragment(new fragment_activities_answer(),"Responses");
                viewPager.setAdapter(viewPagerAdapter);
                tabLayout.setupWithViewPager(viewPager);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_home:
                         startActivity(new Intent(getApplicationContext(), Dashboard.class));
                         overridePendingTransition(0,0);
                    case R.id.navigation_message:
                        return true;
                    case R.id.navigation_add:
                        startActivity(new Intent(getApplicationContext(), Add_offer.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.navigation_Auction:
                        //startActivity(new Intent(getApplicationContext(),Messages.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.navigation_catagories:
                        //startActivity(new Intent(getApplicationContext(),Messages.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }



    @Override
    public void applyTexts(String newText, String title, Offer_Offer_Item item) {
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Activities").document(item.getActivityID());
        item.setPayAddress(newText);
        HashMap<String,Object> data = new HashMap<>();
        data.put("State",item.getState());
        if(item.getState().equals(Offer_Offer_Item.OfferState.accepted)){
            data.put("PaymentAddress",title);
        }
        documentReference.set(data, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {


            }
        });
    }
}