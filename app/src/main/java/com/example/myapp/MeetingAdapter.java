package com.example.myapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class MeetingAdapter extends FirestoreRecyclerAdapter<Meeting, MeetingAdapter.MeetingHolder> {

    OnMeetClickListener listener;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public MeetingAdapter(@NonNull FirestoreRecyclerOptions<Meeting> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MeetingHolder meetingHolder, int i, @NonNull Meeting meeting) {
        meetingHolder.meetTextViewTitle.setText(meeting.getTitle());
        //meetingHolder.meetDate.setText(meeting.getDate());
        meetingHolder.meetDate.setText(meeting.DateToString());
        meetingHolder.meetTextViewAgenda.setText(meeting.getAgenda());
        //meetingHolder.meetTime.setText(meeting.getTime());
        meetingHolder.meetTime.setText(meeting.TimeToString());
        meetingHolder.meetTextViewDocs.setText(meeting.getDocs());

    }

    public void deleteMeet(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    @NonNull
    @Override
    public MeetingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.meet_item, parent, false);
        return new MeetingHolder(v);
    }

    class MeetingHolder extends RecyclerView.ViewHolder {
        TextView meetTextViewTitle;
        TextView meetDate;
        TextView meetTextViewAgenda;
        TextView meetTime;
        TextView meetTextViewDocs;
        ImageView shareMeet;

        public MeetingHolder(@NonNull View itemView) {
            super(itemView);
            meetTextViewTitle = (TextView) itemView.findViewById(R.id.meet_text_view_title);
            meetDate = (TextView) itemView.findViewById(R.id.meet_date);
            meetTextViewAgenda = (TextView) itemView.findViewById(R.id.meet_text_view_agenda);
            meetTime = (TextView) itemView.findViewById(R.id.meet_time);
            meetTextViewDocs = (TextView) itemView.findViewById(R.id.meet_text_view_docs);
            shareMeet = (ImageView) itemView.findViewById(R.id.share_meet);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = getAdapterPosition();
                    deleteMeet(position);
                    return true;
                }
            });

            shareMeet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    listener.OnImageClick(getSnapshots().getSnapshot(position), position);
                }
            });
        }
    }

    public interface OnMeetClickListener {
        void OnMeetClick(DocumentSnapshot documentSnapshot, int position);
        void OnImageClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnMeetClickListener(OnMeetClickListener listener) {
        this.listener = listener;
    }
}
