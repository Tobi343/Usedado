package com.company.usedado.Java.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.company.usedado.Java.items.DashboardBigCardItem;
import com.company.usedado.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class YourOfferAdapter extends RecyclerView.Adapter<YourOfferAdapter.YourOfferAdapterViewHolder> {

    private ArrayList<DashboardBigCardItem> offers = new ArrayList<DashboardBigCardItem>();
    private OnItemClickListner mListner;


    public YourOfferAdapter(ArrayList<DashboardBigCardItem> offerList){
        offers = offerList;
    }
    @NonNull
    @Override
    public YourOfferAdapter.YourOfferAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.your_offer_card,parent,false);
        YourOfferAdapter.YourOfferAdapterViewHolder mvh = new YourOfferAdapter.YourOfferAdapterViewHolder(v,mListner);
        return mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull YourOfferAdapter.YourOfferAdapterViewHolder holder, int position) {
        DashboardBigCardItem currentItem = offers.get(position);
        try {
            Picasso.get().load(currentItem.getImageResource()).into(holder.imageView);
            holder.textView.setText(currentItem.getPrice());
            holder.textView1.setText(currentItem.getName());
            holder.textView2.setText("Aufrufe: "+String.valueOf(currentItem.getAufrufe()));
        }
        catch (Exception e){
            Log.d(".",e.toString());
        }
    }

    @Override
    public int getItemCount() {
        return offers.size();
    }

    public interface OnItemClickListner{
        void onItemClick(int position);
    }


    public void setOnItemClickListner(YourOfferAdapter.OnItemClickListner listner){
        mListner = listner;
    }

    public static class YourOfferAdapterViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageView;
        public TextView textView;
        public TextView textView1;
        public TextView textView2;

        public YourOfferAdapterViewHolder(@NonNull View itemView, final YourOfferAdapter.OnItemClickListner listner) {
            super(itemView);
            imageView = itemView.findViewById(R.id.your_offer_card_pic);
            textView = itemView.findViewById(R.id.your_offer_card_price);
            textView1 = itemView.findViewById(R.id.your_offer_card_title);
            textView2 = itemView.findViewById(R.id.your_offer_card_aufrufe_text);

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
