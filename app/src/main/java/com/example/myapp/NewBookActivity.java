package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class NewBookActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private EditText editTextBookName;
    private EditText editTextAuthorName;
    private  EditText editTextBookDesc;
    private Spinner spinnerBookStatus;
    FirebaseAuth fAuth=FirebaseAuth.getInstance();
    FirebaseUser user;
    String userId;
    private String text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_book);
        user = fAuth.getCurrentUser();
        userId=user.getUid();
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        setTitle("Add Book");
        setContentView(R.layout.activity_new_book);
        editTextBookName=findViewById(R.id.newBookName);
        editTextAuthorName=findViewById(R.id.newAuthorName);
        editTextBookDesc=findViewById(R.id.newBookDesc);
        spinnerBookStatus=findViewById(R.id.newBookStatus);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this,
                R.array.BookStatus, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBookStatus.setAdapter(arrayAdapter);
        spinnerBookStatus.setOnItemSelectedListener(NewBookActivity.this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
         text = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
                saveBook();
                return  true;
            default: return super.onOptionsItemSelected(item);
        }

    }
    private void saveBook() {
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
        CollectionReference bookRef = FirebaseFirestore.getInstance().collection("users").document(userId)
                .collection("Books");
        bookRef.add(new Book(bookName, authorName, bookDesc,text));
        Toast.makeText(this, "Book added", Toast.LENGTH_SHORT).show();
        finish();
    }
}