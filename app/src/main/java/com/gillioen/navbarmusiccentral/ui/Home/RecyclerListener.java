package com.gillioen.navbarmusiccentral.ui.Home;

import androidx.recyclerview.widget.RecyclerView;

import com.gillioen.navbarmusiccentral.AudioTrack;

import java.util.List;

public interface RecyclerListener {
    public void callback(List<AudioTrack> rv);
}
