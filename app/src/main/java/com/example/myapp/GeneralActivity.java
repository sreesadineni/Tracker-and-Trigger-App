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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class GeneralActivity extends AppCompatActivity {
    private final FirebaseFirestore db=FirebaseFirestore.getInstance();

    private  GeneralAdapter adapter;
    RecyclerView recyclerView;
    SearchView searchView;
    Intent intent;
    FirebaseAuth fAuth=FirebaseAuth.getInstance();
    FirebaseUser user;
    String userId;
    private String category;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user= fAuth.getCurrentUser();
        userId=user.getUid();
        intent=getIntent();
        category= intent.getStringExtra("category name");
        setTitle(category);
        setContentView(R.layout.activity_general);
        FloatingActionButton buttonAddGeneral= findViewById(R.id.button_add_general);
        buttonAddGeneral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(GeneralActivity.this, GeneralNew.class);
                intent.putExtra("category name",category);
                startActivity(intent);
            }
        });
        recyclerView=findViewById(R.id.recycler_view1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        setUpRecyclerView();

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                adapter.deleteGeneral(viewHolder.getAdapterPosition());
                Toast.makeText(GeneralActivity.this, String.valueOf(category)+" Deleted", Toast.LENGTH_SHORT).show();

            }
        }).attachToRecyclerView(recyclerView);

    }
    private void setUpRecyclerView() {
        Query query = db.collection("users").document(userId).collection(String.valueOf(category)).orderBy("title");
        FirestoreRecyclerOptions<General> options = new FirestoreRecyclerOptions.Builder<General>().
                setQuery(query, General.class).build();
        adapter = new GeneralAdapter(options);
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        adapter.notifyDataSetChanged();
        adapter.setOnItemClickListener(new GeneralAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

                String path = documentSnapshot.getReference().getPath();
                Intent intent = new Intent(GeneralActivity.this,GeneralUpdate.class);
                intent.putExtra("path", path);
                intent.putExtra("category name",category);
                startActivity(intent);
            }

            @Override
            public void onImageClick(DocumentSnapshot documentSnapshot, int position) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String item = documentSnapshot.toObject(General.class).getTitle();
                String desc = documentSnapshot.toObject(General.class).getDesc();
                String extra = documentSnapshot.toObject(General.class).getExtra();

                String share = "Item:" + String.valueOf(item) + "\n" + String.valueOf(desc) + "\n" + "Item:" + String.valueOf(extra);
                intent.putExtra(Intent.EXTRA_TEXT, share);
                startActivity(Intent.createChooser(intent, "Share Using:"));

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
        Query query=db.collection("users").document(userId).collection(String.valueOf(category)).whereEqualTo("titleLower",newText.toLowerCase()).orderBy("titleLower");
        FirestoreRecyclerOptions<General> options = new FirestoreRecyclerOptions.Builder<General>().
                setQuery(query, General.class).build();
        adapter = new GeneralAdapter(options);
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        adapter.notifyDataSetChanged();
        adapter.setOnItemClickListener(new GeneralAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

                String path=documentSnapshot.getReference().getPath();
                Intent intent= new Intent(GeneralActivity.this, GeneralUpdate.class);
                intent.putExtra("path",path);
                intent.putExtra("category name",category);
                startActivity(intent);
            }

            @Override
            public void onImageClick(DocumentSnapshot documentSnapshot, int position) {
                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String item = documentSnapshot.toObject(General.class).getTitle();
                String desc = documentSnapshot.toObject(General.class).getDesc();
                String extra = documentSnapshot.toObject(General.class).getExtra();

                String share = "Item:" + String.valueOf(item) + "\n" + String.valueOf(desc) + "\n" + "Item:" + String.valueOf(extra);
                intent.putExtra(Intent.EXTRA_TEXT, share);
                startActivity(Intent.createChooser(intent, "Share Using:"));
            }
        });



    }
    @Override
    protected void onStop() {
        super.onStop();
        adapter.startListening();
    }

}