package com.company.usedado.Java.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.company.usedado.Java.items.CatagorieItem;
import com.company.usedado.R;

import java.util.ArrayList;

public class CatagorieAdapter extends RecyclerView.Adapter<CatagorieAdapter.CatagorieAdapterViewHolder> {


        private ArrayList<CatagorieItem> cards = new ArrayList<CatagorieItem>();
        private CatagorieAdapter.OnItemClickListner mListner;


        public interface OnItemClickListner{
            void onItemClick(int position);
        }

        @NonNull
        @Override
        public CatagorieAdapter.CatagorieAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.catagorie_card,parent,false);
            CatagorieAdapter.CatagorieAdapterViewHolder mvh = new CatagorieAdapter.CatagorieAdapterViewHolder(v,mListner);
            return mvh;
        }

        @Override
        public void onBindViewHolder(@NonNull CatagorieAdapter.CatagorieAdapterViewHolder holder, int position) {
            CatagorieItem currentItem = cards.get(position);
            try {
                holder.imageView.setImageResource(currentItem.getImage());
                holder.card.setCardBackgroundColor(currentItem.getColorLayoutt());
                holder.textView.setText(currentItem.getTextCat());
            }
            catch (Exception e){
            }
        }

        public CatagorieAdapter(ArrayList<CatagorieItem> cardList){
            cards = cardList;
        }

        public void setOnItemClickListner(CatagorieAdapter.OnItemClickListner listner){
            mListner = listner;
        }

        @Override
        public int getItemCount() {
            return cards.size();
        }

        public static class CatagorieAdapterViewHolder extends RecyclerView.ViewHolder{

            public ImageView imageView;
            public TextView textView;
            public CardView card;

            public CatagorieAdapterViewHolder(@NonNull View itemView, final CatagorieAdapter.OnItemClickListner listner) {
                super(itemView);
                imageView = itemView.findViewById(R.id.catagorie_picture);
                textView = itemView.findViewById(R.id.catagorie_text);
                card = itemView.findViewById(R.id.catagorie_backCard);

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

