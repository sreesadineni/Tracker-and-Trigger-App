package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class MaintenanceNew extends AppCompatActivity {
    private EditText editTextActivity;
    private EditText editTextPhoneNo;
    private  EditText editTextStatus;
    FirebaseAuth fAuth=FirebaseAuth.getInstance();
    FirebaseUser user;
    String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance_new);
        user = fAuth.getCurrentUser();
        userId=user.getUid();
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        setTitle("Add Home Maintenance Activity");
        setContentView(R.layout.activity_maintenance_new);
        editTextActivity=findViewById(R.id.newActivity);
        editTextPhoneNo=findViewById(R.id.newPhoneNo);
        editTextStatus=findViewById(R.id.newStatus);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.new_grocery_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.save_grocery:
                saveActivity();
                return  true;
            default: return super.onOptionsItemSelected(item);
        }

    }

    private void saveActivity()  {
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
        CollectionReference maintenanceRef = FirebaseFirestore.getInstance().collection("users").document(userId)
                .collection("Home Maintenance");
        maintenanceRef.add(new HomeMaintenance(activity,phone,status));
        Toast.makeText(this, "Home Maintenance Activity added", Toast.LENGTH_SHORT).show();
        finish();
    }
}
