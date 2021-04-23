package net.kirte;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ReminderService extends Service {
    @Override
    public void onCreate() {
        System.out.println("Creating notification!");
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent intent = new Intent(this.getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        String NOTIFICATION_CHANNEL_ID = "KirTe_ch01";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "KirTe notifications", NotificationManager.IMPORTANCE_HIGH);

            // Configuration for notification channel
            notificationChannel.setDescription("KirTe notification channel");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.YELLOW);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            nm.createNotificationChannel(notificationChannel);
        }

        Notification remind;
        remind = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setContentTitle("KirTe: Muistutus")
                .setContentText("Varaamasi vuoro alkaa pian!")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)
                .setSound(sound)
                .build();
        nm.notify(getId(), remind);
        System.out.println("Notification done!");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Integer getId() {
        return Integer.parseInt(new SimpleDateFormat("MMddHHmm").format(new Date()));
    }
}
