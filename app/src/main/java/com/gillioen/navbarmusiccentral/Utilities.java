package com.gillioen.navbarmusiccentral;

import android.content.Context;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.os.Build;

import androidx.annotation.NonNull;

public class Utilities {
    public static boolean areHeadphonesPlugged(@NonNull Context c){
        AudioManager audioManager = (AudioManager)c.getSystemService(Context.AUDIO_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            AudioDeviceInfo[] audioDevices = audioManager.getDevices(AudioManager.GET_DEVICES_ALL);
            for(AudioDeviceInfo deviceInfo : audioDevices){
                    if(deviceInfo.getType()==AudioDeviceInfo.TYPE_WIRED_HEADPHONES
                            || deviceInfo.getType()==AudioDeviceInfo.TYPE_WIRED_HEADSET){
                        return true;
                    }
                }
        }
        else
        {
            return audioManager.isWiredHeadsetOn();
        }
        return false;
    }
}
