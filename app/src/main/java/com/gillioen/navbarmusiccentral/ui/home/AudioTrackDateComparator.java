package com.gillioen.navbarmusiccentral.ui.home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gillioen.navbarmusiccentral.AudioTrack;

import java.util.Comparator;

public class AudioTrackDateComparator implements Comparator<AudioTrack> {
    @Override
    public int compare(@NonNull AudioTrack o1, @NonNull AudioTrack o2)
    {
        if(o1.getDate() == null || o2.getDate() == null)
            return 0;

        return o1.getDate().compareTo(o2.getDate());
    }

    @Nullable
    @Override
    public Comparator<AudioTrack> reversed() {
        return null;
    }
}
