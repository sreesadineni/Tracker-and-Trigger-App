package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class TrackerActivity extends AppCompatActivity{
    private RecyclerView recyclerView;
    private TrackerAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker);
        ArrayList<TrackerCategory> exampleList = new ArrayList<>();
        exampleList.add(new TrackerCategory("Groceries",R.drawable.groceries));
        exampleList.add(new TrackerCategory("Books",R.drawable.books));
        exampleList.add(new TrackerCategory("Furniture",R.drawable.furniture));
        exampleList.add(new TrackerCategory("Medical Reports",R.drawable.medicalreport));
        exampleList.add(new TrackerCategory("Home Maintenance",R.drawable.homemaintenance));



        recyclerView = findViewById(R.id.recycler_view1);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        adapter = new TrackerAdapter(exampleList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new TrackerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                 Intent intent;
                switch (position) {
                    case 0: startActivity(new Intent(TrackerActivity.this,GroceryDisplay.class));
                            break;
                    case 1: startActivity(new Intent(TrackerActivity.this,BookDisplay.class));
                            break;
                    case 2: intent=new Intent(TrackerActivity.this,GeneralActivity.class);
                           intent.putExtra("category name","Furniture");
                           startActivity(intent);
                            break;
                    case 3: startActivity(new Intent(TrackerActivity.this,MedicalreportActivity.class));
                            break;
                    case 4: startActivity(new Intent(TrackerActivity.this,HomeMaintenanceDisplay.class));


                }
            }
        });

    }

}