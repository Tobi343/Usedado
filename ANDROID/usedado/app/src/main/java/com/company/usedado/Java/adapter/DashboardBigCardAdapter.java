package com.company.usedado.Java.adapter;

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

public class DashboardBigCardAdapter extends RecyclerView.Adapter<DashboardBigCardAdapter.DashboardBigCardAdapterViewHolder> {


    private ArrayList<DashboardBigCardItem> cards = new ArrayList<DashboardBigCardItem>();
    private OnItemClickListner mListner;

    public interface OnItemClickListner{
        void onItemClick(int position);
    }

    @NonNull
    @Override
    public DashboardBigCardAdapter.DashboardBigCardAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.big_card,parent,false);
        DashboardBigCardAdapter.DashboardBigCardAdapterViewHolder mvh = new DashboardBigCardAdapter.DashboardBigCardAdapterViewHolder(v,mListner);
        return mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull DashboardBigCardAdapterViewHolder holder, int position) {
        DashboardBigCardItem currentItem = cards.get(position);
        try {
            Picasso.get().load(currentItem.getImageResource()).resize(600, 600)
                    .centerCrop().into(holder.imageView);
            holder.textView.setText(currentItem.getUser());
            holder.textView1.setText(currentItem.getName());
            holder.textView2.setText(currentItem.getPrice());
        }
        catch (Exception e){
        }
    }

    public DashboardBigCardAdapter(ArrayList<DashboardBigCardItem> cardList){
        cards = cardList;
    }

    public void setOnItemClickListner(OnItemClickListner listner){
        mListner = listner;
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    public static class DashboardBigCardAdapterViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageView;
        public TextView textView;
        public TextView textView1;
        public TextView textView2;

        public DashboardBigCardAdapterViewHolder(@NonNull View itemView, final OnItemClickListner listner) {
            super(itemView);
            imageView = itemView.findViewById(R.id.dashboard_card_big_picture);
            textView = itemView.findViewById(R.id.dashboard_card_big_name);
            textView1 = itemView.findViewById(R.id.dashboard_card_big_topic);
            textView2 = itemView.findViewById(R.id.dashboard_card_big_price);

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
