package com.company.usedado.Java.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.company.usedado.Java.adapter.Offer_Offer_Adapter;
import com.company.usedado.Java.items.Offer_Offer_Item;
import com.company.usedado.R;

import java.util.ArrayList;
import java.util.List;

public class fragment_activities_time extends Fragment {

    View v;
    private RecyclerView recyclerView;
    private ArrayList<Offer_Offer_Item> activites;

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
        activites.add(new Offer_Offer_Item(Uri.parse("https://cdn57.androidauthority.net/wp-content/uploads/2020/04/oneplus-8-pro-ultra-wide-sample-twitter-1.jpg"),
                                            "Hallo", 30,25,"Hey alles klar ?",null,"","",""));
        activites.add(new Offer_Offer_Item(Uri.parse("https://cdn57.androidauthority.net/wp-content/uploads/2020/04/oneplus-8-pro-ultra-wide-sample-twitter-1.jpg"),
                "Hallo", 30,25,"Hey alles klar ?",null,"","",""));
        activites.add(new Offer_Offer_Item(Uri.parse("https://cdn57.androidauthority.net/wp-content/uploads/2020/04/oneplus-8-pro-ultra-wide-sample-twitter-1.jpg"),
                "Hallo", 30,25,"Hey alles klar ?",null,"","",""));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_view_time,container,false);
        recyclerView = v.findViewById(R.id.fragment_view_time_recycler_view);
        recyclerView.setAdapter(new Offer_Offer_Adapter(activites));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return v;
    }
}
