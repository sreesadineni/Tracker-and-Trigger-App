package com.example.myapp;



public class HomeMaintenance {
    String activity;
    String phoneNo;
    String status;
    String activitylower;



    public HomeMaintenance() {
    }

    public HomeMaintenance(String activity, String phoneNo, String status) {
        this.activity = activity;
        this.phoneNo = phoneNo;
        this.status = status;
        this.activitylower=activity.toLowerCase();

    }

    public String getActivity() {
        return activity;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public String getStatus() {
        return status;
    }
}
