package com.company.usedado.Java.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.company.usedado.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
public class AddOfferPictureCardAdapter extends RecyclerView.Adapter<AddOfferPictureCardAdapter.AddOfferPictureCardAdapterViewHolder> {


    private ArrayList<String> cards = new ArrayList<String>();
    private AddOfferPictureCardAdapter.OnItemClickListner mListner;

    public interface OnItemClickListner{
        void onItemClick(int position);
    }

    @NonNull
    @Override
    public AddOfferPictureCardAdapter.AddOfferPictureCardAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.picture_card,parent,false);
        AddOfferPictureCardAdapter.AddOfferPictureCardAdapterViewHolder mvh = new AddOfferPictureCardAdapter.AddOfferPictureCardAdapterViewHolder(v,mListner);
        return mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull AddOfferPictureCardAdapter.AddOfferPictureCardAdapterViewHolder holder, int position) {
        Uri currentItem = Uri.parse(cards.get(position));
        try {
                Picasso.get().load(currentItem).resize(600, 600)
                        .centerInside().into(holder.imageView);

        }
        catch (Exception e){
        }
    }

    public AddOfferPictureCardAdapter(ArrayList<String> cardList){
        cards = cardList;
    }

    public void setOnItemClickListner(AddOfferPictureCardAdapter.OnItemClickListner listner){
        mListner = listner;
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    public static class AddOfferPictureCardAdapterViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageView;
        public TextView textView;
        public TextView textView1;
        public TextView textView2;

        public AddOfferPictureCardAdapterViewHolder(@NonNull View itemView, final AddOfferPictureCardAdapter.OnItemClickListner listner) {
            super(itemView);
            imageView = itemView.findViewById(R.id.picture_card_picture);

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
