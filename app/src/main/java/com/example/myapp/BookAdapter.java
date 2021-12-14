package com.example.myapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.firestore.DocumentSnapshot;

public class BookAdapter extends FirestoreRecyclerAdapter<Book, BookAdapter.BookHolder> {
    private BookAdapter.OnItemClickListener listener;


    public BookAdapter(@NonNull FirestoreRecyclerOptions<Book> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull BookHolder holder, int position, @NonNull Book model) {
        holder.textViewBookName.setText(model.getBookName());
        holder.textViewBookDesc.setText(model.getBookDesc());
        holder.textViewAuthorName.setText(model.getAuthorName());
        holder.textViewBookStatus.setText(model.getReadingStatus());


    }

    @NonNull
    @Override
    public BookHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.book_item,parent,false);
        return new BookAdapter.BookHolder(v);

    }
    public void deleteBook(int position){
        getSnapshots().getSnapshot(position).getReference().delete();

    }

    class BookHolder extends RecyclerView.ViewHolder{
        MaterialTextView textViewBookName;
        MaterialTextView textViewAuthorName;
        MaterialTextView textViewBookDesc;
        MaterialTextView textViewBookStatus;
        ImageView shareImage;
        String texts;

        public BookHolder(@NonNull View itemView) {
            super(itemView);
            textViewBookName=itemView.findViewById(R.id.book_name);
            textViewAuthorName=itemView.findViewById(R.id.book_author);
            textViewBookDesc=itemView.findViewById(R.id.bookDesc);
            textViewBookStatus=itemView.findViewById(R.id.bookStatus);
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
    public void setOnItemClickListener(BookAdapter.OnItemClickListener listener){
        this.listener=listener;
    }
}
