package com.gillioen.navbarmusiccentral.ui.Home;

import com.gillioen.navbarmusiccentral.AudioTrack;

import java.util.Comparator;

public class AudioTrackDateComparator implements Comparator<AudioTrack> {
    @Override
    public int compare(AudioTrack o1, AudioTrack o2)
    {
        if(o1.getDate() == null || o2.getDate() == null)
            return 0;

        return o1.getDate().compareTo(o2.getDate());
    }

    @Override
    public Comparator<AudioTrack> reversed() {
        return null;
    }
}
