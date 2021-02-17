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
import java.util.Map;

public class DetailQuestionAdapter extends RecyclerView.Adapter<DetailQuestionAdapter.DetailQuestionAdapterViewHolder> {


    private ArrayList<Map<String,String>> cards = new ArrayList<>();
    private OnItemClickListner mListner;


    public interface OnItemClickListner{
        void onItemClick(int position);
    }

    @NonNull
    @Override
    public DetailQuestionAdapter.DetailQuestionAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.wide_card,parent,false);
        DetailQuestionAdapter.DetailQuestionAdapterViewHolder mvh = new DetailQuestionAdapter.DetailQuestionAdapterViewHolder(v,mListner);
        return mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull DetailQuestionAdapterViewHolder holder, int position) {
        Map<String,String> currentItem = cards.get(position);
        try {

        }
        catch (Exception e){
        }
    }

    public DetailQuestionAdapter(ArrayList<Map<String,String>> cardList){
        cards = cardList;
    }

    public void setOnItemClickListner(OnItemClickListner listner){
        mListner = listner;
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    public static class DetailQuestionAdapterViewHolder extends RecyclerView.ViewHolder{


        public TextView textView;
        public TextView textView1;

        public DetailQuestionAdapterViewHolder(@NonNull View itemView, final OnItemClickListner listner) {
            super(itemView);
            textView = itemView.findViewById(R.id.dashboard_card_big_name);
            textView1 = itemView.findViewById(R.id.dashboard_card_big_topic);

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

