package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public class NewNoteActivity extends AppCompatActivity {
    private EditText editTextTitle;
    private EditText editTextDesc;
    private NumberPicker numberPickerPriority;
    private Button save_note;
    private Button b2;
    String  timeStr = "hh:mm";
    int hourSave,minuteSave;
    TextView timeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        editTextTitle = (EditText) findViewById(R.id.edit_text_note_title);

        editTextDesc = (EditText) findViewById(R.id.edit_text_note_desc);
        numberPickerPriority = (NumberPicker) findViewById(R.id.number_picker_note_priority);
        save_note = (Button) findViewById(R.id.save_note);
        b2=(Button)findViewById(R.id.b2);
        timeTextView= (TextView)findViewById(R.id.timestr);

        numberPickerPriority.setMinValue(1);
        numberPickerPriority.setMaxValue(10);

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleTimeButton();
            }
        });
        save_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });
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
    private void saveNote() {
        String title = editTextTitle.getText().toString();
        String desc = editTextDesc.getText().toString();
        int priority = numberPickerPriority.getValue();
        Calendar finalDateTime = Calendar.getInstance();

        finalDateTime .set(Calendar.HOUR_OF_DAY,hourSave);
        finalDateTime.set(Calendar.MINUTE,minuteSave);
        finalDateTime.set(Calendar.SECOND,0);
        long alarmStartTime=finalDateTime.getTimeInMillis();
        if (title.trim().isEmpty() || desc.trim().isEmpty()) {
            Toast.makeText(this, "Please insert a title and description", Toast.LENGTH_SHORT).show();
            return;
        }
        FirebaseAuth fAuth = FirebaseAuth.getInstance();

        CollectionReference notebookRef = FirebaseFirestore.getInstance()
                .collection("users").document(fAuth.getCurrentUser().getUid()).collection("Notebook");
        notebookRef.add(new Note(title, desc, priority).getNote()).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(NewNoteActivity.this, "firestore success", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(NewNoteActivity.this, "Error", Toast.LENGTH_SHORT).show();

            }
        });
        Intent in=new Intent(this.getApplicationContext(),NewNotify.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                NewNoteActivity.this,  0, in, 0);
        Toast.makeText(this, "Note added", Toast.LENGTH_SHORT).show();
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmStartTime, pendingIntent);
        finish();
    }

}