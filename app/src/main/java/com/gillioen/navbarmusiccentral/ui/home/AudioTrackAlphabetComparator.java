package com.gillioen.navbarmusiccentral.ui.Home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gillioen.navbarmusiccentral.AudioTrack;

import java.util.Comparator;

public class AudioTrackAlphabetComparator implements Comparator<AudioTrack> {
    @Override
    public int compare(@NonNull AudioTrack o1, @NonNull AudioTrack o2) {
        return o1.getTitle().compareTo(o2.getTitle());
    }

    @Nullable
    @Override
    public Comparator<AudioTrack> reversed() {
        return null;
    }
}
