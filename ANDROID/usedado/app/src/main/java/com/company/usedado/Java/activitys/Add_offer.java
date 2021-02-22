package com.company.usedado.Java.activitys;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.company.usedado.Java.adapter.AddOfferPictureCardAdapter;
import com.company.usedado.Java.items.DashboardBigCardItem;
import com.company.usedado.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Add_offer extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private static final String TAG = "Add_offer";
    private Button abortBtn;
    private Button finishBtn;

    private EditText describtion;
    private EditText name;
    private EditText price;
    private EditText catagory;
    private EditText otherPaymentMethod;
    private EditText deliveryPrice;
    private EditText lastPrice;

    private RadioGroup radioGroup;
    private RadioButton fixedPrice;
    private RadioButton negotiablePrice;

    private RecyclerView recyclerView;

    private LinearLayout lastPriceLayout;

    private CheckBox all;
    private CheckBox payPal;
    private CheckBox bank;
    private CheckBox cash;
    private CheckBox other;
    private AddOfferPictureCardAdapter adapter;
    private Uri mImageUri;

    private TextView priceView;

    private DashboardBigCardItem item;

    ArrayList<String> allowedPayments;
    ArrayList<String> picList;
    ArrayList<String> paymentMethods;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_offer);
        SetOnClickListner();
        if(getIntent().hasExtra("PRODUCT")){
            LoadForEdit();
        }
    }

    private void LoadForEdit(){
        Gson gson = new Gson();
        item = gson.fromJson(getIntent().getStringExtra("PRODUCT"), DashboardBigCardItem.class);
        for (String image : item.getImages()) {
            picList.add(0,image);
        }
        adapter.notifyDataSetChanged();
        catagory.setText(item.getTopic());
        price.setText(item.getPrice().replace("€",""));
        describtion.setText(item.getDescribtion());
        name.setText(item.getName());
        deliveryPrice.setText(item.getDeliveryPrice());
        for (String allowedPayment : item.getAllowedPayments()) {
            if(allowedPayment.equals("payPal"))
                payPal.setChecked(true);
            if(allowedPayment.equals("cash"))
                cash.setChecked(true);
            if(allowedPayment.equals("bank"))
                bank.setChecked(true);
        }
    }

    private void SetOnClickListner() {
        abortBtn = findViewById(R.id.offer_add_button_abort);
        finishBtn = findViewById(R.id.offer_add_button_finish);
        picList = new ArrayList<String>();
        allowedPayments = new ArrayList<String>();
        fixedPrice = findViewById(R.id.offer_add_radio_fixed);
        negotiablePrice = findViewById(R.id.offer_add_radio_negotiable);
        recyclerView = findViewById(R.id.offer_add_recyclerView);
        deliveryPrice = findViewById(R.id.offer_add_text_shipping_price);
        describtion = findViewById(R.id.offer_add_text_description);
        name = findViewById(R.id.offer_add_text_name);
        price = findViewById(R.id.offer_add_text_price);
        catagory = findViewById(R.id.offer_add_text_category);
        lastPrice = findViewById(R.id.offer_add_text_last_price);
        all = findViewById(R.id.offer_add_checkbox_accept_all);
        payPal = findViewById(R.id.offer_add_checkbox_pay_pal);
        bank = findViewById(R.id.offer_add_checkbox_bank);
        cash = findViewById(R.id.offer_add_checkbox_cash);
        other = findViewById(R.id.offer_add_checkbox_else);
        priceView = findViewById(R.id.offer_add_fixedPriceText);
        otherPaymentMethod = findViewById(R.id.offer_add_text_apm_else);
        radioGroup = findViewById(R.id.offer_add_radio_group);
        lastPriceLayout = findViewById(R.id.offer_add_last_price_layout);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(fixedPrice.isChecked()){
                    lastPriceLayout.setVisibility(View.GONE);
                }
                else {
                    lastPriceLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(payPal.isChecked())
                    allowedPayments.add("payPal");
                if(cash.isChecked())
                    allowedPayments.add("cash");
                if(bank.isChecked())
                    allowedPayments.add("bank");
                if(other.isChecked())
                    allowedPayments.add(otherPaymentMethod.getText().toString());
                picList.remove(picList.size()-1);
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                HashMap<String, Object> data = new HashMap<>();
                data.put("User", user.getDisplayName());
                data.put("Name", name.getText().toString());
                data.put("Catagory", catagory.getText().toString());
                data.put("Price", price.getText().toString());
                data.put("DeliveryPrice", deliveryPrice.getText().toString());
                data.put("Describtion", describtion.getText().toString());
                data.put("Aufrufe", getIntent().hasExtra("PRODUCT")?item.getAufrufe():0);
                data.put("Questions", new ArrayList<String>());
                data.put("UID", user.getUid());
                data.put("Images", picList);
                data.put("lastPrice", lastPrice.getText().toString());
                data.put("AllowedPayments",allowedPayments);
                CollectionReference colDb = FirebaseFirestore.getInstance().collection("Offers");
                DocumentReference docRef = colDb.document();
                if(getIntent().hasExtra("PRODUCT")){
                    docRef = colDb.document(item.getofferID());
                }
                docRef.set(data, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(Add_offer.this, "Successfully created!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                });

                final DocumentReference docDb = FirebaseFirestore.getInstance().collection("Users").document(user.getUid());
                docDb.update("offers", FieldValue.increment(1));
            }
        });

        abortBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();

            }
        });

        all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    payPal.setChecked(true);
                    cash.setChecked(true);
                    bank.setChecked(true);

                } else {
                    payPal.setChecked(false);
                    cash.setChecked(false);
                    bank.setChecked(false);
                }
            }
        });

        other.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    otherPaymentMethod.setVisibility(View.VISIBLE);
                } else {
                    otherPaymentMethod.setVisibility(View.GONE);
                }
            }
        });

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(RecyclerView.HORIZONTAL);

        //TODO add button in box
        picList.add(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                getApplicationContext().getResources().getResourcePackageName(R.drawable.ic_baseline_add_24) + '/' +
                getApplicationContext().getResources().getResourceTypeName(R.drawable.ic_baseline_add_24) + '/' +
                getApplicationContext().getResources().getResourceEntryName(R.drawable.ic_baseline_add_24));


        adapter = new AddOfferPictureCardAdapter(picList);
        recyclerView.setLayoutManager(llm);
        recyclerView.hasFixedSize();
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListner(new AddOfferPictureCardAdapter.OnItemClickListner() {
            @Override
            public void onItemClick(int position) {
                if (position == picList.size() - 1) {
                    openFileChooser();
                }
            }
        });
    }

    private void openDialog(){
        new AlertDialog.Builder(this).setTitle("Abort Offer")
                .setMessage("Do you really want to abort this offer?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                       finish();
                    }
                })
                .setNegativeButton(android.R.string.no, null).setIcon(android.R.drawable.ic_dialog_info).show();
    }

    private void openFileChooser() {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mImageUri = data.getData();
            UploadFile();
        }

    }

    private String GetFileExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }
    static Random rnd = new Random();
    private void UploadFile() {
        if (mImageUri != null) {
            StorageReference ref = FirebaseStorage.getInstance().getReference("OfferPics");
            ref.child(String.valueOf(rnd.nextInt(10000))).putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    picList.add(0,task.getResult().toString());
                                    adapter.notifyItemInserted(0);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(getApplicationContext(), "Failure!", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "Image upload failure!", Toast.LENGTH_SHORT).show();
        }
    }
}