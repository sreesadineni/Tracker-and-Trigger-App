package com.example.myapp;

public class modelitem {

    private String itemid,itemTitle,timestamp,medical_report;


    public modelitem() {
    }

    public modelitem(String itemid, String itemTitle, String timestamp, String medical_report) {
        this.itemid = itemid;
        this.itemTitle = itemTitle;
        this.timestamp = timestamp;
        this.medical_report = medical_report;
    }

    public String getItemid() {
        return itemid;
    }
    public void setItemid(String itemid) {
        this.itemid = itemid;
    }



    public String getItemTitle() {
        return itemTitle;
    }
    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }


    public String getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }



    public String getMedical_report() {
        return medical_report;
    }
    public void setMedical_report(String medical_report) {
        this.medical_report = medical_report;
    }
}
