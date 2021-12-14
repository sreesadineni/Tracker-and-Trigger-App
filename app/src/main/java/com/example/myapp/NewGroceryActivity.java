package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class NewGroceryActivity extends AppCompatActivity {
    private EditText editTextItem;
    private EditText editTextDesc;
    private NumberPicker numberPickerPreQuantity;
    private NumberPicker numberPickerBuyQuantity;
    FirebaseAuth fAuth=FirebaseAuth.getInstance();
    FirebaseUser user;
    String userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = fAuth.getCurrentUser();
        userid=user.getUid();
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        setTitle("Add Grocery");
        setContentView(R.layout.activity_new_grocery);
        editTextItem=findViewById(R.id.newGroceryItem);
        editTextDesc=findViewById(R.id.newGroceryDesc);
        numberPickerPreQuantity=findViewById(R.id.newPreQuantity);
        numberPickerBuyQuantity=findViewById(R.id.newBuyQuantity);
        numberPickerPreQuantity.setMinValue(0);
        numberPickerPreQuantity.setMaxValue(100);
        numberPickerBuyQuantity.setMinValue(0);
        numberPickerBuyQuantity.setMaxValue(100);
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
                saveGrocery();
                return  true;
            default: return super.onOptionsItemSelected(item);
        }

    }
    private void saveGrocery() {
        String item = editTextItem.getText().toString();
        String description =editTextDesc.getText().toString();
        int preQuantity = numberPickerPreQuantity.getValue();
        int buyQuantity = numberPickerBuyQuantity.getValue();
        if (item.trim().isEmpty()) {
            Toast.makeText(this, "Please insert a Item Name.", Toast.LENGTH_SHORT).show();
            return;
        }
        CollectionReference groceryRef = FirebaseFirestore.getInstance().collection("users").document(userid)
                .collection("Grocery");
        groceryRef.add(new Grocery(item, description, preQuantity,buyQuantity));
        Toast.makeText(this, "Grocery added", Toast.LENGTH_SHORT).show();
        finish();
    }
}