package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Phone extends AppCompatActivity {

    EditText mPhone, mCC, mOTP;
    String otpCode = "123456";
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    ProgressBar progressBar;
    String verificationID;
    Boolean verificationOnProgress = false, check = false;
    PhoneAuthProvider.ForceResendingToken token;
    PhoneAuthCredential credential;
    String PhoneNum, FullName, Email, userID;
    int count=0;
    MaterialTextView state,resend, mloginpageBtn;
    MaterialButton mNextBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        mPhone = findViewById(R.id.Phone);
        mCC = findViewById(R.id.CC);
        mOTP = findViewById(R.id.OTP);
        mNextBtn = findViewById(R.id.submitBtn);
        progressBar = findViewById(R.id.progressBar);
        fAuth = FirebaseAuth.getInstance();
        state = findViewById(R.id.state);
        fStore = FirebaseFirestore.getInstance();
        mloginpageBtn = findViewById(R.id.textView3);
        Intent intent = getIntent();
        Email = intent.getStringExtra("email_key");
        Intent intent1 = getIntent();
        FullName = intent1.getStringExtra("name_key");
        mloginpageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Login.class));
                finish();
            }
        });
        mNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(mPhone.getText().toString().isEmpty()) && mPhone.getText().toString().length() == 10) {
                    PhoneNum = "+91" + mPhone.getText().toString();
                        if (!verificationOnProgress) {
                            mNextBtn.setEnabled(false);
                            progressBar.setVisibility(View.VISIBLE);
                            state.setVisibility(View.VISIBLE);
                            Toast.makeText(Phone.this, "Sending OTP...", Toast.LENGTH_SHORT).show();
                            Log.d("phone", "Phone No.: " + PhoneNum);
                            requestPhoneAuth(PhoneNum);
                        } else {
                            mNextBtn.setEnabled(false);
                            mOTP.setVisibility(View.GONE);
                            progressBar.setVisibility(View.VISIBLE);
                            state.setText("Logging in");
                            state.setVisibility(View.VISIBLE);
                            otpCode = mOTP.getText().toString();
                            if (otpCode.isEmpty()) {
                                mOTP.setError("Required");
                                return;
                            }
                            credential = PhoneAuthProvider.getCredential(verificationID, otpCode);
                            verifyAuth(credential);
                    }
                }
                else{
                    Toast.makeText(Phone.this, "Phone number is not valid", Toast.LENGTH_SHORT).show();
                }

        }
    });
    }
    private void requestPhoneAuth(String phoneNum) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(PhoneNum,60L, TimeUnit.SECONDS,this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){

                    @Override
                    public void onCodeAutoRetrievalTimeOut(String s) {
                        super.onCodeAutoRetrievalTimeOut(s);
                        Toast.makeText(Phone.this, "OTP Timeout, Please Re-generate the OTP Again.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), Phone.class));
                    }

                    @Override
                    public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        verificationID = s;
                        token = forceResendingToken;
                        verificationOnProgress = true;
                        progressBar.setVisibility(View.GONE);
                        state.setVisibility(View.GONE);
                        mNextBtn.setText("Verify");
                        mNextBtn.setEnabled(true);
                        mOTP.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                        // called if otp is automatically detected by the app
                        verifyAuth(phoneAuthCredential);

                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        Toast.makeText(Phone.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void verifyAuth(PhoneAuthCredential credential) {
        fAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    checkUserProfile();

                }
            }
        });
    }
    private void checkUserProfile() {
        DocumentReference docRef = fStore.collection("phone").document(fAuth.getCurrentUser().getUid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    Toast.makeText(Phone.this, "Profile Exists", Toast.LENGTH_SHORT).show();
                    mPhone.setError("number already exists");
                    startActivity(new Intent(getApplicationContext(),Phone.class));
                    finish();
                }else {
                    Toast.makeText(Phone.this, "Profile Do not Exists.", Toast.LENGTH_SHORT).show();

                    check = true;
                    DocumentReference docRef = fStore.collection("phone").document(fAuth.getCurrentUser().getUid());
                    Map<String,Object> user = new HashMap<>();
                    user.put("PhoneNum",PhoneNum);

                    //add user to database
                    docRef.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(Phone.this, "Profile Created", Toast.LENGTH_SHORT).show();
                            Log.d("zxcvbnm", "onSuccess: User Profile Created." + fAuth.getCurrentUser().getUid());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("zxcvbnm", "onFailure: Failed to Create User " + e.toString());
                        }
                    });
                    Intent intent = new Intent(Phone.this, Register.class);
                    intent.putExtra("Phone_key", PhoneNum);
                    startActivity(intent);
                    startActivity(new Intent(getApplicationContext(),Register.class));
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Phone.this, "Profile failure"+ e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

