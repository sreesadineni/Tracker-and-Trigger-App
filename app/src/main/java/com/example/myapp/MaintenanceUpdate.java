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

public class MaintenanceUpdate extends AppCompatActivity {
    private EditText editTextActivity;
    private EditText editTextPhoneNo;
    private  EditText editTextStatus;
    FirebaseAuth fAuth=FirebaseAuth.getInstance();
    FirebaseUser user;
    String userId;
    private DocumentReference documentReference;
    private Task<DocumentSnapshot> documentSnapshot;
    private  DocumentSnapshot document;
    private  HomeMaintenance hm;
    private final FirebaseFirestore db=FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        setContentView(R.layout.activity_maintenance_update);
        setTitle("Update Home Maintenance Activity");
        editTextActivity=findViewById(R.id.updateActivity);
        editTextPhoneNo=findViewById(R.id.updatePhoneNo);
        editTextStatus=findViewById(R.id.updateStatus);
        Intent intent = getIntent();
        String path = intent.getStringExtra("path");
        documentReference=db.document(path);
        documentSnapshot=documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    document = documentSnapshot.getResult();
                    hm = document.toObject(HomeMaintenance.class);
                    editTextActivity.setText(hm.getActivity());
                    editTextPhoneNo.setText(hm.getPhoneNo());
                    editTextStatus.setText(hm.getStatus());

                } else {
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
                updateActivity();
                return  true;
            default: return super.onOptionsItemSelected(item);
        }

    }

    private void updateActivity() {
        String activity = editTextActivity.getText().toString();
        String phone =editTextPhoneNo.getText().toString();
        String status=editTextStatus.getText().toString();

        if (activity.trim().isEmpty()) {
            Toast.makeText(this, "Please insert a Home Maintenance Activity.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (status.trim().isEmpty()){
            Toast.makeText(this, "Please insert a valid status", Toast.LENGTH_SHORT).show();
            return;
        }
        documentReference.update("activity",activity,"phoneNo",phone,"status",status);
        Toast.makeText(this, "Grocery Updated", Toast.LENGTH_SHORT).show();
        finish();
    }
}