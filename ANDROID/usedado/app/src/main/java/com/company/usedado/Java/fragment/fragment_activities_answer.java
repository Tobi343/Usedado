package com.company.usedado.Java.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.company.usedado.Java.activitys.my_offers;
import com.company.usedado.Java.adapter.answerOfferCardAdapter;
import com.company.usedado.Java.items.Offer_Offer_Item;
import com.company.usedado.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class fragment_activities_answer extends Fragment {

    View v;
    private RecyclerView recyclerView;
    private ArrayList<Offer_Offer_Item> activites = new ArrayList<>();
    answerOfferCardAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    public fragment_activities_answer() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       /* for (int i = 0; i < activites.size() - 1; i++) {
            if (activites.get(i).getState().equals(Offer_Offer_Item.OfferState.accepted)) {

            }
            else{
                activites.remove(i);
                i--;
            }
        }*/
        v = inflater.inflate(R.layout.fragment_view_groups,container,false);
        recyclerView = v.findViewById(R.id.fragment_view_group_recycler_view);
        swipeRefreshLayout = v.findViewById(R.id.group_swiper);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchData();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },2000);
            }
        });
        adapter= new answerOfferCardAdapter(activites);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        fetchData();
        return v;
    }

  /*  ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
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
                                db.collection("Offers").document(deletedAdd.getofferID()).delete().addOnFailureListener(new OnFailureListener() {
                                                                                                                            @Override
                                                                                                                            public void onFailure(@NonNull Exception e) {
                                                                                                                                String message = e.getMessage();
                                                                                                                                Toast.makeText(my_offers.this, message, Toast.LENGTH_SHORT).show();

                                                                                                                            }
                                                                                                                        }
                                );
                            }

                        }
                    }).show();
                    break;
                case ItemTouchHelper.RIGHT:
                    //TODO bearbeitung öffnen....modify add_offer to reuse
                    break;
            }
        }
    };*/

    public void fetchData() {
        CollectionReference reference = FirebaseFirestore.getInstance().collection("Activities");
        reference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                activites.clear();

                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                    Map<String, Object> data = queryDocumentSnapshot.getData();
                    if (data.get("UserID").toString().equals(FirebaseAuth.getInstance().getUid())) {
                        Offer_Offer_Item item;
                        activites.add(item = new Offer_Offer_Item(
                                Uri.parse(data.get("Image").toString()),
                                data.get("Title").toString(),
                                Integer.parseInt(data.get("OriginalPrice").toString()),
                                Integer.parseInt(data.get("OfferdPrice").toString()),
                                data.get("AdditionalComments").toString(),
                                Offer_Offer_Item.OfferState.valueOf(data.get("State").toString()),
                                data.get("UserID").toString(),
                                data.get("OfferID").toString(),
                                data.get("OwnerID").toString(),
                                data.get("Method").toString()));
                        try {
                            item.setPayAddress(data.get("PaymentAddress").toString());

                        }
                        catch (Exception e){

                        }
                        item.setAddress(data.get("DeliveryAddress").toString());
                        item.setActivityID(queryDocumentSnapshot.getId());


                    }

                }
                for (int i = 0; i < activites.size() - 1; i++) {
                    if (activites.get(i).getState().equals(Offer_Offer_Item.OfferState.accepted)) {

                    }
                    else{
                        activites.remove(i);
                        i--;
                    }
                }
                adapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });


    }

}
