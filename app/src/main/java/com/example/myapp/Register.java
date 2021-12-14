package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {
    private final static int RC_SIGN_IN = 123;
    private static final String TAG = "fb";
    EditText mFullName, mEmail, mPassword, mconfPassword, mPhone;
    Button mRegisterBtn, mSignUpBtn;
    TextView mloginpageBtn;
    ProgressBar progressBar;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID, PhoneNum, email, password, confPass, fullName;
    GoogleSignInOptions gso;
    //GoogleSignInClient signInClient;
    CallbackManager mCallbackManager;
    LoginButton loginButton;
    Boolean isDataValid = false;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mFullName = findViewById(R.id.PersonName);
        mPhone = findViewById(R.id.Phone);
        mEmail = findViewById(R.id.Email);
        mPassword = findViewById(R.id.Password);
        mconfPassword = findViewById(R.id.confPassword);
        mRegisterBtn = findViewById(R.id.registerBtn);
        mloginpageBtn = findViewById(R.id.textView3);
        progressBar = findViewById(R.id.progressBar);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        //mSignUpBtn = findViewById(R.id.SignUpBtn);


        Bundle bundle = getIntent().getExtras();
        PhoneNum = bundle.getString("Phone_key");

        //Checking whether is user is already logged in into his google account
        // GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        //  if(account != null || fAuth.getCurrentUser() != null){
        //     Toast.makeText(Register.this, "User is Logged in already", Toast.LENGTH_SHORT).show();
        //     startActivity(new Intent(getApplicationContext(), Phone.class));
        //}



        //Creating the User
        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = mEmail.getText().toString().trim();
                password = mPassword.getText().toString().trim();
                fullName = mFullName.getText().toString();
                confPass = mconfPassword.getText().toString().trim();

                //Validaing Input Data
                do {
                    count = 0;
                    validateData(mFullName);
                    validateData(mEmail);
                    validateData(mPassword);
                    validateData(mconfPassword);
                    isValidPassword(password);

                    if (!(password.equals(confPass))) {
                        isDataValid = false;
                        mconfPassword.setError("Password does not Match");
                    } else {
                        count++;
                    }

                    if (mFullName.length() > 15) {
                        isDataValid = false;
                        mFullName.setError("UserName cannot be greater than 15 characters.");
                    } else {
                        count++;
                    }

                    if (count == 7) {
                        isDataValid = true;
                    }

                } while (isDataValid = false);


                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Email is Required");
                    return;
                }

                // Registering the user in Firebase

                if (count == 7) {
                    progressBar.setVisibility(View.VISIBLE);

                    fAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {

                            //Verification Link
                            FirebaseUser fUser = fAuth.getCurrentUser();
                            fUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(Register.this, "Verification Email has been Sent", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Register.this, "Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                            Toast.makeText(Register.this, "User Account Created", Toast.LENGTH_SHORT).show();

                            //Storing data in firestore Database
                            userID = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fStore.collection("users").document(userID);
                            Map<String, Object> user = new HashMap<>();
                            user.put("FullName", fullName);
                            user.put("Email", email);
                            user.put("Phone", PhoneNum);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("TAG", "onSuccess: User Profile is Created" + userID);

                                }
                            });
                            startActivity(new Intent(Register.this, MainActivity.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Register.this, "Error ! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });

    }
    public void validateData(EditText field) {
        if (field.getText().toString().isEmpty()) {
            isDataValid = false;
            field.setError("Required Field.");
        } else {
            count++;
        }
    }

    public void isValidPassword(String Password) {

        // Regex to check valid password.
        String regex = "^(?=.*[0-9])"
                + "(?=.*[a-z])(?=.*[A-Z])"
                + "(?=.*[@#$%^&+=])"
                + "(?=\\S+$).{8,20}$";

        // Compile the ReGex
        Pattern p = Pattern.compile(regex);

        Matcher m = p.matcher(Password);
        if (m.matches() == true) {
            count++;
        } else {
            mPassword.setError("Password should contain at least 8 characters and at most 20 characters.\n" +
                    "It should at least one digit.\n" +
                    "It should contain at least one upper case alphabet.\n" +
                    "It should contain at least one lower case alphabet.\n" +
                    "It should contain at least one special character which includes !@#$%&*()-+=^.\n" +
                    "It should not contain any white space.");
        }

    }
}



