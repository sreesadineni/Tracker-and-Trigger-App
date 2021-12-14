package com.example.myapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.firestore.DocumentSnapshot;

public class GroceryAdapter extends FirestoreRecyclerAdapter<Grocery, GroceryAdapter.GroceryHolder> {
    private OnItemClickListener listener;

    public GroceryAdapter(@NonNull FirestoreRecyclerOptions<Grocery> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull GroceryHolder holder, int position, @NonNull Grocery model) {
        holder.textViewItem.setText(model.getGrocery_Item());
        holder.textViewDesc.setText(model.getGrocery_desc());
        holder.textViewPreQuantity.setText(String.valueOf(model.getPresent_Quantity()));
        holder.textViewBuyQuantity.setText(String.valueOf(model.getBuy_Quantity()));

    }

    @NonNull
    @Override
    public GroceryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.grocery_item,parent,false);
        return new GroceryHolder(v);
    }
    public void deleteGrocery(int position){
        getSnapshots().getSnapshot(position).getReference().delete();

    }



    class GroceryHolder extends RecyclerView.ViewHolder{
        MaterialTextView textViewItem;
        MaterialTextView textViewDesc;
        MaterialTextView textViewPreQuantity;
        MaterialTextView textViewBuyQuantity;
        ImageView  shareImage;


        public GroceryHolder(@NonNull View itemView) {
            super(itemView);
            textViewItem=itemView.findViewById(R.id.grocery_item);
            textViewDesc=itemView.findViewById(R.id.grocery_desc);
            textViewPreQuantity=itemView.findViewById(R.id.present_quantity);
            textViewBuyQuantity=itemView.findViewById(R.id.buy_quantity);
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
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener=listener;
    }



}
