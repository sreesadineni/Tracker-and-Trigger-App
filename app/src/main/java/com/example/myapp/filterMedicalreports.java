package com.example.myapp;

import android.widget.Filter;

import java.util.ArrayList;

public class filterMedicalreports extends Filter {

    private AdapterMedicalreport adapter;
    private ArrayList<modelitem> filterlist;


    public filterMedicalreports(AdapterMedicalreport adapter, ArrayList<modelitem> filterlist) {
        this.adapter = adapter;
        this.filterlist = filterlist;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {

        FilterResults results= new FilterResults();
        // validate data for search query
        if(constraint!=null && constraint.length()>0){
            //change to uppercase, to make case sensitive
            constraint=constraint.toString().toUpperCase();
            //store our filtered list
            ArrayList<modelitem> filteredmodels = new ArrayList<>();
            for(int i=0; i<filterlist.size();i++){

                //check,search by title
                if(filterlist.get(i).getItemTitle().toUpperCase().contains(constraint)){
                    //add filtered data to list
                    filteredmodels.add(filterlist.get(i));
                }
            }
            results.count=filteredmodels.size();
            results.values=filteredmodels;

        }
        else{

            results.count=filterlist.size();
            results.values=filterlist;

        }

        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {

        adapter.medicalreportslist= (ArrayList<modelitem>)results.values;
        // refresh changes
        adapter.notifyDataSetChanged();

    }


}
