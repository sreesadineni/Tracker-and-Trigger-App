package com.example.myapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class TrackerAdapter extends RecyclerView.Adapter<TrackerAdapter.TrackerHolder> {
    private ArrayList<TrackerCategory> mExampleList;
    private OnItemClickListener mlistener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mlistener = listener;
    }
    @NonNull
    @Override
    public TrackerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.tracker_item, parent, false);
        TrackerHolder evh= new TrackerHolder(v,mlistener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull TrackerHolder holder, int position) {
        TrackerCategory currentItem = mExampleList.get(position);
        holder.textView.setText(currentItem.getCategory());
        holder.imageView.setImageResource(currentItem.getImg());

    }

    public TrackerAdapter(ArrayList<TrackerCategory> mExampleList) {
        this.mExampleList = mExampleList;
    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }

    public class TrackerHolder extends RecyclerView.ViewHolder{
    TextView textView;
    ImageView imageView;
    public TrackerHolder(@NonNull View itemView,OnItemClickListener listener) {
        super(itemView);
        textView=itemView.findViewById(R.id.trackerCategory);
        imageView=itemView.findViewById(R.id.trackerImg);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            }
        });

    }
}

}
