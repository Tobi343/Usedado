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

public class CategoryAdapterCardView extends RecyclerView.Adapter<CategoryAdapterCardView.CategoryAdapterCardViewViewHolder> {
    private CategoryAdapterCardView.OnItemClickListner mListner;

    private ArrayList<DashboardBigCardItem> cards = new ArrayList<DashboardBigCardItem>();

    @NonNull
    @Override
    public CategoryAdapterCardView.CategoryAdapterCardViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.categorie_card_2,parent,false);
        CategoryAdapterCardView.CategoryAdapterCardViewViewHolder mvh = new CategoryAdapterCardView.CategoryAdapterCardViewViewHolder(v,mListner);
        return mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapterCardView.CategoryAdapterCardViewViewHolder holder, int position) {
        DashboardBigCardItem currentItem = cards.get(position);
        try {
            Picasso.get().load(currentItem.getImageResource()).into(holder.imageView);
            holder.textView.setText(currentItem.getPrice());
            holder.textView1.setText(currentItem.getName());
            holder.textView2.setText("Name: "+currentItem.getUser());
        }
        catch (Exception e){
        }
    }

    public CategoryAdapterCardView(ArrayList<DashboardBigCardItem> cardList){
        cards = cardList;
    }

    @Override
    public int getItemCount() {
            return cards.size();
    }

    public interface OnItemClickListner{
        void onItemClick(int position);
    }

    public void setOnItemClickListner(CategoryAdapterCardView.OnItemClickListner listner){
        mListner = listner;
    }

    public static class CategoryAdapterCardViewViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageView;
        public TextView textView;
        public TextView textView1;
        public TextView textView2;


        public CategoryAdapterCardViewViewHolder(@NonNull View itemView, final CategoryAdapterCardView.OnItemClickListner listner) {
            super(itemView);
            imageView = itemView.findViewById(R.id.categorie_card_pic);
            textView = itemView.findViewById(R.id.categorie_card_price);
            textView1 = itemView.findViewById(R.id.categorie_card_title);
            textView2 = itemView.findViewById(R.id.categorie_card_name);

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

