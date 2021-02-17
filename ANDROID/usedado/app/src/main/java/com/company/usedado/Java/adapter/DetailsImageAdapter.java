package com.company.usedado.Java.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.company.usedado.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DetailsImageAdapter extends RecyclerView.Adapter<DetailsImageAdapter.DetailsImageAdapterViewHolder> {

    private ArrayList<Uri> images = new ArrayList<Uri>();
    private DashboardBigCardAdapter.OnItemClickListner mListner;

    public interface OnItemClickListner{
        void onItemClick(int position);
    }
    @NonNull
    @Override
    public DetailsImageAdapter.DetailsImageAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.details_image_box,parent,false);
        DetailsImageAdapter.DetailsImageAdapterViewHolder mvh = new DetailsImageAdapter.DetailsImageAdapterViewHolder(v,mListner);
        return mvh;
    }

    public void setOnItemClickListner(DashboardBigCardAdapter.OnItemClickListner listner){
        mListner = listner;
    }


    @Override
    public void onBindViewHolder(@NonNull DetailsImageAdapterViewHolder holder, int position) {
        Uri currentItem = images.get(position);
        try {
            Picasso.get().load(currentItem).into(holder.imageView);
        }
        catch (Exception e){
        }
    }

    public DetailsImageAdapter(ArrayList<String> cardList){
        for (String s : cardList) {
            images.add(Uri.parse(s));

        }
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public static class DetailsImageAdapterViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageView;
        public DetailsImageAdapterViewHolder(@NonNull View itemView, final DashboardBigCardAdapter.OnItemClickListner listner) {
            super(itemView);
            imageView = itemView.findViewById(R.id.details_card_picture);
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
