package com.gillioen.navbarmusiccentral;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.nfc.Tag;
import android.os.Looper;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import android.os.Handler;


public  class NotificationGenerator {
    private static final int NOTIFICATION_ID_AUDIO_BAR = 10;
    public static final String NOTIFY_PREVIOUS = "com.gillioen.navbarmusiccentral.prev";
    public static final String NOTIFY_PAUSE = "com.gillioen.navbarmusiccentral.pause";
    public static final String NOTIFY_PLAY = "com.gillioen.navbarmusiccentral.play";
    public static final String NOTIFY_NEXT = "com.gillioen.navbarmusiccentral.next";

   // public static final NotificationBroadcast audioBarReceiver = new NotificationBroadcast();

    private static NotificationManager nm;
    private static  boolean enabled = true;

    /**
     * Enables or disable the audio bar
     */
    public static void setEnabled(boolean isEnabled)
    {
        if(nm != null )
        {
            enabled = isEnabled;
            if(!enabled)
            {
                // Remove all the existing notifications
                nm.cancel(NOTIFICATION_ID_AUDIO_BAR);
            }
        }
    }

    @SuppressLint("RestrictedApi")
    public static void showAudioPlayerNotification(Context c, AudioTrack track){
        if(enabled)
        {
            RemoteViews expandedAudioView = new RemoteViews(c.getPackageName(),R.layout.audio_bar);
            NotificationCompat.Builder nc = new NotificationCompat.Builder(c);
            nm = (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
            Intent notifyIntent = new Intent(c,MainActivity.class);
            notifyIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            PendingIntent pendingIntent = PendingIntent.getActivity(c,0,notifyIntent,PendingIntent.FLAG_UPDATE_CURRENT);
            nc.setContentIntent(pendingIntent);
            nc.setSmallIcon(R.drawable.ic_action_pause);
            nc.setAutoCancel(false);
            nc.setCustomBigContentView(expandedAudioView);
            nc.setContentTitle("Lecteur Central Music");
            nc.setContentText("Control Audio");
            nc.setShowWhen(false);
            nc.getBigContentView().setTextViewText(R.id.audio_bar_song_name,track.getTitle());
            try {
                loadImageIntoAudioBar(nc,c,track);
            } catch (IOException e) {
                e.printStackTrace();
            }

            setListenersForButtons(expandedAudioView,c);
            nm.notify(NOTIFICATION_ID_AUDIO_BAR,nc.build());
        }
    }

    @SuppressLint("RestrictedApi")
    private static NotificationCompat.Builder loadImageIntoAudioBar(NotificationCompat.Builder nc, Context c, AudioTrack track) throws IOException {

        // On vérifie si on est sur le thread UI
        if (Looper.myLooper() != Looper.getMainLooper())
            return nc;

        if(track.imgPath != null)
        {
            if(track.getApi() != ApiType.None)
            {
                Picasso.with(c).load(track.imgPath).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        nc.getBigContentView().setImageViewBitmap( R.id.track_img_audiobar,bitmap);
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
            }
            else
            {
                File f = new File(track.imgPath);
                Picasso.with(c).load(f).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        nc.getBigContentView().setImageViewBitmap( R.id.track_img_audiobar,bitmap);
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
            }
        }
        else
        {
            Picasso.with(c).load(track.imgPath).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    nc.getBigContentView().setImageViewResource( R.id.track_img_audiobar,R.drawable.ic_action_audiotrack);
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });
        }
        return nc;
    }


    /***
     * We add the listeners for the audioplayer notification control
     * @param view
     * @param context
     */
    public static void setListenersForButtons(RemoteViews view, Context context){
        Intent prev = new Intent(NOTIFY_PREVIOUS);
        Intent pause = new Intent(NOTIFY_PAUSE);
        Intent next = new Intent(NOTIFY_NEXT);
        Intent play = new Intent(NOTIFY_PLAY);

        PendingIntent prevI = PendingIntent.getBroadcast(context, 0, prev, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.audio_bar_previous, prevI);

        PendingIntent nextI = PendingIntent.getBroadcast(context, 0, next, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.audio_bar_next, nextI);

        PendingIntent playI = PendingIntent.getBroadcast(context, 0, play, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.audio_bar_play, playI);

        Log.i("AUDIOBAR","Audio bar listeners are ready");
    }
}
