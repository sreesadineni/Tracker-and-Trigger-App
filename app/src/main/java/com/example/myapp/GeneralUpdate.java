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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class GeneralUpdate extends AppCompatActivity {
    private EditText editTextItem;
    private EditText editTextDesc;
    private  EditText editTextExtra;
    FirebaseAuth fAuth=FirebaseAuth.getInstance();
    FirebaseUser user;
    String userId;
    private DocumentReference documentReference;
    private Task<DocumentSnapshot> documentSnapshot;
    private  DocumentSnapshot document;
    private  General general;
    private final FirebaseFirestore db=FirebaseFirestore.getInstance();
   private String category;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_update);
        editTextItem=findViewById(R.id.updateItem);
        editTextDesc=findViewById(R.id.updateDesc);
        editTextExtra=findViewById(R.id.updateExtra);
        Intent intent = getIntent();
        String path = intent.getStringExtra("path");
        category=intent.getStringExtra("category name");
        setTitle(category);
        documentSnapshot=documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    document=documentSnapshot.getResult();
                    general=document.toObject(General.class);
                    editTextItem.setText(general.getTitle());
                    editTextDesc.setText(general.getDesc());
                    editTextExtra.setText(general.getExtra());


                }
                else{
                    Exception exception = task.getException();
                    System.out.println(exception);
                }

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.new_grocery_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.save_grocery:
                updateGeneral();
                return  true;
            default: return super.onOptionsItemSelected(item);
        }

    }

    private void updateGeneral() {
        String item = editTextItem.getText().toString();
        String description =editTextDesc.getText().toString();
        String extra= editTextExtra.getText().toString();
        if (item.trim().isEmpty()) {
            Toast.makeText(this, "Please insert a Item Name.", Toast.LENGTH_SHORT).show();
            return;
        }
        documentReference.update("title",item,"desc",description,"extra",extra);
        Toast.makeText(this, String.valueOf(category)+" Updated", Toast.LENGTH_SHORT).show();
        finish();
    }
}