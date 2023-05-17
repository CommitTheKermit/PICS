package com.example.googlemapusingmanual.Utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.example.googlemapusingmanual.R;

public class PushNotification {
    Context context;

    public PushNotification(Context context){
        this.context = context;
    }

    //    Notification
    public void createNotificationChannel(String channelId, String channelName, int importance){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationManager notificationManager = (NotificationManager)this.context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId, channelName, importance));
        }
    }

    public void createNotification(String channelId, int id, String title, String text){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this.context, channelId)
                .setPriority(NotificationCompat.PRIORITY_HIGH).setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(title).setContentText(text)
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
        NotificationManager notificationManager = (NotificationManager)this.context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(id, builder.build());
    }
}
