package com.company.usedado.Java.activitys;

import android.content.Intent;
import android.graphics.Color;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.company.usedado.Java.adapter.DashboardBigCardAdapter;
import com.company.usedado.Java.adapter.DetailQuestionAdapter;
import com.company.usedado.Java.adapter.DetailsImageAdapter;
import com.company.usedado.Java.dialogs.dialog_profile;
import com.company.usedado.Java.dialogs.dialog_offered_price;
import com.company.usedado.Java.items.*;
import com.company.usedado.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Offer_detail extends AppCompatActivity implements OnMapReadyCallback,dialog_offered_price.dialog_offered_price_Listener {
    private static final String TAG = "OFFER_DETAILS";

    //TODO dont allow more than one "contact the seller"

    private ImageView bigPicture;
    private ImageView userPicture;
    private TextView userName;
    private TextView userAddress;
    private TextView productName;
    private TextView productPrice;
    private TextView productDisc;
    private GoogleMap map;
    private ImageView expandMap;
    private RelativeLayout userProfile;
    private RecyclerView images;
    private View maps;
    private ImageView like;
    private String favouriteState = "";
    private String offerID;
    private String address;
    private TextView deliveryPrice;
    private Button contactSeller;
    private Uri startPhoto;
    private ImageView back;
    private ImageView share;
    private RecyclerView rw;
    private DashboardBigCardItem item;
    private ImageView payPal;
    private ImageView bar;
    private ImageView bank;
    private TextView sonstige;
    private TextView answerTo;
    private boolean answer = false;
    private EditText question;
    private Button sendQuestion;
    private Integer answerIndex = 0;
    private FirebaseUser user;
    private FirebaseFirestore firestore;
    ArrayList<Pair<String,String>> questions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_detail);
        contactSeller = findViewById(R.id.offer_detail_contact_seller_button);
        bigPicture = findViewById(R.id.details_BigProfilePicture);
        userPicture = findViewById(R.id.details_userPicture);
        productName = findViewById(R.id.details_productName);
        productPrice = findViewById(R.id.details_productPrice);
        deliveryPrice = findViewById(R.id. details_text_shipping_price);
        userName = findViewById(R.id.details_productUser);
        userAddress = findViewById(R.id.details_productAddress);
        userProfile = findViewById(R.id.details_profile_overview);
        question = findViewById(R.id.details_question);
        sendQuestion = findViewById(R.id.details_send_question);
        maps = findViewById(R.id.maps);
        maps.setVisibility(View.GONE);
        rw = findViewById(R.id.activity_offer_detail_recyclerView_questions);
        like = findViewById(R.id.details_mark_as_important);
        expandMap = findViewById(R.id.details_appenMap);
        productDisc=findViewById(R.id.details_description);
        back = findViewById(R.id.details_back);
        share = findViewById(R.id.details_share);
        payPal = findViewById(R.id.offer_detail_payPal);
        bank = findViewById(R.id.offer_detail_bank);
        bar = findViewById(R.id.offer_detail_bar);
        sonstige = findViewById(R.id.offer_detail_sonstiges);
        answerTo = findViewById(R.id.details_answer_to);
        favouriteState = "unlike";
        user = FirebaseAuth.getInstance().getCurrentUser();
        firestore = FirebaseFirestore.getInstance();



        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(like.getTag() == "like"){
                    like.setImageResource(R.drawable.ic_baseline_star_border_24);
                    like.setColorFilter(Color.argb(255, 255, 255, 255));
                    like.setTag("unlike");
                }
                else{
                    like.setImageResource(R.drawable.ic_baseline_star_24);
                    like.setColorFilter(Color.argb(255, 252, 186, 3));
                    like.setTag("like");
                }
            }
        });
        final int beginHeight = userProfile.getLayoutParams().height;
        expandMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(maps.getVisibility() == View.GONE){
                    PrepareMap();
                }
                else {
                    maps.setVisibility(View.GONE);
                    userProfile.getLayoutParams().height = beginHeight;
                }
            }
         });


        contactSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_offered_price dop = new dialog_offered_price(Integer.parseInt(productPrice.getText().toString().replace("€","")),0,item.getAllowedPayments());
                dop.show(getSupportFragmentManager(),"example Dialog");
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String body = "Hey I have found this insane ad on this new modern platform: https://play.google.com/store/apps";
                String sub = "Usedado Market";
                intent.putExtra(Intent.EXTRA_SUBJECT,sub);
                intent.putExtra(Intent.EXTRA_TEXT,body);
                startActivity(Intent.createChooser(intent,"Share via. "));
            }
        });
        Gson gson = new Gson();

        this.item =gson.fromJson(getIntent().getStringExtra("PRODUCT"),DashboardBigCardItem.class);
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("Users").document(item.getUID());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                address=documentSnapshot.get("address").toString();
                userAddress.setText(address.split(",")[0]);
                userName.setText(documentSnapshot.get("name").toString());
                Picasso.get().load(Uri.parse(documentSnapshot.get("profilePicture").toString())).into(userPicture);
                DocumentReference docRef = FirebaseFirestore.getInstance().collection("Users").document(user.getUid());
                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        ArrayList<String> favs = (ArrayList<String>) documentSnapshot.get("favourites");
                        if(favs.contains(offerID)){
                            like.setImageResource(R.drawable.ic_baseline_star_24);
                            like.setColorFilter(Color.argb(255, 252, 186, 3));
                            like.setTag("like");
                            favouriteState = "like";
                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Offer_detail.this, "Error while loading data!", Toast.LENGTH_SHORT).show();
            }
        });



        payPal.setVisibility(View.GONE);
        sonstige.setVisibility(View.GONE);
        bank.setVisibility(View.GONE);
        bar.setVisibility(View.GONE);
        productName.setText(item.getName());
        productPrice.setText(item.getPrice());
        String dp = item.getDeliveryPrice();
        deliveryPrice.setText(dp.equals("")?"Free Shipping":"Shipping: "+dp+" €");
        startPhoto = Uri.parse(item.getImageResource());
        offerID = item.getofferID();
        productDisc.setText(item.getDescribtion());

        rw.setLayoutManager(new LinearLayoutManager(this));
        DetailQuestionAdapter adapter;
        rw.setAdapter(adapter = new DetailQuestionAdapter(questions));

        adapter.setOnItemClickListner(new DetailQuestionAdapter.OnItemClickListner() {
            @Override
            public void onItemClick(int position) {
                answerTo.setText("Answer to "+adapter.cards.get(position).first.split(";")[0]);
                answer = true;
                answerIndex = position+1;
                answerTo.setVisibility(View.VISIBLE);
            }
        });
        DocumentReference doc = firestore.collection("Offers").document(item.getofferID());
        doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.get("Questions") != null){
                    for (String stringStringPair : (ArrayList<String>)documentSnapshot.get("Questions")) {
                        String[]splitter = stringStringPair.split(";");
                        questions.add(new Pair<>(splitter[0]+";"+splitter[1]+(splitter.length > 3?";true":""),(splitter.length > 3?splitter[4]:splitter[2])));
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });


        answerTo.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            answer = false;
                                            answerTo.setVisibility(View.INVISIBLE);
                                        }
                                    }
        );
        sendQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!question.getText().equals("")){
                    questions.add(answer?answerIndex: questions.size(),new Pair<>(user.getDisplayName()+";"+user.getUid()+(answer?";true":""),question.getText().toString()));
                    adapter.notifyDataSetChanged();
                    answer = false;
                    answerTo.setVisibility(View.INVISIBLE);
                    question.setText("");
                }
            }
        });

        for (String allowedPayment : item.getAllowedPayments()) {
            switch (allowedPayment){
                case "payPal":
                    payPal.setVisibility(View.VISIBLE);
                    break;
                case "cash":
                    bar.setVisibility(View.VISIBLE);
                    break;
                case "bank":
                    bank.setVisibility(View.VISIBLE);
                    break;
                default:
                    sonstige.setText(allowedPayment);
                    sonstige.setVisibility(View.VISIBLE);
            }
        }
        
        Picasso.get().load(startPhoto).into(bigPicture);
        images = findViewById(R.id.details_recycler_images);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        images.setLayoutManager(llm);
        final ArrayList<String> imagesList =item.getImages();
        DetailsImageAdapter adapter1 = new DetailsImageAdapter(imagesList);
        images.setAdapter(adapter1);
        adapter1.setOnItemClickListner(new DashboardBigCardAdapter.OnItemClickListner() {
            @Override
            public void onItemClick(int position) {
                Picasso.get().load(Uri.parse(imagesList.get(position))).into(bigPicture);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("Users").document(user.getUid());
        if(!favouriteState.equals(like.getTag())){
            if(like.getTag() == "like"){
               docRef.update("favourites", FieldValue.arrayUnion(offerID));
            }
             else {
                  docRef.update("favourites", FieldValue.arrayRemove(offerID));
              }
        }
        docRef = FirebaseFirestore.getInstance().collection("Offers").document(item.getofferID());
        Map<String,ArrayList> data = new HashMap<>();
        ArrayList<String> arrayList = new ArrayList<>();
        for (Pair<String, String> stringStringPair : questions) {
            String[] splitter = stringStringPair.first.split(";");
            arrayList.add(stringStringPair.first+";"+(splitter.length > 2 ? "Type: Answer;":"")+stringStringPair.second);
        }
        data.put("Questions",arrayList);
        docRef.set(data, SetOptions.merge()).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Offer_detail.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void PrepareMap(){
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maps);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            map = googleMap;
            Geocoder coder = new Geocoder(this);
            //List<Address> address = coder.getFromLocationName(/*userAddress.getText().toString()*/"Donaustadtstraße 45",5);
            //Address location=address.get(0);
            String[]spliter = address.split(":")[1].replace("(","").replace(")","").split(",");
            map.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(spliter[0]),Double.parseDouble(spliter[1]))));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(spliter[0]),Double.parseDouble(spliter[1])),14.0f));
            userProfile.getLayoutParams().height = 900;
            maps.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            Log.d(TAG,e.getMessage());
        }
    }


    @Override
    public void applyTexts(Integer newText, String method) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("UserID", user.getUid());
        data.put("OfferID", offerID.toString());
        data.put("Image", startPhoto.toString());
        data.put("Method", method);
        data.put("Title", productName.getText().toString());
        data.put("OriginalPrice", Integer.parseInt(productPrice.getText().toString().replace("€","")));
        data.put("OfferdPrice", newText);
        data.put("AdditionalComments", "You have got an Offer from: "+user.getDisplayName());
        data.put("State", Offer_Offer_Item.OfferState.asked.toString());
        data.put("LastUpdate", new Date());
        data.put("OwnerID", item.getUID());
        CollectionReference colDb = FirebaseFirestore.getInstance().collection("Activities");
        colDb.document().set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(Offer_detail.this, "Successfully created!", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Offer_detail.this, "Failure", Toast.LENGTH_SHORT).show();
                Log.d("Offer",e.toString());
            }
        });

    }
}
