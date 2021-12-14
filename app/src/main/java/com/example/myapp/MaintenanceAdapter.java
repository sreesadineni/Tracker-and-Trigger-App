package com.example.myapp;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import org.w3c.dom.Text;

public class MaintenanceAdapter extends FirestoreRecyclerAdapter<HomeMaintenance, MaintenanceAdapter.MaintenanceHolder> {
    private OnItemClickListener listener;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public MaintenanceAdapter(@NonNull FirestoreRecyclerOptions<HomeMaintenance> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MaintenanceHolder holder, int position, @NonNull HomeMaintenance model) {
        holder.textViewActivity.setText(model.getActivity());
        holder.textViewPhoneNum.setText(model.getPhoneNo());
        holder.textViewStatus.setText(model.getStatus());
    }
    public void deleteBook(int position){
        getSnapshots().getSnapshot(position).getReference().delete();

    }

    @NonNull
    @Override
    public MaintenanceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.maintenance_item,parent,false);
        return new MaintenanceAdapter.MaintenanceHolder(v);
    }
    public void deleteGrocery(int position){
        getSnapshots().getSnapshot(position).getReference().delete();

    }

    public class MaintenanceHolder extends RecyclerView.ViewHolder{
        TextView textViewActivity;
        TextView textViewPhoneNum;
        TextView textViewStatus;
        ImageView shareImage;
        ImageView callImage;


        public MaintenanceHolder(@NonNull View itemView) {
            super(itemView);
            textViewActivity=itemView.findViewById(R.id.home_activites);
            textViewPhoneNum=itemView.findViewById(R.id.phone_number);
            textViewStatus=itemView.findViewById(R.id.maintenance_status);
            shareImage=itemView.findViewById(R.id.share);
            callImage=itemView.findViewById(R.id.callImage);
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
            callImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION && listener!=null){
                        listener.onCallClick(getSnapshots().getSnapshot(position),position);
                    }

                }
            });



        }

    }
    public interface OnItemClickListener{
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
        void onImageClick(DocumentSnapshot documentSnapshot,int position);
        void onCallClick(DocumentSnapshot documentSnapshot,int position);
    }
    public void setOnItemClickListener(MaintenanceAdapter.OnItemClickListener listener){
        this.listener=listener;
    }
}