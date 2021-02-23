package com.company.usedado.Java.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.company.usedado.Java.activitys.my_offers;
import com.company.usedado.Java.items.Offer_Offer_Item;
import com.company.usedado.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class answerOfferCardAdapter extends RecyclerView.Adapter<answerOfferCardAdapter.answerOfferCardAdapterViewHolder> {

    private ArrayList<Offer_Offer_Item> cards = new ArrayList<Offer_Offer_Item>();

    @NonNull
    @Override
    public answerOfferCardAdapter.answerOfferCardAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_answer_offer,parent,false);
        answerOfferCardAdapter.answerOfferCardAdapterViewHolder mvh = new answerOfferCardAdapter.answerOfferCardAdapterViewHolder(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull answerOfferCardAdapter.answerOfferCardAdapterViewHolder holder, int position) {
        Offer_Offer_Item currentItem = cards.get(position);
        try {
            holder.textView.setText(currentItem.getTitle());
            holder.textView1.setText(currentItem.getPayAddress());
            holder.textView2.setText("Method: "+ currentItem.getMethod() +"\nPrice: "+currentItem.getOfferdPrice());
            /*holder.textView3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Offer_Offer_Item item = currentItem;

                    Snackbar.make(holder.itemView,"Add deleted: "+currentItem.getTitle(), BaseTransientBottomBar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    }).addCallback(new Snackbar.Callback(){
                        @Override
                        public void onDismissed(Snackbar snackbar, int event) {
                            //see Snackbar.Callback docs for event details
                            if(item[0] == null) {
                                Toast.makeText(my_offers.this, "UNDO", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(my_offers.this, deletedAdd.getofferID(), Toast.LENGTH_SHORT).show();
                                db.collection("Offers").document(deletedAdd.getofferID()).delete().addOnFailureListener(new OnFailureListener() {
                                                                                                                            @Override
                                                                                                                            public void onFailure(@NonNull Exception e) {
                                                                                                                                String message = e.getMessage();
                                                                                                                                Toast.makeText(my_offers.this, message, Toast.LENGTH_SHORT).show();

                                                                                                                            }
                                                                                                                        }
                                );
                            }

                        }
                    }).show();
                }
            });*/

        }
        catch (Exception e){
        }
    }

    public answerOfferCardAdapter(ArrayList<Offer_Offer_Item> cardList){
        cards = cardList;
    }


    @Override
    public int getItemCount() {
        return cards.size();
    }

    public static class answerOfferCardAdapterViewHolder extends RecyclerView.ViewHolder{

        public TextView textView;
        public TextView textView1;
        public TextView textView2;
        public TextView textView3;

        public answerOfferCardAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.card_answer_heading);
            textView1 = itemView.findViewById(R.id.card_answer_address);
            textView2 = itemView.findViewById(R.id.card_answer_details);
            textView3 = itemView.findViewById(R.id.delete_answer);
        }

    }

}

