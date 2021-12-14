package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class NewListActivity extends AppCompatActivity {
    private EditText editTextListItem;
    private Button saveItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_list);

        editTextListItem = (EditText) findViewById(R.id.edit_text_list_item);
        saveItem = (Button) findViewById(R.id.save_list_item);

        saveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveListItem();
            }
        });
    }

    private void saveListItem() {
        String item = editTextListItem.getText().toString();
        if (item.trim().isEmpty()) {
            Toast.makeText(this, "Please insert list item", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        CollectionReference listRef = FirebaseFirestore.getInstance()
                .collection("users").document(fAuth.getCurrentUser().getUid()).collection("Lists");
        listRef.add(new List(item, false).getList()).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(NewListActivity.this, "firestore success", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(NewListActivity.this, "Error", Toast.LENGTH_SHORT).show();
                Toast.makeText(NewListActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        Toast.makeText(this, "List Item added", Toast.LENGTH_SHORT).show();
        finish();

    }
}