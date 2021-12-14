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

public class GeneralAdapter extends FirestoreRecyclerAdapter<General, GeneralAdapter.GeneralHolder> {
    private OnItemClickListener listener;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public GeneralAdapter(@NonNull FirestoreRecyclerOptions<General> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull GeneralHolder holder, int position, @NonNull General model) {
        holder.textViewItem.setText(model.getTitle());
        holder.textViewDesc.setText(model.getDesc());
        holder.textViewExtra.setText(model.getExtra());
    }

    @NonNull
    @Override
    public GeneralHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.general_item,parent,false);
        return new GeneralHolder(v);
    }
    public void deleteGeneral(int position){
        getSnapshots().getSnapshot(position).getReference().delete();

    }


    class GeneralHolder extends RecyclerView.ViewHolder{
        TextView textViewItem;
        TextView textViewDesc;
        TextView textViewExtra;
        ImageView shareImage;

        public GeneralHolder(@NonNull View itemView) {
            super(itemView);
            textViewItem=itemView.findViewById(R.id.general_title);
            textViewDesc=itemView.findViewById(R.id.general_desc);
            textViewExtra=itemView.findViewById(R.id.generalExtra);
            shareImage=itemView.findViewById(R.id.image_share);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION && listener!=null){
                        listener.onItemClick(getSnapshots().getSnapshot(position),position);
                    }
                }
            });
            shareImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION && listener!=null){
                        listener.onImageClick(getSnapshots().getSnapshot(position),position);
                    }

                }
            });

        }
    }
    public interface OnItemClickListener{
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
        void onImageClick(DocumentSnapshot documentSnapshot,int position);
    }
    public void setOnItemClickListener(GeneralAdapter.OnItemClickListener listener){
        this.listener=listener;
    }

}
