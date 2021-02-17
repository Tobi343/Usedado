package com.company.usedado.Java.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.company.usedado.Java.adapter.ViewPagerAdapter;
import com.company.usedado.Java.fragment.fragment_activities_group;
import com.company.usedado.Java.fragment.fragment_activities_time;
import com.company.usedado.Java.items.DashboardBigCardItem;
import com.company.usedado.R;
import com.google.android.material.bottomnavigation.BottomNavigationMenu;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

public class activity_offerActivitys extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_activitys);

        tabLayout = (TabLayout) findViewById(R.id.offer_activitys_tabs);
        viewPager = (ViewPager) findViewById(R.id.offer_activitys_view);
        bottomNavigationView = findViewById(R.id.offer_activitys_bottom_navigation);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.AddFragment(new fragment_activities_time(),"New Activities");
        viewPagerAdapter.AddFragment(new fragment_activities_group(),"All Activities");
        viewPagerAdapter.AddFragment(new fragment_activities_group(),"Response");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_home:
                         startActivity(new Intent(getApplicationContext(), Dashboard.class));
                         overridePendingTransition(0,0);
                    case R.id.navigation_message:
                        return true;
                    case R.id.navigation_add:
                        startActivity(new Intent(getApplicationContext(), Add_offer.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.navigation_Auction:
                        //startActivity(new Intent(getApplicationContext(),Messages.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.navigation_catagories:
                        //startActivity(new Intent(getApplicationContext(),Messages.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }

    public void onItemClicks(DashboardBigCardItem item) {
        Intent intent = new Intent(getApplicationContext(), Offer_detail.class);
        intent.putExtra("PRODUCT_NAME",item.getName());
        intent.putExtra("PRODUCT_URI",item.getImageResource().toString());
        intent.putExtra("PRODUCT_PRICE",item.getPrice());
        intent.putExtra("PRODUCT_USER",item.getUID());
        intent.putExtra("PRODUCT_DELIVERYPRICE",item.getDeliveryPrice());
        intent.putExtra("PRODUCT_IMAGES",item.getImages());
        intent.putExtra("PRODUCT_DESC",item.getDescribtion());
        intent.putExtra("PRODUCT_ID",item.getofferID());
        startActivity(intent);
    }
}