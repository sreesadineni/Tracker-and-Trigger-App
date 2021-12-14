package com.example.myapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import static androidx.core.content.ContextCompat.getSystemService;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MeetingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MeetingsFragment extends Fragment {

    public static final String CHANNEL_ID = "Meetings";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private CollectionReference meetingsRef = db.collection("users")
            .document(firebaseAuth.getCurrentUser().getUid()).collection("Meetings");

    private MeetingAdapter meetingAdapter;

    RecyclerView meet_rclView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    FloatingActionButton newMeeting;

    public MeetingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MeetingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MeetingsFragment newInstance(String param1, String param2) {
        MeetingsFragment fragment = new MeetingsFragment();
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
        View view =  inflater.inflate(R.layout.fragment_meetings, container, false);
        newMeeting = (FloatingActionButton) view.findViewById(R.id.newMeet);
        newMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), NewMeetActivity.class));
            }
        });

        meet_rclView = (RecyclerView) view.findViewById(R.id.meet_rclView);
        setUpRecyclerView();

        return view;
    }



    private void setUpRecyclerView() {

        Query query = meetingsRef.orderBy("meetTimeStamp", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Meeting> options = new FirestoreRecyclerOptions.Builder<Meeting>()
                .setQuery(query, Meeting.class).build();
        meetingAdapter = new MeetingAdapter(options);

        meet_rclView.setHasFixedSize(true);
        meet_rclView.setLayoutManager(new LinearLayoutManager(getContext()));
        meet_rclView.setAdapter(meetingAdapter);

        meetingAdapter.setOnMeetClickListener(new MeetingAdapter.OnMeetClickListener() {
            @Override
            public void OnMeetClick(DocumentSnapshot documentSnapshot, int position) {}

            @Override
            public void OnImageClick(DocumentSnapshot documentSnapshot, int position) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                Meeting tempMeet = documentSnapshot.toObject(Meeting.class);
                String title = tempMeet.getTitle();
                String agenda = tempMeet.getAgenda();
                String docs = tempMeet.getDocs();
                String date = tempMeet.DateToString();
                String time = tempMeet.TimeToString();
                String share = "Meeting: " + title + " addressing " + agenda + " scheduled on "
                        + date + " at " + time + " hrs.\n" + "Documents required are: " + docs;
                intent.putExtra(Intent.EXTRA_TEXT, share);
                startActivity(Intent.createChooser(intent, "Share using: "));
            }

        });
    }

    @Override
    public void onStart() {
        super.onStart();
        meetingAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        meetingAdapter.stopListening();
    }
}