package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class GeneralNew extends AppCompatActivity {
    private EditText editTextItem;
    private EditText editTextDesc;
    private  EditText editTextExtra;
    FirebaseAuth fAuth=FirebaseAuth.getInstance();
    FirebaseUser user;
    String userId;
    Intent intent;
    private String category;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_new);
        user = fAuth.getCurrentUser();
        userId=user.getUid();
        intent=getIntent();
        category=intent.getStringExtra("category name");
        setTitle("New"+category);
        editTextItem=findViewById(R.id.newItem);
        editTextDesc=findViewById(R.id.newDesc);
        editTextExtra=findViewById(R.id.newExtra);


    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.new_grocery_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.save_grocery:
                saveGeneral();
                return  true;
            default: return super.onOptionsItemSelected(item);
        }

    }

    private void saveGeneral() {
        String item = editTextItem.getText().toString();
        String description =editTextDesc.getText().toString();
        String extra= editTextExtra.getText().toString();
        if (item.trim().isEmpty()) {
            Toast.makeText(this, "Please insert a Item Name.", Toast.LENGTH_SHORT).show();
            return;

        }
        CollectionReference generalRef = FirebaseFirestore.getInstance().collection("users").document(userId)
                .collection(String.valueOf(category));
        generalRef.add(new General(item, description, extra));
        Toast.makeText(this, String.valueOf(category)+" added", Toast.LENGTH_SHORT).show();
        finish();
    }
}