package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {
    RadioButton female;
    Button Logout;
    ImageView mPic;
    TextView mTextUser;
    FirebaseAuth fAuth;
    Button tracker, trigger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Logout = findViewById(R.id.logoutBtn);
        mPic = findViewById(R.id.Pic);
        mTextUser = findViewById(R.id.TextUser);
        fAuth = FirebaseAuth.getInstance();
        tracker=findViewById(R.id.button_tracker);
        trigger = findViewById(R.id.button_trigger);
        female=findViewById(R.id.FemaleButton);

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        FirebaseUser user = fAuth.getCurrentUser();
        updateUI(user);
        tracker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,TrackerActivity.class));
            }
        });
        trigger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Activity_ToDoList.class));
            }
        });
        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPic.setImageResource(R.drawable.female);
            }
        });

    }
    public void logout(View view){
        FirebaseAuth.getInstance().signOut();
        GoogleSignIn.getClient(this,new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN).build()).signOut().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Signout Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI(FirebaseUser user){
        if(user != null){
            mTextUser.setText(user.getEmail());
            if(user.getPhotoUrl() != null){
                String photoUrl = user.getPhotoUrl().toString();
                photoUrl = photoUrl + "?type=large";
                Picasso.get().load(photoUrl).into(mPic);
            }
        }
        else{
            mTextUser.setText(" ");
        }
    }
}