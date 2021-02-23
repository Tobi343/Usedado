package com.company.usedado.Java.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.company.usedado.Java.Interfaces.AdapterOfferInter;
import com.company.usedado.Java.items.Offer_Offer_Item;
import com.company.usedado.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Offer_Offer_Adapter extends RecyclerView.Adapter<Offer_Offer_Adapter.Offer_Offer_AdapterViewHolder> {

    private ArrayList<Offer_Offer_Item> cards = new ArrayList<Offer_Offer_Item>();

    @NonNull
    @Override
    public Offer_Offer_Adapter.Offer_Offer_AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.offer_card,parent,false);
        Offer_Offer_Adapter.Offer_Offer_AdapterViewHolder mvh = new Offer_Offer_Adapter.Offer_Offer_AdapterViewHolder(v);
        return mvh;
    }
    private List<AdapterOfferInter> channels = new ArrayList<>();

    public void addObserver(AdapterOfferInter channel) {
        this.channels.add(channel);
    }

    public void removeObserver(AdapterOfferInter channel) {
        this.channels.remove(channel);
    }

    @Override
    public void onBindViewHolder(@NonNull Offer_Offer_Adapter.Offer_Offer_AdapterViewHolder holder, int position) {
        Offer_Offer_Item currentItem = cards.get(position);
        try {
            Picasso.get().load(currentItem.getImage()).into(holder.imageView);
            holder.textView.setText(currentItem.getTitle());
            holder.textView1.setText("Original Price:  "+currentItem.getOriginalPrice());
            holder.textView2.setText("Offerd Price:     "+currentItem.getOfferdPrice());
            holder.textView3.setText("via. "+currentItem.getMethod());
            holder.buttonReject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notifyItemRemoved(cards.indexOf(currentItem));
                    currentItem.setState(Offer_Offer_Item.OfferState.rejected);
                    UpdateItem(currentItem);
                    cards.remove(currentItem);

                }
            });
            holder.buttonAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notifyItemRemoved(cards.indexOf(currentItem));
                    currentItem.setState(Offer_Offer_Item.OfferState.accepted);
                    currentItem.setPayAddress("abc");
                    UpdateItem(currentItem);
                    cards.remove(currentItem);

                }
            });
            holder.buttonPending.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int i = cards.indexOf(currentItem);
                   if(i >= 1){
                       notifyItemMoved(i,cards.size()-1);

                       cards.set(i, cards.get(cards.size()-1));
                        cards.set(cards.size()-1, currentItem);
                   }
                    currentItem.setState(Offer_Offer_Item.OfferState.pending);
                    UpdateItem(currentItem);
                }
            });
            //holder.textView3.setText(currentItem.getAdditionalComments());
        }
        catch (Exception e){
        }
    }

    public void UpdateItem(Offer_Offer_Item item){
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Activities").document(item.getActivityID());
        HashMap<String,Object> data = new HashMap<>();
        data.put("State",item.getState());
        if(item.getState().equals(Offer_Offer_Item.OfferState.accepted)){
            data.put("PaymentAddress",item.getPayAddress());
        }
    documentReference.set(data,SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                for (AdapterOfferInter channel : channels) {
                    channel.fetchData();
                }
            }
        });



    }

    public Offer_Offer_Adapter(ArrayList<Offer_Offer_Item> cardList){
        cards = cardList;
    }





    @Override
    public int getItemCount() {
        return cards.size();
    }

    public static class Offer_Offer_AdapterViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageView;
        public TextView textView;
        public TextView textView1;
        public TextView textView2;
        public TextView textView3;
        public ImageView buttonReject;
        public ImageView buttonAccept;
        public ImageView buttonPending;

        public Offer_Offer_AdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.offer_offer_image);
            textView = itemView.findViewById(R.id.offer_offer_title);
            textView1= itemView.findViewById(R.id.offer_offer_oriPrice);
            textView2 = itemView.findViewById(R.id.offer_offer_offerPrice);
            textView3 = itemView.findViewById(R.id.offer_offer_method);
            buttonReject = itemView.findViewById(R.id.offer_card_reject_button);
            buttonAccept = itemView.findViewById(R.id.offer_card_accept_button);
            buttonPending = itemView.findViewById(R.id.offer_card_pending_button);

        }

    }

}

