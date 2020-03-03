package com.gillioen.navbarmusiccentral;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.session.MediaSession;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.gillioen.navbarmusiccentral.Services.NotificationActionService;


public class NotificationAudioBar {

    public static Notification notif;
    public static String CHANNEL_ID = "channel1";
    private static boolean channelSubbed = false;

    // Notification commands
    public static final String ACTION_PLAY = "actionplay";


    public static void createNotification(Context c, AudioTrack track, int playButton, int pos , int size)
    {
        Intent intentPlay = new Intent(c, NotificationActionService.class)
            .setAction(ACTION_PLAY);
        PendingIntent pendingIntentPlay = PendingIntent.getBroadcast(c,0,intentPlay,PendingIntent.FLAG_UPDATE_CURRENT);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            MediaSession mediaSessionCompat = new MediaSession(c,"tag");

            // Pour le moment on ne gère pas les images

            notif = new NotificationCompat.Builder(c,CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_music_note)
                    //.setLargeIcon(icon)
                    .setContentTitle(track.title)
                    .setContentText(track.artist)
                    .setOnlyAlertOnce(true)
                    .addAction(R.drawable.play,"Play", pendingIntentPlay)
                    .setShowWhen(false) // Ne pas afficher le timestamp
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .build();

            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(c);
            notificationManagerCompat.notify(1,notif);
        }
        else {
            notif = new NotificationCompat.Builder(c,CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_music_note)
                    .setContentTitle(track.title)
                    .setContentText(track.artist)
                    //.setLargeIcon(icon)
                    .setOnlyAlertOnce(true)
                    .setShowWhen(false)
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .build();

            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(c);
            notificationManagerCompat.notify(1,notif);
        }


    }

    static NotificationManager manager;
    public static void startAudioBar(Activity act)
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,"Efrei", NotificationManager.IMPORTANCE_LOW);
            manager = act.getSystemService(NotificationManager.class);
            if(manager != null){
                manager.createNotificationChannel(channel);
            }
        }

    }
}
