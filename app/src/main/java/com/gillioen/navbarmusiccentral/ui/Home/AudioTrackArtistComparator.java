package com.gillioen.navbarmusiccentral.ui.Home;

import com.gillioen.navbarmusiccentral.AudioTrack;

import java.util.Comparator;

public class AudioTrackArtistComparator implements Comparator<AudioTrack> {
    @Override
    public int compare(AudioTrack o1, AudioTrack o2)
    {
        return o1.getArtist().compareTo(o2.getArtist());
    }

    @Override
    public Comparator<AudioTrack> reversed() {
        return null;
    }
}
