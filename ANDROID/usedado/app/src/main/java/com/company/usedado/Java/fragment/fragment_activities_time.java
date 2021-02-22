package com.company.usedado.Java.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.company.usedado.Java.adapter.Offer_Offer_Adapter;
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
import java.util.List;
import java.util.Map;

public class fragment_activities_time extends Fragment {

    View v;
    private RecyclerView recyclerView;
    private ArrayList<Offer_Offer_Item> activites;
    Offer_Offer_Adapter adapter;

    public fragment_activities_time() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activites = new ArrayList<Offer_Offer_Item>();
        /*
         private Uri image;
        private String title;
        private int originalPrice;
        private int offerdPrice;
        private String additionalComments;
        private OfferState state;
         */
        CollectionReference reference = FirebaseFirestore.getInstance().collection("Activities");
        reference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                    Map<String,Object> data = queryDocumentSnapshot.getData();
                   if( data.get("OwnerID").toString().equals(FirebaseAuth.getInstance().getUid())){
                       activites.add(new Offer_Offer_Item(
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
                   }

                }
                adapter.notifyDataSetChanged();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_view_time,container,false);
        recyclerView = v.findViewById(R.id.fragment_view_time_recycler_view);
         adapter= new Offer_Offer_Adapter(activites);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return v;
    }
}
