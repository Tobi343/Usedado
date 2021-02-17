package com.company.usedado.Java.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.company.usedado.Java.adapter.YourOfferAdapter;
import com.company.usedado.Java.items.DashboardBigCardItem;
import com.company.usedado.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class my_offers extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FirebaseUser user;
    private ArrayList<DashboardBigCardItem> offers;
    private YourOfferAdapter ya;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_offers);
        recyclerView = findViewById(R.id.your_offer_recycler_view);
        user = FirebaseAuth.getInstance().getCurrentUser();
        GetData();

    }

    public void GetData()  {
        offers = new ArrayList<DashboardBigCardItem>();
        if (user != null) {
            Task<QuerySnapshot> task = db.collection("Offers")
                    .whereEqualTo("UID", user.getUid().toString())
                    .get();
            task.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
//String name, String topic, String price, String delivery, String user, String userID, String offerID, String describtion, int aufrufe, ArrayList<String> allowedPayments, ArrayList<Map<String, String>> questions) {

                                offers.add(new DashboardBigCardItem((ArrayList<String>) document.get("Images"),document.get("Name").toString(), null, document.get("Price").toString()+"€", null, null, null , document.getId(),null, Integer.parseInt(document.get("Aufrufe").toString()), null,(Map<String, String>) document.get("Questions")));
                        }
                        ya = new YourOfferAdapter(offers);

                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        recyclerView.setAdapter(ya);

                        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
                        itemTouchHelper.attachToRecyclerView(recyclerView);


                    } else {

                    }
                }
            });
        }
    }

    DashboardBigCardItem deletedAdd = null;

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            int pos = viewHolder.getAdapterPosition();

            switch (direction){
                case ItemTouchHelper.LEFT:
                    deletedAdd = offers.get(pos);
                    offers.remove(pos);
                    ya.notifyItemRemoved(pos);
                    Snackbar.make(recyclerView,"Add deleted: "+deletedAdd.getName(), BaseTransientBottomBar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            offers.add(pos,deletedAdd);
                            ya.notifyItemInserted(pos);
                        }
                    }).addCallback(new Snackbar.Callback(){
                        @Override
                        public void onDismissed(Snackbar snackbar, int event) {
                            //see Snackbar.Callback docs for event details
                            if(offers.size() > 0 &&  offers.get(pos).equals(deletedAdd)) {
                                Toast.makeText(my_offers.this, "UNDO", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(my_offers.this, deletedAdd.getofferID(), Toast.LENGTH_SHORT).show();
                                db.collection("Offers").document(deletedAdd.getofferID()).delete();
                            }

                        }
                    }).show();
                    break;
                case ItemTouchHelper.RIGHT:
                    //TODO bearbeitung öffnen....modify add_offer to reuse
                    break;
            }
        }
    };
}