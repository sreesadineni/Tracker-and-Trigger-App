package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UpdateGrocery extends AppCompatActivity {
    private EditText editTextItem;
    private EditText editTextDesc;
    private NumberPicker numberPickerPreQuantity;
    private NumberPicker numberPickerBuyQuantity;
    private final FirebaseFirestore db=FirebaseFirestore.getInstance();
    private DocumentReference documentReference;
    private Task<DocumentSnapshot> documentSnapshot;
    private  DocumentSnapshot document;
    private  Grocery grocery;
    /*FirebaseAuth fAuth;
    FirebaseUser user;
*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        setTitle("Update Grocery");
        setContentView(R.layout.activity_update_grocery);
        editTextItem=findViewById(R.id.newGroceryItem);
        editTextDesc=findViewById(R.id.newGroceryDesc);
        numberPickerPreQuantity=findViewById(R.id.newPreQuantity);
        numberPickerBuyQuantity=findViewById(R.id.newBuyQuantity);
        numberPickerPreQuantity.setMinValue(0);
        numberPickerPreQuantity.setMaxValue(100);
        numberPickerBuyQuantity.setMinValue(0);
        numberPickerBuyQuantity.setMaxValue(100);
        Intent intent = getIntent();
        String path = intent.getStringExtra("path");
        documentReference=db.document(path);
        documentSnapshot=documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    document=documentSnapshot.getResult();
                    grocery=document.toObject(Grocery.class);
                    editTextItem.setText(grocery.getGrocery_Item());
                    editTextDesc.setText(grocery.getGrocery_desc());
                    numberPickerPreQuantity.setValue(grocery.getPresent_Quantity());
                    numberPickerBuyQuantity.setValue(grocery.getBuy_Quantity());

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
                updateGrocery();
                return  true;
            default: return super.onOptionsItemSelected(item);
        }

    }
    private void updateGrocery(){
        String item = editTextItem.getText().toString();
        String description =editTextDesc.getText().toString();
        int preQuantity = numberPickerPreQuantity.getValue();
        int buyQuantity = numberPickerBuyQuantity.getValue();
        if (item.trim().isEmpty()) {
            Toast.makeText(this, "Please insert a Item Name.", Toast.LENGTH_SHORT).show();
            return;
        }
        documentReference.update("grocery_Item",item,"grocery_desc",description,"present_Quantity",preQuantity
                ,"buy_Quantity",buyQuantity);
        Toast.makeText(this, "Grocery Updated", Toast.LENGTH_SHORT).show();
        finish();

    }


}
