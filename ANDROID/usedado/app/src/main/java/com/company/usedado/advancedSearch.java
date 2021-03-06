package com.company.usedado;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.company.usedado.Java.activitys.Add_offer;
import com.company.usedado.Java.activitys.Offer_detail;
import com.company.usedado.Java.adapter.CategoryAdapterCardView;
import com.company.usedado.Java.adapter.YourOfferAdapter;
import com.company.usedado.Java.items.DashboardBigCardItem;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class advancedSearch extends AppCompatActivity {

    List<String> categorys = new ArrayList<>(Arrays.asList("Title","Price"));


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_search2);
        ArrayList<DashboardBigCardItem> items = new ArrayList<>();

        // Grab your RecyclerView and the RecyclerViewFastScroller from the layout
        RecyclerView recyclerView = findViewById(R.id.recyclerViewSearch);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        CategoryAdapterCardView adapter = new CategoryAdapterCardView(items);
        recyclerView.setAdapter(adapter);
        Spinner spinner = findViewById(R.id.sortSpinner);
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,categorys);

        spinner.setAdapter(aa);
        Collections.sort(items, new Comparator<DashboardBigCardItem>() {
            @Override
            public int compare(DashboardBigCardItem o1, DashboardBigCardItem o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    Collections.sort(items, new Comparator<DashboardBigCardItem>() {
                        @Override
                        public int compare(DashboardBigCardItem o1, DashboardBigCardItem o2) {
                            return o1.getName().compareTo(o2.getName());
                        }
                    });
                }
                else{
                    Collections.sort(items, new Comparator<DashboardBigCardItem>() {
                        @Override
                        public int compare(DashboardBigCardItem o1, DashboardBigCardItem o2) {
                            Integer price1 = Integer.parseInt(o1.getPrice().replace("€",""));
                            Integer price2 = Integer.parseInt(o2.getPrice().replace("€",""));
                            if(price1 > price2){
                                return 1;
                            }
                            else if(price1 < price2){
                                return -1;
                            }
                            return 0;
                        }
                    });
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        adapter.setOnItemClickListner(new CategoryAdapterCardView.OnItemClickListner() {
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


                adapter.notifyDataSetChanged();
            }
        });

    }
}