package com.company.usedado.Java.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.company.usedado.Java.items.Offer_Offer_Item;
import com.company.usedado.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.SnapshotMetadata;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class acceptedOfferCardAdapter extends RecyclerView.Adapter<acceptedOfferCardAdapter.acceptedOfferCardAdapterViewHolder> {

    private ArrayList<Offer_Offer_Item> cards = new ArrayList<Offer_Offer_Item>();

    @NonNull
    @Override
    public acceptedOfferCardAdapter.acceptedOfferCardAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_offer_accepted,parent,false);
        acceptedOfferCardAdapter.acceptedOfferCardAdapterViewHolder mvh = new acceptedOfferCardAdapter.acceptedOfferCardAdapterViewHolder(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull acceptedOfferCardAdapter.acceptedOfferCardAdapterViewHolder holder, int position) {
        Offer_Offer_Item currentItem = cards.get(position);
        try {
            holder.textView.setText("Address: "+currentItem.getAddress()+" \nTitle: "+currentItem.getTitle()+"\nPayment: "+ currentItem.getMethod() +"\nPrice: "+currentItem.getOfferdPrice()+"\nComment: "+currentItem.getAdditionalComments());

        }
        catch (Exception e){
        }
    }

    public acceptedOfferCardAdapter(ArrayList<Offer_Offer_Item> cardList){
        cards = cardList;
    }


    @Override
    public int getItemCount() {
        return cards.size();
    }

    public static class acceptedOfferCardAdapterViewHolder extends RecyclerView.ViewHolder{

        public TextView textView;

        public acceptedOfferCardAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.offer_accepted_text);


        }

    }

}

