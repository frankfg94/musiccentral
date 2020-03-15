package com.gillioen.navbarmusiccentral;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Recoit les actions de l'audiobar
 */
public class AudioBroadcastReceiver extends BroadcastReceiver {

    public static MainActivity ma;

    public AudioBroadcastReceiver(){ }
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i("AUDIOBAR", "Detected click from audiobar, actual track is  " + ma.curTrackIndex);
        switch (intent.getAction()) {
            case NotificationGenerator.NOTIFY_PAUSE:
                Log.i("AUDIOBAR", "NOTIFY_PAUSE");
                break;
            case NotificationGenerator.NOTIFY_PLAY:
                Toast.makeText(context, "Musique en pause ou continuée", Toast.LENGTH_LONG).show();
                if (ma.audiobarIconModePlay)
                {
                    ma.pauseTrack();
                }
                else
                {
                    ma.playTrack(ma.musicList.get(MainActivity.curTrackIndex));
                    ma.audiobarIconModePlay = true;
                }
                Log.i("AUDIOBAR", "NOTIFY_PLAY");
                break;
            case NotificationGenerator.NOTIFY_NEXT:
                Log.i("AUDIOBAR", "NOTIFY_NEXT");
                if(ma.curTrackIndex+1 < ma.musicList.size())
                {
                    ma.curTrackIndex++;
                    ma.playTrack(ma.musicList.get(ma.curTrackIndex));
                }
                else
                    Toast.makeText(context, "Fin de la liste", Toast.LENGTH_LONG).show();
                break;
            case NotificationGenerator.NOTIFY_PREVIOUS:
                Toast.makeText(context, "NOTIFY_PREV", Toast.LENGTH_LONG).show();
                if(ma.curTrackIndex-1 > 0)
                {
                    ma.curTrackIndex--;
                    ma.playTrack(ma.musicList.get(ma.curTrackIndex));
                }
                else
                    Toast.makeText(context, "Début de la liste", Toast.LENGTH_LONG).show();
                Log.i("AUDIOBAR", "NOTIFY_PREV");
                break;
        }

    }
}
