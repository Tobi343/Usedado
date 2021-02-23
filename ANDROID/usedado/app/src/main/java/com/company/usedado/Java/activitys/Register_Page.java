package com.company.usedado.Java.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.company.usedado.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Register_Page extends AppCompatActivity {

    private static final String TAG = "Register_Page";
    private static final int RC_SIGN_IN = 9001;

    private static Button SignUpButton;
    private static Button SignUpWithGoogleButton;
    private static Button SignUpWithFaceBookButton;
    private  static TextView SignUpText;
    private static TextView EmailText;
    private static TextView NameText;
    private static TextView PasswordText;
    private FirebaseAuth mAuth;
    public static int UserCount = 0;
    private GoogleSignInOptions gso;
    private GoogleSignInClient mGoogleSignInClient;


    /*
        Signup Button -> SignUp() //Creates Account
        SignUp():success -> GetAdminPanel() //Gets Data for UserCount
        GetAdminPanel():success -> setRegistration() //Set additional Data and place in the Firestore ->  finishProcess() opens Dashboard

     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register__page);

        // Configure Google Sign In
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        SetOnClickListeners();
    }

    public void SetOnClickListeners(){
        //Declare Buttons
        SignUpButton = findViewById(R.id.Register_SignInButton);
        SignUpWithFaceBookButton = findViewById(R.id.Register_SignInWithFacebook);
        SignUpWithGoogleButton = findViewById(R.id.Register_SignInWithGoogle);



        mAuth = FirebaseAuth.getInstance();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        //Declare TextViews
        SignUpText = findViewById(R.id.Register_NoAccount);

        //Declare EditTexts
        EmailText = findViewById(R.id.Register_editTextTextEmailAddress);
        PasswordText = findViewById(R.id.Register_editTextTextPassword);
        NameText = findViewById(R.id.Register_editTextName);

        //Set OnClickListeners for Buttons
        SignUpButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                    SignUp();
            }
        });

        SignUpWithFaceBookButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });

        SignUpWithGoogleButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        //Set OnClickListeners for TextViews
        SignUpText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(Register_Page.this, Login_Page.class));
            }
        });




    }
    @Override
    public void onBackPressed() {
        finishedProcess(new HashMap<>());
    }
    private void SignUp() {

        InputMethodManager imm = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(SignUpButton.getWindowToken(), 0);

        mAuth.createUserWithEmailAndPassword(EmailText.getText().toString(), PasswordText.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            Toast.makeText(Register_Page.this, "Regestration finished.", Toast.LENGTH_SHORT).show();
                            GetAdminPanel();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            try {
                                throw task.getException();
                            } catch(FirebaseAuthWeakPasswordException e) {
                                Toast.makeText(Register_Page.this, "Weak Password", Toast.LENGTH_SHORT).show();
                                PasswordText.requestFocus();
                            } catch(FirebaseAuthInvalidCredentialsException e) {
                                Toast.makeText(Register_Page.this, "Incorrect Mail", Toast.LENGTH_SHORT).show();
                                EmailText.requestFocus();
                            } catch(FirebaseAuthUserCollisionException e) {
                                Toast.makeText(Register_Page.this, "User already exists", Toast.LENGTH_SHORT).show();
                            } catch(Exception e) {
                                Log.e(TAG, e.getMessage());
                            }

                        }
                    }
                });




    }

    /**
     * Method to set the default settings into the new User profile
     *      @param user The new created FirebaseUser instance
    **/
    public void setRegistration (FirebaseUser user) {
       String name = "";
        for (UserInfo user1 : user.getProviderData()) {
            //if (user1.getProviderId().equals("google.com")){
            name = user1.getDisplayName();
            //}
        }
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(NameText.getText().toString().equals("")?name:NameText.getText().toString()).setPhotoUri(Uri.parse("https://firebasestorage.googleapis.com/v0/b/usedado.appspot.com/o/UserImage%2Fdeafult.jpeg?alt=media&token=70fee5c5-4cb3-4695-9778-8698a50c6c8c")).build();
        user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile updated.");
                        }
                    }
                });


        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();
                        HashMap<String,Object> data = new HashMap<>();
                        data.put("address", "unknown");
                        data.put("phone", "unknown");
                        data.put("ads", "0");
                        data.put("messages", "0");
                        data.put("offers", "0");
                        data.put("favourites", new ArrayList<String>());
                        data.put("number", "#"+ ++UserCount);
                        data.put("token",token);
                        data.put("profilePicture",  profileUpdates.getPhotoUri().toString());
                        for (UserInfo user1 : user.getProviderData()) {
                            //if (user1.getProviderId().equals("google.com")){
                            data.put("name",  user1.getDisplayName());
                            //}
                        }
                        if(!data.containsKey("name")||data.get("name")== null) {
                            data.put("name", profileUpdates.getDisplayName());
                        }



                        CollectionReference colDb = FirebaseFirestore.getInstance().collection("Users");
                        colDb.document(user.getUid()).set(data);

                        data = new HashMap<>();
                        data.put("NumberOfUsers", UserCount);
                        colDb = FirebaseFirestore.getInstance().collection("Users");
                        colDb.document("Adminpanel").set(data, SetOptions.merge());
                        Map<String,String> data1 = new HashMap<>();
                        data1.put( "USER_NAME",NameText.getText().toString());
                        finishedProcess(data1);
                    }
                });


    }

    public void GetAdminPanel(){

        DocumentReference docRef = FirebaseFirestore.getInstance().collection("Users").document("Adminpanel");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    UserCount = Integer.parseInt(document.get("NumberOfUsers").toString());
                    setRegistration(FirebaseAuth.getInstance().getCurrentUser());
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }


    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success
                            Log.d(TAG, "signInWithCredential:success");
                                    GetAdminPanel();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }

    //Method to finish the SignUp Process and go to Dashboard
    public void finishedProcess(Map<String,String> extraStrings){
        Intent intent = new Intent(Register_Page.this, Dashboard.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        for (Map.Entry<String, String> entry : extraStrings.entrySet()) {
            intent.putExtra(entry.getKey(), entry.getValue());
        }
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }

}