package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class GroceryDisplay extends AppCompatActivity {

    private final FirebaseFirestore db=FirebaseFirestore.getInstance();
   // private final CollectionReference groceryRef=db.collection("Grocery");
    private  GroceryAdapter adapter;
    RecyclerView recyclerView;
    SearchView searchView;
    Intent intent;
    FirebaseAuth fAuth=FirebaseAuth.getInstance();
    FirebaseUser user;
    String userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Grocery");
         user= fAuth.getCurrentUser();
         userid=user.getUid();
         setContentView(R.layout.activity_grocery_display);

        FloatingActionButton buttonAddGrocery = findViewById(R.id.button_add_grocery);
        buttonAddGrocery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GroceryDisplay.this, NewGroceryActivity.class));
            }
        });
        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //intent=getIntent();
        //user=  intent.getStringExtra("user_id");
        setUpRecyclerView();
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                adapter.deleteGrocery(viewHolder.getAdapterPosition());
                Toast.makeText(GroceryDisplay.this, "Grocery Deleted", Toast.LENGTH_SHORT).show();

            }
        }).attachToRecyclerView(recyclerView);


    }

    private void setUpRecyclerView() {
        Query query =db.collection("users").document(userid).collection("Grocery").orderBy("grocery_Item");
        FirestoreRecyclerOptions<Grocery> options= new FirestoreRecyclerOptions.Builder<Grocery>().
                setQuery(query,Grocery.class).build();
        adapter = new GroceryAdapter(options);
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        adapter.notifyDataSetChanged();
        adapter.setOnItemClickListener(new GroceryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

                String path=documentSnapshot.getReference().getPath();
                Intent intent= new Intent(GroceryDisplay.this, UpdateGrocery.class);
                intent.putExtra("path",path);
                startActivity(intent);
            }

            @Override
            public void onImageClick(DocumentSnapshot documentSnapshot, int position) {
                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String item=documentSnapshot.toObject(Grocery.class).getGrocery_Item();
                String desc=documentSnapshot.toObject(Grocery.class).getGrocery_desc();
                int preQuantity=documentSnapshot.toObject(Grocery.class).getPresent_Quantity();
                int buyQuantity=documentSnapshot.toObject(Grocery.class).getBuy_Quantity();
                String share= "Item:" + String.valueOf(item) + "\t" + String.valueOf(desc)+"\n"+"Remaining Quantity:" + preQuantity + "\n"
                        + "Buy Quantity:" + buyQuantity;
                intent.putExtra(Intent.EXTRA_TEXT,share);
                startActivity(Intent.createChooser(intent,"Share Using:"));

            }
        });




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_grocery_menu,menu);
        MenuItem item= menu.findItem(R.id.search_grocery);
        item.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return  true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                setUpRecyclerView();
                return true;
            }
        });
        searchView= (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }

        });
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    private void search(String newText) {
        Query query=db.collection("users").document(userid).collection("Grocery").whereEqualTo("itemIgnoreCase",newText.toLowerCase()).orderBy("grocery_desc");
        FirestoreRecyclerOptions <Grocery>options=new FirestoreRecyclerOptions.Builder<Grocery>().setQuery(query,Grocery.class).build();
        adapter=new GroceryAdapter(options);
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        adapter.notifyDataSetChanged();
        adapter.setOnItemClickListener(new GroceryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

                String path=documentSnapshot.getReference().getPath();
                Intent intent= new Intent(GroceryDisplay.this, UpdateGrocery.class);
                intent.putExtra("path",path);
                startActivity(intent);
            }

            @Override
            public void onImageClick(DocumentSnapshot documentSnapshot, int position) {
                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String item=documentSnapshot.toObject(Grocery.class).getGrocery_Item();
                String desc=documentSnapshot.toObject(Grocery.class).getGrocery_desc();
                int preQuantity=documentSnapshot.toObject(Grocery.class).getPresent_Quantity();
                int buyQuantity=documentSnapshot.toObject(Grocery.class).getBuy_Quantity();
                String share= "Item:" + String.valueOf(item) + "\t" + String.valueOf(desc)+"\n"+"Remaining Quantity:" + preQuantity + "\n"
                        + "Buy Quantity:" + buyQuantity;
                intent.putExtra(Intent.EXTRA_TEXT,share);
                startActivity(Intent.createChooser(intent,"Share Using:"));

            }
        });



    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.startListening();
    }
}