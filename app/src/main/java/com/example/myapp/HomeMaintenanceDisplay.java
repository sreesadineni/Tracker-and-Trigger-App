package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
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

public class HomeMaintenanceDisplay extends AppCompatActivity {
    private final FirebaseFirestore db=FirebaseFirestore.getInstance();
    private  MaintenanceAdapter adapter;
    RecyclerView recyclerView;
    SearchView searchView;
    Intent intent;
    FirebaseAuth fAuth=FirebaseAuth.getInstance();
    FirebaseUser user;
    String userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user= fAuth.getCurrentUser();
        userId=user.getUid();
        setTitle("Home Maintenance");
        setContentView(R.layout.activity_book_display);
        FloatingActionButton buttonAddBook = findViewById(R.id.button_add_book);
        buttonAddBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeMaintenanceDisplay.this, MaintenanceNew.class));
            }
        });
        recyclerView=findViewById(R.id.recycler_view);
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
                adapter.deleteBook(viewHolder.getAdapterPosition());
                Toast.makeText(HomeMaintenanceDisplay.this, "Activity Deleted", Toast.LENGTH_SHORT).show();

            }
        }).attachToRecyclerView(recyclerView);
    }


    private void setUpRecyclerView() {
        Query query =db.collection("users").document(userId).collection("Home Maintenance").orderBy("activity");
        FirestoreRecyclerOptions<HomeMaintenance> options= new FirestoreRecyclerOptions.Builder<HomeMaintenance>().
                setQuery(query,HomeMaintenance.class).build();
        adapter = new MaintenanceAdapter(options);
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        adapter.notifyDataSetChanged();
        adapter.setOnItemClickListener(new MaintenanceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

                String path=documentSnapshot.getReference().getPath();
                Intent intent= new Intent(HomeMaintenanceDisplay.this,MaintenanceUpdate .class);
                intent.putExtra("path",path);
                startActivity(intent);
            }

            @Override
            public void onImageClick(DocumentSnapshot documentSnapshot, int position) {
                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String activity=documentSnapshot.toObject(HomeMaintenance.class).getActivity();
                String phoneNumber=documentSnapshot.toObject(HomeMaintenance.class).getPhoneNo();
                String status=documentSnapshot.toObject(HomeMaintenance.class).getStatus();

                String share= String.valueOf(activity) + ":" +String.valueOf(phoneNumber);
                intent.putExtra(Intent.EXTRA_TEXT,share);
                startActivity(Intent.createChooser(intent,"Share Using:"));


            }

            @Override
            public void onCallClick(DocumentSnapshot documentSnapshot, int position) {
                String number=documentSnapshot.toObject(HomeMaintenance.class).getPhoneNo();
                Uri call = Uri.parse("tel:" + number);
                Intent intent=new Intent(Intent.ACTION_DIAL,call);
                startActivity(intent);
                Toast.makeText(HomeMaintenanceDisplay.this, "Making Call...", Toast.LENGTH_SHORT).show();
            }
        });


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_grocery_menu, menu);
        MenuItem item = menu.findItem(R.id.search_grocery);
        item.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
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
        Query query=db.collection("users").document(userId).collection("Home Maintenance").whereEqualTo("activitylower",newText.toLowerCase());
        FirestoreRecyclerOptions <HomeMaintenance>options=new FirestoreRecyclerOptions.Builder<HomeMaintenance>().setQuery(query,HomeMaintenance.class).build();
        adapter=new MaintenanceAdapter(options);
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        adapter.notifyDataSetChanged();
        adapter.setOnItemClickListener(new MaintenanceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

                String path=documentSnapshot.getReference().getPath();
                Intent intent= new Intent(HomeMaintenanceDisplay.this,MaintenanceUpdate.class );
                intent.putExtra("path",path);
                startActivity(intent);
            }

            @Override
            public void onImageClick(DocumentSnapshot documentSnapshot, int position) {
                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String activity=documentSnapshot.toObject(HomeMaintenance.class).getActivity();
                String phoneNumber=documentSnapshot.toObject(HomeMaintenance.class).getPhoneNo();
                String status=documentSnapshot.toObject(HomeMaintenance.class).getStatus();

                String share= String.valueOf(activity) + ":" +String.valueOf(phoneNumber);
                intent.putExtra(Intent.EXTRA_TEXT,share);
                startActivity(Intent.createChooser(intent,"Share Using:"));

                intent.putExtra(Intent.EXTRA_TEXT,share);
                startActivity(Intent.createChooser(intent,"Share Using:"));

            }

            @Override
            public void onCallClick(DocumentSnapshot documentSnapshot, int position) {

                String number=documentSnapshot.toObject(HomeMaintenance.class).getPhoneNo();
                Uri call = Uri.parse("tel:" + number);
                Intent intent=new Intent(Intent.ACTION_DIAL,call);
                startActivity(intent);
                Toast.makeText(HomeMaintenanceDisplay.this, "Making Call...", Toast.LENGTH_SHORT).show();
            }
        });



    }



    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}
