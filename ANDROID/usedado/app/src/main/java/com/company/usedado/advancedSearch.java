package com.company.usedado;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.company.usedado.Java.activitys.Add_offer;
import com.company.usedado.Java.activitys.Offer_detail;
import com.company.usedado.Java.adapter.YourOfferAdapter;
import com.company.usedado.Java.items.DashboardBigCardItem;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import xyz.danoz.recyclerviewfastscroller.sectionindicator.AbsSectionIndicator;

import java.util.ArrayList;

import xyz.danoz.recyclerviewfastscroller.sectionindicator.SectionIndicator;
import xyz.danoz.recyclerviewfastscroller.vertical.VerticalRecyclerViewFastScroller;

public class advancedSearch extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_search2);
        ArrayList<DashboardBigCardItem> items = new ArrayList<>();

        // Grab your RecyclerView and the RecyclerViewFastScroller from the layout
        RecyclerView recyclerView = findViewById(R.id.recyclerViewSearch);
        VerticalRecyclerViewFastScroller fastScroller = findViewById(R.id.fast_scroller);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        YourOfferAdapter adapter = new YourOfferAdapter(items);
        recyclerView.setAdapter(adapter);

        // Connect the recycler to the scroller (to let the scroller scroll the list)
        fastScroller.setRecyclerView(recyclerView);

        // Connect the scroller to the recycler (to let the recycler scroll the scroller's handle)
        recyclerView.setOnScrollListener(fastScroller.getOnScrollListener());
        fastScroller.setSectionIndicator(new SectionIndicator() {
            @Override
            public void setProgress(float progress) {

            }

            @Override
            public void setSection(Object section) {

            }

            @Override
            public void animateAlpha(float targetAlpha) {

            }
        });


        adapter.setOnItemClickListner(new YourOfferAdapter.OnItemClickListner() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getApplicationContext(), Offer_detail.class);
                Gson gson = new Gson();
                String myJson = gson.toJson(items.get(position));
                intent.putExtra("PRODUCT", myJson);
                startActivity(intent);
            }
        });

        FirebaseFirestore.getInstance().collection("Offers").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    items.add(new DashboardBigCardItem((ArrayList<String>) document.get("Images"),
                            document.get("Name").toString(),
                            document.get("Catagory").toString(),
                            document.get("Price").toString()+"€",
                            document.get("DeliveryPrice").toString(),
                            document.get("User").toString(),
                            document.get("UID").toString() ,
                            document.getId(),
                            document.get("Describtion").toString(),
                            Integer.parseInt(document.get("Aufrufe").toString()),
                            (ArrayList<String>)document.get("AllowedPayments")));

                }
                items.addAll(items);
                items.addAll(items);

                adapter.notifyDataSetChanged();
            }
        });

    }
}