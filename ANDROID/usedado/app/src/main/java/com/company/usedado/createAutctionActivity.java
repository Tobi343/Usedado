package com.company.usedado;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.company.usedado.Java.activitys.profile;
import com.company.usedado.Java.items.AuctionItem;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;

public class createAutctionActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView image;
    private Uri imageUri;
    private String ID;
    static Random rnd = new Random();
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_autction);
        ID = Long.toHexString(Double.doubleToLongBits(Math.random()));

        user = FirebaseAuth.getInstance().getCurrentUser();

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.clear();

        long today = MaterialDatePicker.todayInUtcMilliseconds();

        CalendarConstraints.Builder constraintBuilder = new CalendarConstraints.Builder();
        constraintBuilder.setValidator(DateValidatorPointForward.now());

        MaterialDatePicker.Builder<Pair<Long,Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        builder.setTitleText("SELECT A DATE");
        builder.setCalendarConstraints(constraintBuilder.build());

        final  MaterialDatePicker materialDatePicker = builder.build();

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        LinearLayout dateSelector = findViewById(R.id.dateSelection);
        TextView name = findViewById(R.id.auctionName);
        TextView startDate = findViewById(R.id.startDate);
        TextView endDate = findViewById(R.id.endDate);
        TextView startTime = findViewById(R.id.startTime);
        TextView endTime = findViewById(R.id.endTime);
        Button finishBtn = findViewById(R.id.auction_add_button_finish);
        EditText price = findViewById(R.id.minimumPriceAuction);
        EditText desc = findViewById(R.id.descriptionAuction);
        image = findViewById(R.id.imageAuction);

        dateSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDatePicker.show(getSupportFragmentManager(),"DATE_PICKER");
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                                    Intent intent = new Intent();
                                    intent.setType("image/*");
                                    intent.setAction(Intent.ACTION_GET_CONTENT);
                                    startActivityForResult(intent,PICK_IMAGE_REQUEST);


            }
        });

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                Pair<Long,Long> selectionn = (Pair<Long, Long>) selection;
                String pattern = "yyyy/MM/dd";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                startDate.setText(simpleDateFormat.format(new Date(selectionn.first)));
                endDate.setText(simpleDateFormat.format(new Date(selectionn.second)));

            }
        });

        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int mHour = c.get(Calendar.HOUR_OF_DAY);
                int mMin = c.get(Calendar.MINUTE);

                TimePickerDialog tpd =new TimePickerDialog(createAutctionActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        endTime.setText((hourOfDay<10 ? "0"+hourOfDay:hourOfDay)+":"+(minute<10 ? "0"+minute:minute));
                    }
                },mHour,mMin,true);
                tpd.show();
            }
        });

        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int mHour = c.get(Calendar.HOUR_OF_DAY);
                int mMin = c.get(Calendar.MINUTE);

                TimePickerDialog tpd =new TimePickerDialog(createAutctionActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        startTime.setText((hourOfDay<10 ? "0"+hourOfDay:hourOfDay)+":"+(minute<10 ? "0"+minute:minute));
                    }
                },mHour,mMin,true);
                tpd.show();
            }
        });

        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Date startDates = new Date();
                Date endDates = new Date();

                
                try {
                    startDates =  new SimpleDateFormat("yyyy/MM/dd/hh:mm").parse((startDate.getText()+"/"+startTime.getText()));
                    endDates =  new SimpleDateFormat("yyyy/MM/dd/hh:mm").parse((endDate.getText()+"/"+endTime.getText()));
                } catch (ParseException e) {
                    Toast.makeText(createAutctionActivity.this, "Parsing Error!", Toast.LENGTH_SHORT).show();
                    return;
                }


                if(desc.getText().toString().isEmpty() || price.getText().toString().isEmpty()){
                    Toast.makeText(createAutctionActivity.this, "Error! Invalid Input!", Toast.LENGTH_SHORT).show();
                }
                else if(startDates.before(new Date())){
                    Toast.makeText(createAutctionActivity.this, "StartDate invalid!", Toast.LENGTH_SHORT).show();
                }
                else if(startDates.after(endDates)){
                    Toast.makeText(createAutctionActivity.this, "Dates are Invalid!", Toast.LENGTH_SHORT).show();
                }
                else if(image.getTag().toString().isEmpty()){
                    Toast.makeText(createAutctionActivity.this, "No Image", Toast.LENGTH_SHORT).show();
                }
                else if(name.getText().toString().isEmpty()){
                    Toast.makeText(createAutctionActivity.this, "Name invalid!", Toast.LENGTH_SHORT).show();
                }
                else {
                    AuctionItem item = new AuctionItem(name.getText().toString(),ID,(startDate.getText()+"/"+startTime.getText()),(endDate.getText()+"/"+endTime.getText()),Integer.parseInt(price.getText().toString()),desc.getText().toString(), image.getTag().toString(),FirebaseAuth.getInstance().getCurrentUser());

                    Map<String, Object> item1 = item.toMap();

                    database.child("Auction").child(ID).setValue(item1).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(createAutctionActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(createAutctionActivity.this, "Success", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            imageUri = data.getData();
            UploadFile();
        }
        else if(resultCode == AutocompleteActivity.RESULT_ERROR){
            Status status = Autocomplete.getStatusFromIntent(data);
            Toast.makeText(this, status.getStatusMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private String GetFileExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void UploadFile(){
        if(imageUri != null){

            StorageReference fileRef = FirebaseStorage.getInstance().getReference().child(user.getUid()+"_profilePicture."+GetFileExtension(imageUri));
            fileRef.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    String a = task.getResult().toString();
                                    image.setTag(a);
                                    Picasso.get().load(imageUri).resize(600, 600) // resizes the image to these dimensions (in pixel)
                                            .centerCrop().into(image);
                                }
                            });


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(createAutctionActivity.this, "Failure!", Toast.LENGTH_SHORT).show();
                        }
                    });

        }
        else {
            Toast.makeText(this, "Image upload failure!", Toast.LENGTH_SHORT).show();
        }
    }


}