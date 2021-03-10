package com.company.usedado.Java.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.company.usedado.Java.Interfaces.AdapterOfferInter;
import com.company.usedado.Java.adapter.Offer_Offer_Adapter;
import com.company.usedado.Java.adapter.acceptedOfferCardAdapter;
import com.company.usedado.Java.items.Offer_Offer_Item;
import com.company.usedado.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class fragment_activities_group extends Fragment implements AdapterOfferInter {

    View v;
    private RecyclerView recyclerView;
    private ArrayList<Offer_Offer_Item> activites;
    acceptedOfferCardAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;


    public fragment_activities_group(ArrayList<Offer_Offer_Item> activites) {
        this.activites = activites;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public void onResume() {
        super.onResume();
        fetchData();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        for (int i = 0; i < activites.size(); i++) {
            if (activites.get(i).getState().equals(Offer_Offer_Item.OfferState.accepted)) {

            }
            else{
                activites.remove(i);
                i--;
            }
        }
        v = inflater.inflate(R.layout.fragment_view_groups,container,false);
        recyclerView = v.findViewById(R.id.fragment_view_group_recycler_view);
        swipeRefreshLayout = v.findViewById(R.id.group_swiper);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchData();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },2000);
            }
        });
        adapter= new acceptedOfferCardAdapter(activites);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return v;
    }

    @Override
    public void fetchData() {
        CollectionReference reference = FirebaseFirestore.getInstance().collection("Activities");
        reference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                activites.clear();

                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                    Map<String, Object> data = queryDocumentSnapshot.getData();
                    if (data.get("OwnerID").toString().equals(FirebaseAuth.getInstance().getUid())) {
                        Offer_Offer_Item item;
                        activites.add(item = new Offer_Offer_Item(
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
                for (int i = 0; i < activites.size(); i++) {
                    if (activites.get(i).getState().equals(Offer_Offer_Item.OfferState.accepted)) {

                    }
                    else{
                        activites.remove(i);
                        i--;
                    }
                }
                adapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });


    }

}
