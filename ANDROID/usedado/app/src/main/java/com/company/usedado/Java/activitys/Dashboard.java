package com.company.usedado.Java.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.company.usedado.Java.adapter.DashboardBigCardAdapter;
import com.company.usedado.Java.items.DashboardBigCardItem;
import com.company.usedado.Java.adapter.DashboardWideCardAdapter;
import com.company.usedado.Java.adapter.CatagorieAdapter;
import com.company.usedado.Java.items.CatagorieItem;
import com.company.usedado.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Dashboard extends AppCompatActivity {

    private  ImageView profile;
    private  Button searchAll;
    private  Button myAdds;

    private BottomNavigationView bottomNavigationView;

    private TextView name;

    private AutoCompleteTextView searchBar;

    private RecyclerView recyclerView;
    private RecyclerView recyclerView1;
    private RecyclerView recyclerView2;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private FirebaseUser user;


  private static final String TAG = "Dashboard";

    @Override
    protected void onResume() {
        super.onResume();
        if (getIntent().hasExtra("refresh")){
            GetDataFromFirestore();
        }
      if(user != null){
          user = FirebaseAuth.getInstance().getCurrentUser();

          Picasso.get().load(user.getPhotoUrl()).resize(600, 600).centerCrop().into(profile);
          name.setText("Hallo "+(getIntent().hasExtra("USER_NAME") ? getIntent().getStringExtra("USER_NAME").split(" ")[0].toString() : user.getDisplayName().split(" ")[0].toString())+",");
      }
      else {
          Toast.makeText(this, "Empty my dude", Toast.LENGTH_SHORT).show();
      }
    }
    ArrayList<DashboardBigCardItem> items = new ArrayList<DashboardBigCardItem>();
    ArrayList<DashboardBigCardItem> searchItems = new ArrayList<DashboardBigCardItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        startActivityForResult(new Intent(getApplicationContext(), SplashScreen.class),1);
        overridePendingTransition(0,0);


        SetOnClickListner();
        GetDataFromFirestore();
        user = FirebaseAuth.getInstance().getCurrentUser();
      if(user== null){
          startActivity(new Intent(getApplicationContext(), Login_Page.class));
          finish();
      }
      else {
          if(!user.isEmailVerified()){
              new AlertDialog.Builder(this).setTitle("Verify you Email!")
                      .setMessage("Dont forget to verify you email to publish an add.")
                      .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                          public void onClick(DialogInterface dialog, int which) {
                          }
                      })
                      .setIcon(android.R.drawable.ic_dialog_email).show();
          }
      }


    }




    private void GetDataFromFirestore(){
        CollectionReference colRef = FirebaseFirestore.getInstance().collection("Offers");
        colRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {


                for (QueryDocumentSnapshot queryDocumentSnapshot :task.getResult()) {
                    Map<String, Object> data = queryDocumentSnapshot.getData();
                    items.add(new DashboardBigCardItem( (ArrayList<String>) data.get("Images"),data.get("Name").toString(), data.get("Catagory").toString(),data.get("Price").toString(), data.get("DeliveryPrice").toString(),
                            data.get("User").toString(),data.get("UID").toString(),queryDocumentSnapshot.getId(),data.get("Describtion").toString(), Integer.parseInt(data.get("Aufrufe").toString()),(ArrayList<String>)data.get("AllowedPayments"),(Map<String, String>)data.get("Questions")));
                }
                String[] names = new String[items.size()];
                for (int i = 0; i < items.size(); i++) {
                    names[i] = items.get(i).getName();
                }
                searchItems.addAll(items);
                ArrayAdapter<String> search = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,names);
                searchBar.setAdapter(search);

                SetUpRecycler();
            }
        });
    }
    
    private void SetUpRecycler( ){

        recyclerView = findViewById(R.id.dashboard_long_viewRecycler);
        recyclerView2 = findViewById(R.id.dashboard_long_second_viewRecycler);
        recyclerView1 = findViewById(R.id.dashboard_wide_viewRecycler);

        recyclerView.setHasFixedSize(true);
        recyclerView2.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(llm);

        llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView2.setLayoutManager(llm);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);
        recyclerView1.setLayoutManager(gridLayoutManager);
        DashboardBigCardAdapter adapter;
        recyclerView.setAdapter(adapter = new DashboardBigCardAdapter(searchItems));
        adapter.setOnItemClickListner(new DashboardBigCardAdapter.OnItemClickListner() {
            @Override
            public void onItemClick(int position) {
                onItemClicks(searchItems.get(position));
            }
        });
        DashboardWideCardAdapter adapter1;
        recyclerView1.setAdapter(adapter1 = new DashboardWideCardAdapter(items));
        adapter1.setOnItemClickListner(new DashboardWideCardAdapter.OnItemClickListner() {
            @Override
            public void onItemClick(int position) {
                onItemClicks(items.get(position));
            }
        });
        ArrayList<CatagorieItem> catagorieItems = new ArrayList<CatagorieItem>();
        catagorieItems.add(new CatagorieItem(R.drawable.book,"Books",getResources().getColor(R.color.main_font)));
        catagorieItems.add(new CatagorieItem(R.drawable.clothes_hanger,"Clothes",getResources().getColor(R.color.main_orange)));
        catagorieItems.add(new CatagorieItem(R.drawable.gamepad,"Games",getResources().getColor(R.color.colorLighterGreen)));


        CatagorieAdapter adapter2;
        recyclerView2.setAdapter(adapter2 = new CatagorieAdapter(catagorieItems));

    }


    public void onItemClicks(DashboardBigCardItem item) {
        final DocumentReference docDb = FirebaseFirestore.getInstance().collection("Offers").document(item.getofferID());
        docDb.update("Aufrufe", FieldValue.increment(1));
        Intent intent = new Intent(getApplicationContext(), Offer_detail.class);
        intent.putExtra("PRODUCT_NAME",item.getName());
        intent.putExtra("PRODUCT_URI",item.getImageResource().toString());
        intent.putExtra("PRODUCT_PRICE",item.getPrice());
        intent.putExtra("PRODUCT_USER",item.getUID());
        intent.putExtra("PRODUCT_DELIVERYPRICE",item.getDeliveryPrice());
        intent.putExtra("PRODUCT_IMAGES",item.getImages());
        intent.putExtra("PRODUCT_DESC",item.getDescribtion());
        intent.putExtra("PRODUCT_ID",item.getofferID());
        intent.putExtra("PRODUCT_QUESTIONS", (Parcelable) item.getQuestions());
        startActivity(intent);
    }



        private void SetOnClickListner(){
        profile = findViewById(R.id.dashboard_profile);
        searchBar = findViewById(R.id.dashboard_searchbar);
        searchAll = findViewById(R.id.dashboard_searchAll);
        myAdds = findViewById(R.id.dashboard_btn_my_adds);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        name = findViewById(R.id.dashboard_name);

        bottomNavigationView.setSelectedItemId(R.id.navigation_home);

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchItems = new ArrayList<DashboardBigCardItem>();
                for (DashboardBigCardItem item : items) {
                    if(item.getName().toLowerCase().contains(s.toString().toLowerCase())){
                        searchItems.add(item);
                    }

                }
                SetUpRecycler();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_home:
                        return true;
                    case R.id.navigation_message:
                        startActivity(new Intent(getApplicationContext(), activity_offerActivitys.class));
                        overridePendingTransition(0,0);
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

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this, com.company.usedado.Java.activitys.profile.class));
            }
        });

        searchAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });
        myAdds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this, my_offers.class));
                overridePendingTransition(0,0);

            }
        });



    }

}