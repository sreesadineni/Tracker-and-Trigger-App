package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.allyants.notifyme.NotifyMe;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;

public class NewMeetActivity extends AppCompatActivity {
    EditText titleEditText, docsEditText, agendaEditText;
    Button setMeetDate, setMeetTime, saveMeet;
    TextView dateTextView, timeTextView;
    DatePickerDialog datePickerDialog;

    String dateStr = "dd/mm/yy", timeStr = "hh:mm";
    Calendar dateTime;
    int yearSave, monthSave, daySave, hourSave, minuteSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_meet);

        titleEditText = (EditText) findViewById(R.id.edit_text_meet_title);

        docsEditText = (EditText) findViewById(R.id.edit_text_meet_docs);
        agendaEditText = (EditText) findViewById(R.id.edit_text_meet_agenda);

        dateTextView = (TextView) findViewById(R.id.text_view_meet_date);
        timeTextView = (TextView) findViewById(R.id.text_view_meet_time);

        setMeetDate = (Button) findViewById(R.id.button_set_meet_date);
        setMeetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleDateButton();
            }
        });
        setMeetTime = (Button) findViewById(R.id.button_set_meet_time);
        setMeetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleTimeButton();
            }
        });

        saveMeet = (Button) findViewById(R.id.save_meet);
        saveMeet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMeet();
            }
        });

    }

    public void handleDateButton() {
        Calendar calendar = Calendar.getInstance();
        int YEAR = calendar.get(Calendar.YEAR);
        int MONTH = calendar.get(Calendar.MONTH);
        int DATE = calendar.get(Calendar.DATE);

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int YEAR, int MONTH, int DATE) {
                dateStr = DATE + "/" + (MONTH+1) + "/" + YEAR;
                dateTextView.setText(dateStr);
                calendar.set(YEAR, MONTH, DATE);
                Intent intent = new Intent(NewMeetActivity.this, MeetingBroadcastReceiver.class);

                intent.putExtra("time",titleEditText.getText().toString());
                yearSave = YEAR;
                monthSave = MONTH;
                daySave = DATE;
            }
        }, YEAR, MONTH, DATE);
        datePickerDialog.show();

    }

    public void handleTimeButton() {
        Calendar calendar = Calendar.getInstance();
        int HOUR = calendar.get(Calendar.HOUR);
        int MINUTE = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int HOUR, int MINUTE) {
                timeStr = String.format("%02d", HOUR) + ":" + String.format("%02d", MINUTE);
                timeTextView.setText(timeStr);
                hourSave = HOUR;
                minuteSave =MINUTE;
            }
        }, HOUR, MINUTE, false);
        timePickerDialog.show();
    }

    public void saveMeet() {
        String title = titleEditText.getText().toString();
        String agenda = agendaEditText.getText().toString();
        String docs = docsEditText.getText().toString();
        Calendar finalDateTime = Calendar.getInstance();

        finalDateTime.set(Calendar.DATE,daySave);
        finalDateTime .set(Calendar.HOUR_OF_DAY,hourSave);
        finalDateTime.set(Calendar.MINUTE,minuteSave);
        finalDateTime.set(Calendar.SECOND,0);
        long alarmStartTime=finalDateTime.getTimeInMillis();
        Timestamp meetDateTime = new Timestamp((long)finalDateTime.getTimeInMillis()/1000, 0);
        long ms = meetDateTime.getSeconds();

        if (title.trim().isEmpty() || agenda.trim().isEmpty()) {
            Toast.makeText(this, "Please insert a title and agenda", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseAuth fAuth = FirebaseAuth.getInstance();

        CollectionReference meetingsRef = FirebaseFirestore.getInstance()
                .collection("users").document(fAuth.getCurrentUser().getUid()).collection("Meetings");
        meetingsRef.add(new Meeting(title, agenda, docs, meetDateTime, ms /* dateTime, dateStr, timeStr*/))
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(NewMeetActivity.this, "firestore success", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(NewMeetActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
        Toast.makeText(this, "Meeting added", Toast.LENGTH_SHORT).show();

        Calendar notifCalendar = Calendar.getInstance();
        notifCalendar.set(yearSave, monthSave, daySave, hourSave , minuteSave);


        Intent intent = new Intent(NewMeetActivity.this, MeetingBroadcastReceiver.class);
        intent.putExtra("time",titleEditText.getText().toString());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                NewMeetActivity.this,  0, intent, 0);


        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, notifCalendar.getTimeInMillis(), pendingIntent);
    }

}