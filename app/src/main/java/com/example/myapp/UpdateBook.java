package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UpdateBook extends AppCompatActivity {
    private EditText editTextBookName;
    private EditText editTextAuthorName;
    private  EditText editTextBookDesc;
    private Spinner spinnerBookStatus;
    FirebaseAuth fAuth=FirebaseAuth.getInstance();
    FirebaseUser user;
    String userId;
    private DocumentReference documentReference;
    private Task<DocumentSnapshot> documentSnapshot;
    private  DocumentSnapshot document;
    private  Book book;
    private final FirebaseFirestore db=FirebaseFirestore.getInstance();
    private  String status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        setTitle("Update Book");
        setContentView(R.layout.activity_update_book);
        editTextBookName=findViewById(R.id.updateBookName);
        editTextAuthorName=findViewById(R.id.updateAuthorName);
        editTextBookDesc=findViewById(R.id.updateExtra);
        spinnerBookStatus=findViewById(R.id.updateBookStatus);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.BookStatus, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBookStatus.setAdapter(arrayAdapter);
        Intent intent = getIntent();
        String path = intent.getStringExtra("path");
        documentReference=db.document(path);
        documentSnapshot=documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    document=documentSnapshot.getResult();
                    book=document.toObject(Book.class);
                    editTextBookName.setText(book.getBookName());
                    editTextAuthorName.setText(book.getAuthorName());
                    editTextBookDesc.setText(book.getBookDesc());
                    if (book.getReadingStatus() != null) {
                        int spinnerPosition = arrayAdapter.getPosition(book.getReadingStatus());
                        spinnerBookStatus.setSelection(spinnerPosition);
                    }


                }
                else{
                    Exception exception = task.getException();
                    System.out.println(exception);
                }

            }
        });
        spinnerBookStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                status=parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
                updateBook();
                return  true;
            default: return super.onOptionsItemSelected(item);
        }

    }
    private void updateBook(){
        String bookName = editTextBookName.getText().toString();
        String authorName =editTextAuthorName.getText().toString();
        String bookDesc=editTextBookDesc.getText().toString();


        if (bookName.trim().isEmpty()) {
            Toast.makeText(this, "Please insert a Book Name.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (authorName.trim().isEmpty()){
            Toast.makeText(this, "Please insert an Author Name.", Toast.LENGTH_SHORT).show();
            return;
        }
        documentReference.update("bookName",bookName,"authorName",authorName,"bookDesc",bookDesc
                ,"readingStatus",status);
        Toast.makeText(this, "Book Updated", Toast.LENGTH_SHORT).show();
        finish();

    }
}