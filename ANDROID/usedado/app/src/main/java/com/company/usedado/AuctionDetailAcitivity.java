package com.company.usedado;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.company.usedado.Java.AuctionSystem;
import com.company.usedado.Java.items.AuctionItem;
import com.company.usedado.Java.items.DashboardBigCardItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.Date;
import java.util.Map;


public class AuctionDetailAcitivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auction_detail_acitivity);
        final TickerView tickerView = findViewById(R.id.tickerViewPrice);
        tickerView.setCharacterLists(TickerUtils.provideNumberList());
        AuctionSystem system = new AuctionSystem();

        ImageView reduce = findViewById(R.id.remove_counter);
        ImageView add = findViewById(R.id.add_counter);
        Button setBid = findViewById(R.id.setBid);
        TextView txt = findViewById(R.id.price_anzeige);
        TextView desc = findViewById(R.id.auctionDescribtion);
        TextView title = findViewById(R.id.auctionTitleDetail);
        TextView endTime = findViewById(R.id.auctionEndDate);
        TextView oriPrice = findViewById(R.id.auctionOriginalPrice);
        ImageView image = findViewById(R.id.auctionDetailImage);

        Gson gson = new Gson();

        AuctionItem item =gson.fromJson(getIntent().getStringExtra("PRODUCT"), AuctionItem.class);

        oriPrice.setText("Original Price: "+ item.getStartPrice()+"€");
        txt.setText(item.getRecentPrice()+"€");
        endTime.setText(item.getEndTime());
        title.setText(item.getName());
        desc.setText(item.getDescribtion());
        Picasso.get().load(item.getImage()).into(image);

        final int[] value = {item.getRecentPrice()};



        setBid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Integer.valueOf(tickerView.getText().replace("€","") )< value[0]){
                    system.setBid(item.getAuctionID(), value[0], FirebaseAuth.getInstance().getCurrentUser());
                }
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               txt.setText((++value[0])+"€");

            }
        });

        reduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt.setText((--value[0])+"€");
            }
        });

        tickerView.setText(item.getRecentPrice()+"€");




        FirebaseDatabase.getInstance().getReference().child("Auction/"+item.getAuctionID()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.getKey().equals("recentPrice")){
                    long snapshot1 = (long) snapshot.getValue();
                    value[0] = (int) snapshot1;
                    tickerView.setText(snapshot1+"€");

                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    public void FetchData(){

    }

}