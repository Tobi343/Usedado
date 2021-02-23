package com.company.usedado.Java.activitys;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.company.usedado.Java.adapter.CategoryAdapterCardView;
import com.company.usedado.Java.adapter.DashboardBigCardAdapter;
import com.company.usedado.Java.items.DashboardBigCardItem;
import com.company.usedado.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

public class CategorieView extends AppCompatActivity {

    private AutoCompleteTextView searchBar;
    private RecyclerView recyclerView;
    private TextView headline;

    ArrayList<DashboardBigCardItem> items = new ArrayList<DashboardBigCardItem>();
    ArrayList<DashboardBigCardItem> searchItems = new ArrayList<DashboardBigCardItem>();
    CategoryAdapterCardView adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorie_view);
        recyclerView = findViewById(R.id.category_recycler);
        searchBar = findViewById(R.id.category_searchbar);
        headline = findViewById(R.id.category_headline);

        headline.setText(getIntent().getStringExtra("Title"));
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchItems = new ArrayList<DashboardBigCardItem>();
                for (DashboardBigCardItem item : items) {
                    if(item.getName().toLowerCase().contains(s.toString().toLowerCase())){
                        searchItems.add(item);
                    }
                }
                recyclerView.setAdapter(adapter = new CategoryAdapterCardView(searchItems));
                adapter.setOnItemClickListner(new CategoryAdapterCardView.OnItemClickListner() {
                    @Override
                    public void onItemClick(int position) {
                        onItemClicks(searchItems.get(position));
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        CollectionReference colRef = FirebaseFirestore.getInstance().collection("Offers");
        colRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                items.clear();
                searchItems.clear();

                for (QueryDocumentSnapshot queryDocumentSnapshot :task.getResult()) {
                    Map<String, Object> data = queryDocumentSnapshot.getData();
                    items.add(new DashboardBigCardItem( (ArrayList<String>) data.get("Images"),data.get("Name").toString(), data.get("Catagory").toString(),data.get("Price").toString(), data.get("DeliveryPrice").toString(),
                            data.get("User").toString(),data.get("UID").toString(),queryDocumentSnapshot.getId(),data.get("Describtion").toString(), Integer.parseInt(data.get("Aufrufe").toString()),(ArrayList<String>)data.get("AllowedPayments")));
                }
                String[] names = new String[items.size()];
                for (int i = 0; i < items.size(); i++) {
                    names[i] = items.get(i).getName();
                }
                items = (ArrayList<DashboardBigCardItem>) items.stream().filter(x->x.getTopic().equals(headline.getText().toString())).collect(Collectors.toList());
                searchItems.addAll(items);
                ArrayAdapter<String> search = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,names);
                searchBar.setAdapter(search);

                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recyclerView.setAdapter(adapter = new CategoryAdapterCardView(searchItems));
                adapter.setOnItemClickListner(new CategoryAdapterCardView.OnItemClickListner() {
                    @Override
                    public void onItemClick(int position) {
                        onItemClicks(searchItems.get(position));
                    }
                });
            }
        });

    }
    public void onItemClicks(DashboardBigCardItem item) {
        final DocumentReference docDb = FirebaseFirestore.getInstance().collection("Offers").document(item.getofferID());
        docDb.update("Aufrufe", FieldValue.increment(1));
        Intent intent = new Intent(getApplicationContext(), Offer_detail.class);
        Gson gson = new Gson();
        String myJson = gson.toJson(item);
        intent.putExtra("PRODUCT", myJson);
        startActivity(intent);
    }
}