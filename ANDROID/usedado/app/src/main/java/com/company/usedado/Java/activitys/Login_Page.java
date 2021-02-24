package com.company.usedado.Java.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.company.usedado.Java.dialogs.dialog_profile;
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
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;


public class Login_Page extends AppCompatActivity implements dialog_profile.dialog_profile_Listener {

    private static final String TAG = "Login_Page";
    private static final int RC_SIGN_IN = 9001;
    private static Button SignInButton;
    private static Button SignInWithGoogleButton;
    private static Button SignInWithFaceBookButton;
    private  static TextView SignUpText;
    private static TextView ForgotPasswordText;
    private static TextView EmailText;
    private static TextView PasswordText;
    private GoogleSignInOptions gso;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__page);



        // Configure Google Sign In
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        SetOnClickListeners();
    }
// ...



        public void SetOnClickListeners(){
        //Declare Buttons
        SignInButton = findViewById(R.id.Login_SignInButton);
        SignInWithFaceBookButton = findViewById(R.id.Login_SignInWithFacebook);
        SignInWithGoogleButton = findViewById(R.id.Login_SignInWithGoogle);


        //Declare TextViews
        SignUpText = findViewById(R.id.Login_NoAccount);
        ForgotPasswordText = findViewById(R.id.Login_ForgotPassword);

        //Declare EditTexts
        EmailText = findViewById(R.id.Login_editTextTextEmailAddress);
        PasswordText = findViewById(R.id.Login_editTextTextPassword);


        //Set OnClickListeners for Buttons
        SignInButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mAuth.signInWithEmailAndPassword(EmailText.getText().toString(), PasswordText.getText().toString()).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, Log Message and finish this Process
                            Log.d(TAG, "signInWithEmail:success");


                            finishedProcess();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                        }
                    }
                });


            }
        });

        SignInWithFaceBookButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                
            }
        });

        SignInWithGoogleButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        //Set OnClickListeners for TextViews
        SignUpText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Login_Page.this, Register_Page.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        });

        ForgotPasswordText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog_profile dp = new dialog_profile("Enter your Email Address!");
                dp.show(getSupportFragmentManager(),"example Dialog");

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
                            FirebaseFirestore.getInstance().collection("Users").document(task.getResult().getUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                   String id = documentSnapshot.getId();
                                    finishedProcess();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Login_Page.this, "No Account found! Please try Register!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Login_Page.this, Register_Page.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }
                            }).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    Toast.makeText(Login_Page.this, task.getResult().getId(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }

    //Method to finish the SignUp Process and go to Dashboard
    public void finishedProcess(){
        Intent intent = new Intent(Login_Page.this, Dashboard.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Login_Page.this, Dashboard.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void applyTexts(final String newText, String title) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(newText)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Login_Page.this, "Check your Mails!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(Login_Page.this, "Failure!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    //TODO save user data to the Cache to reduce database calls!!

}