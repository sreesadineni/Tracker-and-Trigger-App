package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;

import com.google.firebase.auth.FirebaseAuth;


public class Login extends AppCompatActivity {
    private final static int RC_SIGN_IN = 123;
    private static final String TAG = "fb";
    EditText mEmail, mPassword;
    Button mLoginBtn, SignInBtn, mLoginFbBtn;
    TextView mCreateBtn, mForgotBtn;
    ProgressBar progressBar;
    FirebaseAuth fAuth;
    GoogleSignInOptions gso;
    GoogleSignInClient signInClient;
    LoginButton loginButton;
    private CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = findViewById(R.id.Email);
        mPassword = findViewById(R.id.Password);
        mLoginBtn = findViewById(R.id.loginBtn);
        mCreateBtn = findViewById(R.id.textView3);
        mForgotBtn = findViewById(R.id.textView4);
        progressBar = findViewById(R.id.progressBar);
        fAuth = FirebaseAuth.getInstance();
        //SignInBtn = findViewById(R.id.SignInBtn);
       // mLoginFbBtn = findViewById(R.id.LoginFBBtn);


        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Email is Required");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    mPassword.setError("Password is Required");
                    return;
                }

                if (password.length() < 6) {
                    mPassword.setError("Password must be greater than Six Characters");
                    return;
                }
                //Authenticating the user
                progressBar.setVisibility(View.VISIBLE);
//
                //Authenticating the user
                fAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(Login.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Login.this, "Error ! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        });

        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Phone.class));
            }
        });

        mForgotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText resetMail = new EditText((v.getContext()));
                AlertDialog.Builder passwordRestDialog = new AlertDialog.Builder(v.getContext());
                passwordRestDialog.setTitle("Reset Password?");
                passwordRestDialog.setMessage("Enter your Email to Recieve Rest Link. ");
                passwordRestDialog.setView(resetMail);

                passwordRestDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Extract the email and send the link
                        String mail = resetMail.getText().toString();
                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Login.this, "Reset Link is sent to your Mail", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Login.this, "Error! Link is not Sent" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                passwordRestDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Close the Dialog
                    }
                });

                passwordRestDialog.create().show();

            }
        });
//
////        //Google Sign In
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestEmail()
//                .build();
//
//        signInClient = GoogleSignIn.getClient(this, gso);
//
//        //Checking whether is user is already logged in into his google account
//        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
//        if(account != null || fAuth.getCurrentUser() != null){
//            Toast.makeText(Login.this, "User is Logged in already", Toast.LENGTH_SHORT).show();
//            startActivity(new Intent(getApplicationContext(),MainActivity.class));
//        }
//
//        SignInBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent signInIntent = signInClient.getSignInIntent();
//                startActivityForResult(signInIntent, RC_SIGN_IN);
//            }
//        });
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        mCallbackManager.onActivityResult(requestCode, resultCode, data);
//        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
//        if (requestCode == RC_SIGN_IN){
//
//            // The Task returned from this call is always completed, no need to attach
//            // a listener.
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//
//            try {
//                GoogleSignInAccount account = task.getResult(ApiException.class);
//                //Creating Credentials in Firebase
//                AuthCredential authCredential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
//                fAuth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        Toast.makeText(Login.this, "Your Google Account is Connected", Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//
//                    }
//                });
//
//            }
//             catch (ApiException e){
//                e.printStackTrace();
//            }
//        }
    }
    }
