package com.company.usedado.Java.adapter;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.company.usedado.Java.items.Offer_Offer_Item;
import com.company.usedado.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.SnapshotMetadata;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

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
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(holder.itemView.getContext()).setTitle("Delete Answer")
                            .setMessage("Do you really want to delete this Answer? You cant undo this action!")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    FirebaseFirestore.getInstance().collection("Activities").document(currentItem.getActivityID()).delete().addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            String message = e.getMessage();
                                            Toast.makeText(holder.itemView.getContext(), message, Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            notifyItemRemoved(position);
                                        }
                                    });
                                }
                            })
                            .setNegativeButton(android.R.string.no, null).setIcon(android.R.drawable.ic_dialog_info).show();
                }
            });
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
        public TextView delete;

        public acceptedOfferCardAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.offer_accepted_text);
            delete = itemView.findViewById(R.id.delete_accepted_offer_info);

        }

    }

}

