package com.gillioen.navbarmusiccentral.ui.Home;

import com.gillioen.navbarmusiccentral.AudioTrack;

import java.util.Comparator;

public class AudioTrackAPIComparator implements Comparator<AudioTrack> {
    @Override
    public int compare(AudioTrack o1, AudioTrack o2) {
        return o1.getApi().compareTo(o2.getApi());
    }

    @Override
    public Comparator<AudioTrack> reversed() {
        return null;
    }
}
