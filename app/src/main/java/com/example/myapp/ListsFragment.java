package com.example.myapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListsFragment extends Fragment {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference listRef = db.collection("users")
            .document(firebaseAuth.getCurrentUser().getUid()).collection("Lists");

    private ListAdapter listAdapter;

    RecyclerView list_rclView;
    FloatingActionButton newList;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ListsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListsFragment newInstance(String param1, String param2) {
        ListsFragment fragment = new ListsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lists, container, false);

        newList = (FloatingActionButton) view.findViewById(R.id.newList);
        newList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), NewListActivity.class));
            }
        });

        list_rclView = (RecyclerView) view.findViewById(R.id.lists_rclView);
        setUpRecyclerView();

        return view;
    }

    private void setUpRecyclerView() {
        Query query = listRef;
        FirestoreRecyclerOptions<List> options = new FirestoreRecyclerOptions.Builder<List>()
                .setQuery(query, List.class).build();
        listAdapter = new ListAdapter(options);

        list_rclView.setHasFixedSize(true);
        list_rclView.setLayoutManager(new LinearLayoutManager(getContext()));
        list_rclView.setAdapter(listAdapter);

    }

    @Override
    public void onStart() {
        super.onStart();
        listAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        listAdapter.stopListening();
    }
}