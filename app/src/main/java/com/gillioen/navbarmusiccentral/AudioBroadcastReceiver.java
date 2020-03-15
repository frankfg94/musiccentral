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

        Log.d("AUDIOBAR", "Detected click from audiobar, actual track is  " + MainActivity.curTrackIndex);
        switch (intent.getAction()) {
            case NotificationGenerator.NOTIFY_PAUSE:
                Log.i("AUDIOBAR", "NOTIFY_PAUSE");
                break;
            case NotificationGenerator.NOTIFY_PLAY:
                Toast.makeText(context, "Musique en pause ou continuée", Toast.LENGTH_LONG).show();
                if (MainActivity.audiobarIconModePlay)
                {
                    ma.pauseTrack();
                }
                else
                {
                    ma.playTrack(ma.musicList.get(MainActivity.curTrackIndex));
                    MainActivity.audiobarIconModePlay = true;
                }
                Log.d("AUDIOBAR", "NOTIFY_PLAY");
                break;
            case NotificationGenerator.NOTIFY_NEXT:
                Log.d("AUDIOBAR", "NOTIFY_NEXT");
                if(MainActivity.curTrackIndex +1 < ma.musicList.size())
                {
                    MainActivity.curTrackIndex++;
                    ma.playTrack(ma.musicList.get(MainActivity.curTrackIndex));
                }
                else
                    Toast.makeText(context, "Fin de la liste", Toast.LENGTH_LONG).show();
                break;
            case NotificationGenerator.NOTIFY_PREVIOUS:
                Toast.makeText(context, "NOTIFY_PREV", Toast.LENGTH_LONG).show();
                if(MainActivity.curTrackIndex -1 > 0)
                {
                    MainActivity.curTrackIndex--;
                    ma.playTrack(ma.musicList.get(MainActivity.curTrackIndex));
                }
                else
                    Toast.makeText(context, "Début de la liste", Toast.LENGTH_LONG).show();
                Log.d("AUDIOBAR", "NOTIFY_PREV");
                break;
        }

    }
}
