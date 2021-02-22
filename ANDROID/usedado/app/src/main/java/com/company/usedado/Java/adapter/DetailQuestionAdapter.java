package com.company.usedado.Java.adapter;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.company.usedado.Java.items.DashboardBigCardItem;
import com.company.usedado.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DetailQuestionAdapter extends RecyclerView.Adapter<DetailQuestionAdapter.DetailQuestionAdapterViewHolder> {


    public ArrayList<Pair<String,String>> cards = new ArrayList<>();
    private OnItemClickListner mListner;


    public interface OnItemClickListner{
        void onItemClick(int position);
    }

    @NonNull
    @Override
    public DetailQuestionAdapter.DetailQuestionAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_card,parent,false);
        DetailQuestionAdapter.DetailQuestionAdapterViewHolder mvh = new DetailQuestionAdapter.DetailQuestionAdapterViewHolder(v,mListner);
        return mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull DetailQuestionAdapterViewHolder holder, int position) {
        Pair<String,String> currentItem = cards.get(position);
        try {
            String[] splitter = currentItem.first.split(";");
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) holder.cv.getLayoutParams();
            p.setMargins(splitter.length > 2 ? 100:0,0,0,0);
            holder.cv.setLayoutParams(p);
            holder.textView.setText(splitter[0]);
            holder.textView1.setText(currentItem.second);


        }
        catch (Exception e){
        }
    }

    public DetailQuestionAdapter(ArrayList<Pair<String,String>> cardList){
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
        public CardView cv;

        public DetailQuestionAdapterViewHolder(@NonNull View itemView, final OnItemClickListner listner) {
            super(itemView);
            textView = itemView.findViewById(R.id.question_card_name);
            textView1 = itemView.findViewById(R.id.question_card_question);
            cv = itemView.findViewById(R.id.question_card_card);
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

