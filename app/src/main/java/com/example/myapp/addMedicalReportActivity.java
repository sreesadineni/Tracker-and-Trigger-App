package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class addMedicalReportActivity extends AppCompatActivity {

    private ImageButton backBtn;
    private ImageView medicalreportIv;
    private EditText titleEt;
    private Button addProductsBtn;

    FirebaseUser user;
    String userid;

    private FirebaseAuth fAuth;
    private ProgressDialog progressDialog;

    private static final int CAMERA_REQUEST_CODE=200;
    private static final int STORAGE_REQUEST_CODE=300;

    private static final int IMAGE_PICK_GALLERY_CODE=400;
    private static final int IMAGE_PICK_CAMERA_CODE=500;

    private String[] cameraPermissions;
    private String[] storagePermissions;

    private Uri image_uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medical_report);

        backBtn = findViewById(R.id.backBtn);
        titleEt = findViewById(R.id.titleEt);
        medicalreportIv = findViewById(R.id.medicalreportIv);
        addProductsBtn = findViewById(R.id.addProductsBtn);

        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();
        userid=user.getUid();

        cameraPermissions=new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions=new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        medicalreportIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePickDialog();
            }
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        addProductsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputdata();
            }
        });

    }
    private String itemTitle;

    private void inputdata() {

        itemTitle = titleEt.getText().toString().trim();
        //Validate data
        if (TextUtils.isEmpty(itemTitle)) {
            Toast.makeText(this, "Title is required...", Toast.LENGTH_SHORT).show();
            return;
        }
        addProduct();
    }

    private void addProduct(){

        progressDialog.setMessage("Adding Items...");
        progressDialog.show();

        String timestamp = "" + System.currentTimeMillis();

        if(image_uri==null){
            //without image
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("itemid", "" + timestamp);
            hashMap.put("itemTitle", "" + itemTitle);
            hashMap.put("timestamp", "" + timestamp);
            hashMap.put("medical_report","");

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users").document(userid)
                    .collection("Medical report").document(timestamp)
                    .set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d("information", "document has been saved");
                    progressDialog.dismiss();
                    Toast.makeText(addMedicalReportActivity.this, "Item added...", Toast.LENGTH_SHORT).show();
                    clearData();


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Log.d("information", "document has not been saved");
                    progressDialog.dismiss();
                    Toast.makeText(addMedicalReportActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });

        }
        else{

            String filePathAndName="medicalreport_images/"+""+timestamp;
            //String filePathAndName="product_images/"+""+timestamp;
            StorageReference storageReference= FirebaseStorage.getInstance().getReference(filePathAndName);
            storageReference.putFile(image_uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Task<Uri> uriTask= taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isSuccessful());
                            Uri downloadImageUri=uriTask.getResult();

                            if(uriTask.isSuccessful()){

                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("itemid", "" + timestamp);
                                hashMap.put("itemTitle", "" + itemTitle);
                                hashMap.put("timestamp", "" + timestamp);
                                hashMap.put("medical_report",""+downloadImageUri);

                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                db.collection("users").document(userid)
                                        .collection("Medical report").document(timestamp)
                                        .set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("information", "document has been saved");
                                        progressDialog.dismiss();
                                        Toast.makeText(addMedicalReportActivity.this, "Item added...", Toast.LENGTH_SHORT).show();
                                        clearData();


                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        Log.d("information", "document has not been saved");
                                        progressDialog.dismiss();
                                        Toast.makeText(addMedicalReportActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                });

                            }

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            progressDialog.dismiss();
                            Toast.makeText(addMedicalReportActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });


        }

    }
    public void clearData() {

        titleEt.setText("");
        medicalreportIv.setImageResource(R.drawable.ic_baseline_add_photo_alternate_24);
        image_uri=null;
    }

    private void showImagePickDialog(){

        String[] options={"Camera","Gallery"};

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Pick Image")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(which==0){

                            if(checkCameraPermission()){
                                pickFromCamera();
                            }
                            else {
                                requestCameraPermission();
                            }

                        }
                        else {

                            if(checkStoragePermission()){

                                pickFromGallery();

                            }
                            else {

                                requestStoragePermission();

                            }

                        }

                    }
                })
                .show();

    }

    private void pickFromGallery(){

        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,IMAGE_PICK_GALLERY_CODE);
    }

    private void pickFromCamera(){

        ContentValues contentValues= new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE,"Temp_Image_Title");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION,"Temp_Image_Description");

        image_uri=getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);
        Intent intent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
        startActivityForResult(intent,IMAGE_PICK_CAMERA_CODE);

    }

    private boolean checkStoragePermission(){

        boolean result= ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)== (PackageManager.PERMISSION_GRANTED);
        return result;

    }

    private void requestStoragePermission(){

        ActivityCompat.requestPermissions(this,storagePermissions,STORAGE_REQUEST_CODE);

    }
    private boolean checkCameraPermission(){

        boolean result= ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)== (PackageManager.PERMISSION_GRANTED);
        boolean result1= ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)== (PackageManager.PERMISSION_GRANTED);

        return result && result1;

    }

    private void requestCameraPermission(){

        ActivityCompat.requestPermissions(this,cameraPermissions,CAMERA_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){

            case CAMERA_REQUEST_CODE:{
                if(grantResults.length>0){

                    boolean cameraAccepted = grantResults[0]== PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1]== PackageManager.PERMISSION_GRANTED;
                    if(cameraAccepted && storageAccepted){
                        pickFromCamera();
                    }
                    else{
                        Toast.makeText(this,"Camera & Storage permissions are required...",Toast.LENGTH_SHORT).show();
                    }

                }

            }
            case STORAGE_REQUEST_CODE:{

                if(grantResults.length>0){
                    boolean storageAccepted= grantResults[0]==PackageManager.PERMISSION_GRANTED;
                    if(storageAccepted){
                        pickFromGallery();
                    }
                    else{
                        Toast.makeText(this,"Storage permissions are required...",Toast.LENGTH_SHORT).show();
                    }
                }

            }

        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(resultCode==RESULT_OK){

            if(requestCode==IMAGE_PICK_GALLERY_CODE){

                image_uri=data.getData();
                medicalreportIv.setImageURI(image_uri);
            }
            else if (requestCode== IMAGE_PICK_CAMERA_CODE) {

                medicalreportIv.setImageURI(image_uri);
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }






}