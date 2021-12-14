package com.example.myapp;
import com.google.firebase.Timestamp;


public class Meeting {
    private String title;
    private String agenda;
    private String docs;
    Timestamp meetTimeStamp;
    long seconds = 0L;

    public Meeting() {

    }

    public Meeting(String title, String agenda, String docs, Timestamp meetTimeStamp, long seconds/*String date, String time*//*Date meetDateTime*/) {
        this.title = title;
        this.agenda = agenda;
        this.docs = docs;
        this.meetTimeStamp = meetTimeStamp;
        this.seconds = seconds;
    }

    public String getTitle() {
        return title;
    }

    public String getAgenda() {
        return agenda;
    }

    public String getDocs() {
        return docs;
    }

    String TimeToString() {
        String timeStr = String.format("%02d", meetTimeStamp.toDate().getHours()) + ":" + String.format("%02d", meetTimeStamp.toDate().getMinutes());
            return  timeStr;
    }

    String DateToString() {
        String dateStr = meetTimeStamp.toDate().getDate() + "/" + meetTimeStamp.toDate().getMonth() + "/" + String.format("%04d", meetTimeStamp.toDate().getYear());
        return dateStr;
    }

    public Timestamp getMeetTimeStamp() {
        return meetTimeStamp;
    }

}
