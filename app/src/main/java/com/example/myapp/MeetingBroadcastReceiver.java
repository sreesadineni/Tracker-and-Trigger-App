package com.example.myapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.EditText;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.example.myapp.NewMeetActivity;

public class MeetingBroadcastReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID="meeting";
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        //throw new UnsupportedOperationException("Not yet implemented");

            int notificationId=intent.getIntExtra("notificationId",0);
            String time=intent.getStringExtra("time");
//            cal mainActivity when notification is tapped
            Intent mainIntent=new Intent(context,MainActivity.class);
            PendingIntent contentIntent=PendingIntent.getActivity(
                    context, 0,mainIntent,0
            );

            NotificationManager notificationManager=
                    (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
                CharSequence channel_name="My Notification";
                int importance =NotificationManager.IMPORTANCE_DEFAULT;

                NotificationChannel channel =new NotificationChannel(CHANNEL_ID,channel_name,importance);
                notificationManager.createNotificationChannel(channel);

            }

            //prepare notification
            NotificationCompat.Builder builder =new NotificationCompat.Builder(context,CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_baseline_meeting_room_24)
                    .setContentTitle("Meeting Remainder")
                    .setContentText("You have a meeting now")
                    .setContentIntent(contentIntent)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true);

            //notify
            notificationManager.notify(notificationId,builder.build());
    }
}