package com.example.myapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MedicalreportActivity extends AppCompatActivity {
    private ImageButton addItemBtn;
    private EditText searchitemEt;
    private RecyclerView itemsRv;
    FirebaseUser user;
    String userid;
    private FirebaseAuth fAuth;
    private ProgressDialog progressDialog;
    private ArrayList<modelitem> medicalreportslist;
    private AdapterMedicalreport adapterMedicalreport;
    private String dashboard_category;
    private static final String TAG = "DocSnippets";
    @Override
    protected void onStart() {
        super.onStart();
        loadallitems();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicalreport);

        addItemBtn = findViewById(R.id.addItemBtn);
        searchitemEt = findViewById(R.id.searchitemEt);
        itemsRv = findViewById(R.id.itemsRv);
        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();
        userid=user.getUid();
        searchitemEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                try {
                    adapterMedicalreport.getFilter().filter(s);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });
        addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MedicalreportActivity.this, addMedicalReportActivity.class);
                startActivity(intent);
            }
        });
    }
    private void loadallitems() {
        medicalreportslist=new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(userid)
                .collection("Medical report")

                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error!=null){
                            Log.w(TAG,"listen failed",error);
                        }
                        medicalreportslist.clear();;

                        for (QueryDocumentSnapshot document : value) {
                            Log.d(TAG, document.getId() + " => " + document.getData());
                            modelitem modelitemTemp = document.toObject(modelitem.class);
                            medicalreportslist.add(modelitemTemp);
                        }
                        //setup adapter
                        adapterMedicalreport = new AdapterMedicalreport(MedicalreportActivity.this,medicalreportslist);
                        //set adapter
                        itemsRv.setAdapter(adapterMedicalreport);
                    }
                });
    }
}