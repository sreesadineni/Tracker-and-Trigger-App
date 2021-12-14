package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class PhoneAuth extends AppCompatActivity {
    FirebaseAuth fAuth;
    String phoneNumber = "+917451221244";
    String otpCode = "123456";
    String verificationId;
    EditText phone,optEnter;
    Button next;
    String phoneNum;
  //  CountryCodePicker countryCodePicker;
    PhoneAuthCredential credential;
    Boolean verificationOnProgress = false, check = false;
    ProgressBar progressBar;
    TextView state,resend;
    PhoneAuthProvider.ForceResendingToken token;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth);

        phone = findViewById(R.id.PhoneNo);
        optEnter = findViewById(R.id.codeEnter);
        //countryCodePicker = findViewById(R.id.ccp);
        next = findViewById(R.id.nextBtn);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBar);
        state = findViewById(R.id.state);
        //resend = findViewById(R.id.resendOtpBtn);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!phone.getText().toString().isEmpty() && phone.getText().toString().length() == 10) {

                    if(!verificationOnProgress){
                        next.setEnabled(false);
                        progressBar.setVisibility(View.VISIBLE);
                        state.setVisibility(View.VISIBLE);
                        phoneNum = "+91"+ phone.getText().toString();
                        Log.d("phone", "Phone No.: " + phoneNum);
                        requestPhoneAuth(phoneNum);
                    }else {
                        next.setEnabled(false);
                        optEnter.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                        state.setText("Logging in");
                        state.setVisibility(View.VISIBLE);
                        otpCode = optEnter.getText().toString();
                        if(otpCode.isEmpty()){
                            optEnter.setError("Required");
                            return;
                        }

                        credential = PhoneAuthProvider.getCredential(verificationId,otpCode);
                        verifyAuth(credential, phoneNum);
                    }

                }else {
                    phone.setError("Valid Phone Required");
                }
            }
        });


    }

    private void requestPhoneAuth(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber,60L, TimeUnit.SECONDS,this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){

                    @Override
                    public void onCodeAutoRetrievalTimeOut(String s) {
                        super.onCodeAutoRetrievalTimeOut(s);
                        Toast.makeText(PhoneAuth.this, "OTP Timeout, Please Re-generate the OTP Again.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),PhoneAuth.class));
                        resend.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        verificationId = s;
                        token = forceResendingToken;
                        verificationOnProgress = true;
                        progressBar.setVisibility(View.GONE);
                        state.setVisibility(View.GONE);
                        next.setText("Verify");
                        next.setEnabled(true);
                        optEnter.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                        // called if otp is automatically detected by the app
                        verifyAuth(phoneAuthCredential, phoneNum);

                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        Toast.makeText(PhoneAuth.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void verifyAuth(PhoneAuthCredential credential, String phoneNum) {
        fAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(PhoneAuth.this, "Phone Verified."+fAuth.getCurrentUser().getUid(), Toast.LENGTH_SHORT).show();
                    checkUserProfile();

                }else {
                    progressBar.setVisibility(View.GONE);
                    state.setVisibility(View.GONE);
                    Toast.makeText(PhoneAuth.this, "Can not Verify phone and Create Account.", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(PhoneAuth.this, "Profile Exists", Toast.LENGTH_SHORT).show();
                    phone.setError("number already exists");
                    startActivity(new Intent(getApplicationContext(),PhoneAuth.class));

                }else {
                    Toast.makeText(PhoneAuth.this, "Profile Do not Exists.", Toast.LENGTH_SHORT).show();
                    check = true;
                    DocumentReference docRef = fStore.collection("phone").document(fAuth.getCurrentUser().getUid());
                    Map<String,Object> user = new HashMap<>();
                    user.put("PhoneNum",phoneNum);

                    //add user to database
                    docRef.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(PhoneAuth.this, "Profile Created", Toast.LENGTH_SHORT).show();
                            Log.d("zxcvbnm", "onSuccess: User Profile Created." + fAuth.getCurrentUser().getUid());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("zxcvbnm", "onFailure: Failed to Create User " + e.toString());
                        }
                    });
                    startActivity(new Intent(getApplicationContext(),Register.class));
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PhoneAuth.this, "Profile Do Not Exists", Toast.LENGTH_SHORT).show();
            }
        });
    }
}