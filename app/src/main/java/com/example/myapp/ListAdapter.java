package com.example.myapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class ListAdapter extends FirestoreRecyclerAdapter<List, ListAdapter.ListHolder> {

    private OnCheckBoxClickListener listener;

    public ListAdapter(@NonNull FirestoreRecyclerOptions<List> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ListHolder holder, int position, @NonNull List model) {
        holder.checkBox.setChecked(model.getStatus());
        holder.listItem.setText(model.getItem());
    }

    public void deleteItem (int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    public void updateStatus (int position, boolean newStatus) {
        getSnapshots().getSnapshot(position).getReference().update("status", newStatus);
    }

    @NonNull
    @Override
    public ListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ListHolder(v);
    }

    class ListHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        TextView listItem;

        public ListHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkBox);
            checkBox.setChecked(false);
            listItem = (TextView) itemView.findViewById(R.id.text_view_list_item);

            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onCheckBoxClick(getSnapshots().getSnapshot(position), position);
                    }
                    updateStatus(position, checkBox.isChecked());
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = getAdapterPosition();
                    deleteItem(position);
                    return true;
                }
            });

        }
    }

    public interface OnCheckBoxClickListener {
        void onCheckBoxClick (DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnCheckBoxListener(OnCheckBoxClickListener listener) { this.listener = listener; }
}
