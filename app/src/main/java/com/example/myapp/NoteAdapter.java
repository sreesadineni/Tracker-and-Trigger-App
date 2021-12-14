package com.example.myapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class NoteAdapter extends FirestoreRecyclerAdapter<Note, NoteAdapter.NoteHolder> {

    private OnItemClickListener listener;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public NoteAdapter(@NonNull FirestoreRecyclerOptions<Note> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteHolder noteHolder, int i, @NonNull Note note) {
        noteHolder.noteTextViewTitle.setText(note.getTitle());
        noteHolder.noteTextViewDesc.setText(note.getDesc());
        noteHolder.noteTextViewPriority.setText(String.valueOf(note.getPriority()));

    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);
        return new NoteHolder(v);
    }

    public void deleteItem (int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    class NoteHolder extends RecyclerView.ViewHolder {
        TextView noteTextViewTitle;
        TextView noteTextViewDesc;
        TextView noteTextViewPriority;
        ImageView shareNote;

        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            noteTextViewTitle = (TextView) itemView.findViewById(R.id.note_text_view_title);
            noteTextViewDesc = (TextView) itemView.findViewById(R.id.note_text_view_desc);
            noteTextViewPriority = (TextView) itemView.findViewById(R.id.note_text_view_priority);
            shareNote = (ImageView) itemView.findViewById(R.id.share_note);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });

            shareNote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    listener.OnImageClick(getSnapshots().getSnapshot(position), position);
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick (DocumentSnapshot documentSnapshot, int position);
        void OnImageClick (DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener (OnItemClickListener listener) {
        this.listener = listener;
    }

}
