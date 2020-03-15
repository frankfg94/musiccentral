package com.gillioen.navbarmusiccentral.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

import com.gillioen.navbarmusiccentral.ApiType;
import com.gillioen.navbarmusiccentral.AudioTrack;
import com.gillioen.navbarmusiccentral.MainActivity;
import com.gillioen.navbarmusiccentral.NotificationGenerator;

public class AudiobarNotificationService extends IntentService {


    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public AudiobarNotificationService(String name) {
        super(name);
    }

    public AudiobarNotificationService(){
        super("com.gillioen.navbarmusiccentral.service");
    }

    public static MainActivity ma;
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d("AUDIOSERVICE","Received audio intent!!");
        switch(intent.getStringExtra("todo"))
        {
            case "sync_cur_track":
                AudioTrack t = new AudioTrack();
                t.setImgPath(intent.getStringExtra("imgPath"));
                t.setAudioPath(intent.getStringExtra("audioPath"));
                t.setTitle(intent.getStringExtra("title"));
                t.setApi((ApiType) intent.getSerializableExtra("apiType"));
                boolean isPaused = intent.getBooleanExtra("isPaused",false);
                NotificationGenerator.showAudioPlayerNotification(getApplicationContext(),t, isPaused);
                break;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("AUDIOSERVICE","Destroyed service");
    }
}
