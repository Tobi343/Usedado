package com.company.usedado.Java.activitys;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.company.usedado.Java.dialogs.dialog_profile;
import com.company.usedado.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class profile extends AppCompatActivity implements dialog_profile.dialog_profile_Listener {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String TAG = "profile";

    private ImageView profile_picture;
    private TextView name;
    private TextView ads;
    private TextView message;
    private TextView member;
    private TextView email;
    private TextView phone;
    private TextView address;
    private TextView memberSince;
    private TextView password;

    private LinearLayout changeEmail;
    private LinearLayout changePhone;
    private LinearLayout changeAddress;
    private LinearLayout changePassword;

    private ImageView back;
    private ImageView logout;

    private EditText editAddress;

    private Uri mImageUri;

    private StorageReference mStorageRef;
    private FirebaseUser user;

    private final static String CHANGE_EMAIL_TITLE = "Change you Email!";
    private final static String CHANGE_PHONE_TITLE = "Change you Phone!";
    private final static String CHANGE_ADDRESS_TITLE = "Change you Address!";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        SetOnClickListeners();
        GetData();

        if(!Places.isInitialized()){
            Places.initialize(getApplicationContext(),getString(R.string.map_key));
        }

    }

    public void GetData() {

       if(user != null){
           DocumentReference docRef = FirebaseFirestore.getInstance().collection("Users").document(user.getUid());
           docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
               @RequiresApi(api = Build.VERSION_CODES.O)
               @Override
               public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                   if (task.isSuccessful()) {
                       DocumentSnapshot document = task.getResult();
                       phone.setText(document.get("phone").toString());
                       ads.setText(document.get("offers").toString());
                       message.setText(document.get("messages").toString());
                       member.setText(document.get("number").toString());
                       long a = user.getMetadata().getCreationTimestamp();
                       Date date = new Date(a);
                       Format format = new SimpleDateFormat("MMM dd, yyyy");
                       memberSince.setText(format.format(date));
                       address.setText(document.get("address").toString().split(",")[0]);

                   } else {
                       Log.d(TAG, "get failed with ", task.getException());
                   }
               }
           });
       }
    }



    public void SetOnClickListeners() {

        profile_picture = findViewById(R.id.profile_picture);
        name = findViewById(R.id.profile_name);
        ads = findViewById(R.id.profile_ads);
        message = findViewById(R.id.profile_messages);
        member = findViewById(R.id.profile_member);
        email = findViewById(R.id.profile_email);
        phone = findViewById(R.id.profile_phone);
        address = findViewById(R.id.profile_address);
        memberSince = findViewById(R.id.profile_birthday);
        password = findViewById(R.id.profile_password);
        logout =  findViewById(R.id.profile_logout);

        back = findViewById(R.id.profile_back);

        changeEmail = findViewById(R.id.profile_changeEmail);
        changePhone = findViewById(R.id.profile_changePhone);
        changeAddress = findViewById(R.id.profile_changeAddress);
        changePassword = findViewById(R.id.profile_changePassword);

        mStorageRef = FirebaseStorage.getInstance().getReference("UserImage");

        user = FirebaseAuth.getInstance().getCurrentUser();

        Picasso.get().load(user.getPhotoUrl()).resize(600, 600) // resizes the image to these dimensions (in pixel)
                .centerCrop().into(profile_picture);
        email.setText(user.getEmail());
        password.setText("ABCDEFGHIJKLMN");
        name.setText(user.getDisplayName());

        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(profile.this, Login_Page.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        changeEmail.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog_profile dp = new dialog_profile(CHANGE_EMAIL_TITLE);
                dp.show(getSupportFragmentManager(),"example Dialog");
            }
        });

        changePhone.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog_profile dp = new dialog_profile(CHANGE_PHONE_TITLE);
                dp.show(getSupportFragmentManager(),"example Dialog");
            }
        });
        changeAddress.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS,Place.Field.LAT_LNG,Place.Field.NAME);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,fieldList).build(getApplicationContext());
                startActivityForResult(intent,100);
            }
        });
        changePassword.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });

        profile_picture.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openFileChooser();
            }
        });
    }



    private void openFileChooser() {
                 new AlertDialog.Builder(this).setTitle("Change Picture")
                         .setMessage("Do you really want to change your profile picture?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(intent,PICK_IMAGE_REQUEST);
                    }
                })
                .setNegativeButton(android.R.string.no, null).setIcon(android.R.drawable.ic_dialog_info).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            mImageUri = data.getData();
            UploadFile();
        }

        if(requestCode == 100 &&  resultCode == RESULT_OK){
            Place place = Autocomplete.getPlaceFromIntent(data);
            address.setText(place.getName());
            applyTexts(place.getAddress()+";"+place.getLatLng(),CHANGE_ADDRESS_TITLE);

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
        if(mImageUri != null){
            StorageReference fileRef = mStorageRef.child(user.getUid()+"_profilePicture."+GetFileExtension(mImageUri));
            fileRef.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    String a = task.getResult().toString();
                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setPhotoUri(Uri.parse(a)).build();
                                    user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "User profile updated.");
                                                Picasso.get().load(mImageUri).resize(600, 600) // resizes the image to these dimensions (in pixel)
                                                        .centerCrop().into(profile_picture);
                                            }
                                        }
                                    });
                                }
                            });


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(profile.this, "Failure!", Toast.LENGTH_SHORT).show();
                        }
                    });

        }
        else {
            Toast.makeText(this, "Image upload failure!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void applyTexts(final String newText, String title) {
        if(title.equals(CHANGE_EMAIL_TITLE)){
            user.updateEmail("user@example.com").addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    email.setText(newText);
                }
            });
        }
        if(title.equals(CHANGE_ADDRESS_TITLE)){
            HashMap<String,Object> data = new HashMap<>();
            data.put("address", newText);
            CollectionReference colDb = FirebaseFirestore.getInstance().collection("Users");
            colDb.document(user.getUid()).set(data,SetOptions.merge());
        }
        if(title.equals(CHANGE_PHONE_TITLE)){
            HashMap<String,Object> data = new HashMap<>();
            data.put("phone", newText);
            CollectionReference colDb = FirebaseFirestore.getInstance().collection("Users");
            colDb.document(user.getUid()).set(data,SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    phone.setText(newText);
                }
            });
        }
    }

}