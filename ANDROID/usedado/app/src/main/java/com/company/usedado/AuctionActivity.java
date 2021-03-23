package com.company.usedado;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.company.usedado.Java.activitys.Offer_detail;
import com.company.usedado.Java.adapter.AuctionCardAdapter;
import com.company.usedado.Java.adapter.DashboardWideCardAdapter;
import com.company.usedado.Java.items.AuctionItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.concurrent.ThreadSafe;

public class AuctionActivity extends AppCompatActivity {

    ArrayList<AuctionItem> items1 = new ArrayList<>();

    private   AuctionCardAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auction);
        RecyclerView rw = findViewById(R.id.auctionRecycler);


        FirebaseDatabase.getInstance().getReference().child("Auction").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(AuctionActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();                }
                else {
                   DataSnapshot snapshot = task.getResult();
                   Map<String ,Map<String,Object>> items = (Map<String, Map<String, Object>>) snapshot.getValue();
                    items1.clear();

                    for (String s : items.keySet()) {
                        Map<String, Object> value = items.get(s);

                        //    public AuctionItem(String name, String auctionID, String startTime, String endTime, int startPrice, int recentPrice, String describtion, String image, FirebaseUser recentBidUser) {
                        items1.add(new AuctionItem(value.get("Name").toString(), s,
                                value.get("startTime").toString(),
                                value.get("endTime").toString()
                                , Integer.parseInt(value.get("startPrice").toString()) , Integer.parseInt(value.get("recentPrice").toString()), value.get("Describtion").toString(), value.get("Image").toString(),null));


                        //    public AuctionItem(String name,String auctionID,Date startTime, Date endTime, int startPrice, String describtion, Uri image) {

                    }

                     adapter = new AuctionCardAdapter(items1);

                    LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());

                    rw.setAdapter(adapter);
                    rw.setLayoutManager(llm);

                    adapter.setOnItemClickListner(new AuctionCardAdapter.OnItemClickListner() {
                        @Override
                        public void onItemClick(int position) {

                            Intent intent = new Intent(getApplicationContext(), AuctionDetailAcitivity.class);
                            Gson gson = new Gson();
                            String myJson = gson.toJson( items1.get(position));
                            intent.putExtra("PRODUCT", myJson);
                            startActivity(intent);
                        }
                    });
                }
            }
        });



        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.clear();

        long today = MaterialDatePicker.todayInUtcMilliseconds();

        CalendarConstraints.Builder constraintBuilder = new CalendarConstraints.Builder();
        constraintBuilder.setValidator(DateValidatorPointForward.now());

        MaterialDatePicker.Builder<Pair<Long,Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        builder.setTitleText("SELECT A DATE");
        builder.setCalendarConstraints(constraintBuilder.build());

        final  MaterialDatePicker materialDatePicker = builder.build();


        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                Pair<Long,Long> selectionn = (Pair<Long, Long>) selection;

            }
        });

        Button btn = findViewById(R.id.myAuctions);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),my_auctions.class));
            }
        });

        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.auction_swiper);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                FirebaseDatabase.getInstance().getReference().child("Auction").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(AuctionActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            DataSnapshot snapshot = task.getResult();
                            Map<String, Map<String, Object>> items = (Map<String, Map<String, Object>>) snapshot.getValue();
                            items1.clear();

                            for (String s : items.keySet()) {
                                Map<String, Object> value = items.get(s);


                                //    public AuctionItem(String name, String auctionID, String startTime, String endTime, int startPrice, int recentPrice, String describtion, String image, FirebaseUser recentBidUser) {
                                items1.add(new AuctionItem(value.get("Name").toString(), s,
                                        value.get("startTime").toString(),
                                        value.get("endTime").toString()
                                        , Integer.parseInt(value.get("startPrice").toString()), Integer.parseInt(value.get("recentPrice").toString()), value.get("Describtion").toString(), value.get("Image").toString(), null));


                                //    public AuctionItem(String name,String auctionID,Date startTime, Date endTime, int startPrice, String describtion, Uri image) {

                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },2000);
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab_add_auction);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //materialDatePicker.show(getSupportFragmentManager(),"DATE_PICKER");
                startActivity(new Intent(getApplicationContext(),createAutctionActivity.class));
            }
        });


        FirebaseDatabase.getInstance().getReference().child("Auction/").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                FirebaseDatabase.getInstance().getReference().child("Auction").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(AuctionActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            DataSnapshot snapshot = task.getResult();
                            Map<String, Map<String, Object>> items = (Map<String, Map<String, Object>>) snapshot.getValue();
                            items1.clear();

                            for (String s : items.keySet()) {
                                Map<String, Object> value = items.get(s);


                                //    public AuctionItem(String name, String auctionID, String startTime, String endTime, int startPrice, int recentPrice, String describtion, String image, FirebaseUser recentBidUser) {
                                items1.add(new AuctionItem(value.get("Name").toString(), s,
                                        value.get("startTime").toString(),
                                        value.get("endTime").toString()
                                        , Integer.parseInt(value.get("startPrice").toString()), Integer.parseInt(value.get("recentPrice").toString()), value.get("Describtion").toString(), value.get("Image").toString(), null));


                                //    public AuctionItem(String name,String auctionID,Date startTime, Date endTime, int startPrice, String describtion, Uri image) {

                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
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

        Thread t = new Thread(){
            @Override
            public void run() {
                while (!isInterrupted()){
                    try {
                        Thread.sleep(1000);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(adapter!=null){
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        });

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        t.start();

    }
}