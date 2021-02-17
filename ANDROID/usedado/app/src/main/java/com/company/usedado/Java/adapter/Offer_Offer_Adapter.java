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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Offer_Offer_Adapter extends RecyclerView.Adapter<Offer_Offer_Adapter.Offer_Offer_AdapterViewHolder> {

    private ArrayList<Offer_Offer_Item> cards = new ArrayList<Offer_Offer_Item>();

    @NonNull
    @Override
    public Offer_Offer_Adapter.Offer_Offer_AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.offer_card,parent,false);
        Offer_Offer_Adapter.Offer_Offer_AdapterViewHolder mvh = new Offer_Offer_Adapter.Offer_Offer_AdapterViewHolder(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull Offer_Offer_Adapter.Offer_Offer_AdapterViewHolder holder, int position) {
        Offer_Offer_Item currentItem = cards.get(position);
        try {
            Picasso.get().load(currentItem.getImage()).into(holder.imageView);
            holder.textView.setText(currentItem.getTitle());
            holder.textView1.setText("Original Price:  "+currentItem.getOriginalPrice());
            holder.textView2.setText("Offerd Price:     "+currentItem.getOfferdPrice());
            holder.buttonReject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notifyItemRemoved(cards.indexOf(currentItem));
                    currentItem.setState(Offer_Offer_Item.OfferState.rejected);
                    cards.remove(currentItem);
                }
            });
            holder.buttonAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notifyItemRemoved(cards.indexOf(currentItem));
                    currentItem.setState(Offer_Offer_Item.OfferState.accepted);
                    cards.remove(currentItem);
                }
            });
            holder.buttonPending.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int i = cards.indexOf(currentItem);
                    notifyItemMoved(i,cards.size()-1);
                    currentItem.setState(Offer_Offer_Item.OfferState.pending);
                    cards.set(i, cards.get(cards.size()-1));
                    cards.set(cards.size()-1, currentItem);
                }
            });
            //holder.textView3.setText(currentItem.getAdditionalComments());
        }
        catch (Exception e){
        }
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
        public ImageView buttonReject;
        public ImageView buttonAccept;
        public ImageView buttonPending;

        public Offer_Offer_AdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.offer_offer_image);
            textView = itemView.findViewById(R.id.offer_offer_title);
            textView1= itemView.findViewById(R.id.offer_offer_oriPrice);
            textView2 = itemView.findViewById(R.id.offer_offer_offerPrice);
            buttonReject = itemView.findViewById(R.id.offer_card_reject_button);
            buttonAccept = itemView.findViewById(R.id.offer_card_accept_button);
            buttonPending = itemView.findViewById(R.id.offer_card_pending_button);
        }

    }

}

