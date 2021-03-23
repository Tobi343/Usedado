package com.company.usedado.Java.adapter;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.company.usedado.Java.items.AuctionItem;
import com.company.usedado.Java.items.Offer_Offer_Item;
import com.company.usedado.R;
import com.company.usedado.createAutctionActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Period;
import java.util.ArrayList;
import java.util.Date;

public class AuctionCardAdapter extends RecyclerView.Adapter<AuctionCardAdapter.AuctionCardAdapterViewHolder> {

    private ArrayList<AuctionItem> cards = new ArrayList<AuctionItem>();
    private AuctionCardAdapter.OnItemClickListner mListner;

    public interface OnItemClickListner{
        void onItemClick(int position);
    }
    public void setOnItemClickListner(AuctionCardAdapter.OnItemClickListner listner){
        mListner = listner;
    }


    @NonNull
    @Override
    public AuctionCardAdapter.AuctionCardAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_auction,parent,false);
        AuctionCardAdapter.AuctionCardAdapterViewHolder mvh = new AuctionCardAdapter.AuctionCardAdapterViewHolder(v,mListner);
        return mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull AuctionCardAdapter.AuctionCardAdapterViewHolder holder, int position) {
        AuctionItem currentItem = cards.get(position);
        Picasso.get().load(currentItem.getImage()).into(holder.image);
            holder.title.setText(currentItem.getName());
            holder.descr.setText(currentItem.getDescribtion());
            int price = currentItem.getRecentPrice();
            holder.price.setText(String.valueOf(price)+" €");

        Date startDates = new Date();
        Date endDates = new Date();


        try {
            startDates =  new SimpleDateFormat("yyyy/MM/dd/hh:mm").parse(currentItem.getStartTime());
            endDates =  new SimpleDateFormat("yyyy/MM/dd/hh:mm").parse(currentItem.getEndTime());
        } catch (ParseException e) {
            return;
        }

        Date date = endDates;
           Date date1 = new Date();
            long diff =  date.getTime()-date1.getTime();
            int numOfDays = (int) (diff / (1000 * 60 * 60 * 24));
            int hours = (int) (diff / (1000 * 60 * 60));
            int minutes = (int) (diff / (1000 * 60));
            int seconds = (int) (diff / (1000));
            holder.restTime.setText((hours-1)+"h "+minutes%60+"m "+seconds%60+"s");



    }


    public AuctionCardAdapter(ArrayList<AuctionItem> cardList){
        cards = cardList;
    }


    @Override
    public int getItemCount() {
        return cards.size();
    }

    public static class AuctionCardAdapterViewHolder extends RecyclerView.ViewHolder{

        public ImageView image;
        public TextView title;
        public TextView descr;
        public TextView price;
        public TextView restTime;

        public AuctionCardAdapterViewHolder(@NonNull View itemView, final AuctionCardAdapter.OnItemClickListner listner) {
            super(itemView);
            title = itemView.findViewById(R.id.auctionTitle);
            descr = itemView.findViewById(R.id.auctionDescribtion);
            price = itemView.findViewById(R.id.auctionPrice);
            restTime = itemView.findViewById(R.id.auctionDate);
            image = itemView.findViewById(R.id.auctionCardImage);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listner != null){
                        int pos = getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION){
                            listner.onItemClick(pos);
                        }
                    }
                }
            });
        }

    }

}

