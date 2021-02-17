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


public class DashboardWideCardAdapter extends RecyclerView.Adapter<DashboardWideCardAdapter.DashboardWideCardAdapterViewHolder> {


    private ArrayList<DashboardBigCardItem> cards = new ArrayList<DashboardBigCardItem>();
    private OnItemClickListner mListner;


    public interface OnItemClickListner{
        void onItemClick(int position);
    }

    @NonNull
    @Override
    public DashboardWideCardAdapter.DashboardWideCardAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.wide_card,parent,false);
        DashboardWideCardAdapter.DashboardWideCardAdapterViewHolder mvh = new DashboardWideCardAdapter.DashboardWideCardAdapterViewHolder(v,mListner);
        return mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull DashboardWideCardAdapterViewHolder holder, int position) {
        DashboardBigCardItem currentItem = cards.get(position);
        try {
            Picasso.get().load(currentItem.getImageResource()).fit().into(holder.imageView);
            String [] name =currentItem.getUser().split(" ");
            holder.textView.setText(name[0]+" "+(name.length <= 1?"":name[1].charAt(0)+"."));
            holder.textView1.setText(currentItem.getName());
            holder.textView2.setText(currentItem.getPrice());
        }
        catch (Exception e){
        }
    }

    public DashboardWideCardAdapter(ArrayList<DashboardBigCardItem> cardList){
        cards = cardList;
    }

    public void setOnItemClickListner(OnItemClickListner listner){
        mListner = listner;
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    public static class DashboardWideCardAdapterViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageView;
        public TextView textView;
        public TextView textView1;
        public TextView textView2;

        public DashboardWideCardAdapterViewHolder(@NonNull View itemView, final OnItemClickListner listner) {
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
