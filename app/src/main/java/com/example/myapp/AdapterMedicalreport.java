package com.example.myapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
public class AdapterMedicalreport extends RecyclerView.Adapter<AdapterMedicalreport.HolderMedicalreport> implements Filterable{

    private Context context;
    public ArrayList<modelitem> medicalreportslist,filterlist;
    private filterMedicalreports filter;
    private static final String TAG = "DocSnippets";
    FirebaseUser user;
    String userid;

    private FirebaseAuth fAuth;

    public AdapterMedicalreport(Context context, ArrayList<modelitem> medicalreportslist) {
        this.context = context;
        this.medicalreportslist = medicalreportslist;
        this.filterlist=medicalreportslist;
    }

    @NonNull
    @Override
    public HolderMedicalreport onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.row_medicalreports,parent,false);
        return new HolderMedicalreport(view);

    }

    @Override
    public void onBindViewHolder(@NonNull HolderMedicalreport holder, int position) {

        modelitem modelitem= medicalreportslist.get(position);
        String id= modelitem.getItemid();
        String title=modelitem.getItemTitle();
        String timestamp=modelitem.getTimestamp();
        String icon= modelitem.getMedical_report();

        holder.titleTv.setText(title);

        try{

            Picasso.get().load(icon).fit().placeholder(R.drawable.ic_baseline_add_photo_alternate_24).into(holder.medicalreportIv);
        }
        catch (Exception e){

            holder.medicalreportIv.setImageResource(R.drawable.ic_baseline_add_photo_alternate_24);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //on click shows details
                detailsbottomsheet(modelitem);

            }
        });


    }
    private void detailsbottomsheet(modelitem modelitem) {

        //bottom sheet
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.bs_item_details, null);
        // set view to bottom sheet
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.setCanceledOnTouchOutside(false);

        ImageButton backBtn = view.findViewById(R.id.backBtn);
        ImageButton deleteBtn = view.findViewById(R.id.deleteBtn);
        TextView titleTv = view.findViewById(R.id.titleTv);


        //getdata
        String id = modelitem.getItemid();
        String title = modelitem.getItemTitle();
        String timestamp = modelitem.getTimestamp();


        //setdata

        titleTv.setText(title);
        bottomSheetDialog.show();
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bottomSheetDialog.dismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete")
                        .setMessage("Are you sure you want to delete this item \"" + title + "\"" + "?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //   deleteitem(id); // id is itemid

                                fAuth = FirebaseAuth.getInstance();
                                user = fAuth.getCurrentUser();
                                userid=user.getUid();
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                db.collection("users").document(userid)
                                        .collection("Medical report").document(timestamp)
                                        .delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                                Toast.makeText(context, "Item deleted...", Toast.LENGTH_SHORT).show();

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w(TAG, "Error deleting document", e);
                                                Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });


                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //cancel
                                dialog.dismiss();

                            }
                        })
                        .show();

            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bottomSheetDialog.dismiss();
            }
        });

    }


    @Override
    public int getItemCount() {
        return medicalreportslist.size();
    }
    @Override
    public Filter getFilter() {

        if(filter==null){

            filter=new filterMedicalreports(this,filterlist);
        }
        return filter;
    }

    class HolderMedicalreport extends RecyclerView.ViewHolder{

        private ImageView medicalreportIv;
        private TextView titleTv;

        public HolderMedicalreport(@NonNull View itemView) {
            super(itemView);

            medicalreportIv=itemView.findViewById(R.id.medicalreportIv);
            titleTv=itemView.findViewById(R.id.titleTv);

        }
    }



}
