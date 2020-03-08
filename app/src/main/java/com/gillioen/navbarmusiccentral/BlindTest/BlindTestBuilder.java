package com.gillioen.navbarmusiccentral.BlindTest;

import com.gillioen.navbarmusiccentral.AudioTrack;

import java.util.ArrayList;

public class BlindTestBuilder {
    private final ArrayList<AudioTrack> tracks;
    BlindTest bTest;
    private int gameTrackCount = 10;
    private String title = "Undefined BlindTest";
    private int trackPlayDurationSec = 10;
    private int choiceCountForEachTrack = 4;
    public BlindTestBuilder(ArrayList<AudioTrack> tracks)
    {
        this.tracks = tracks;
    }

    public BlindTest build()
    {
        return new BlindTest(getGameTrackCount(), getTitle(), getTrackPlayDurationSec(),choiceCountForEachTrack,tracks);
    }

    public int getChoiceCountForEachTrack() {
        return choiceCountForEachTrack;
    }

    public BlindTestBuilder setChoiceCountForEachTrack(int choiceCountForEachTrack) {
        if(choiceCountForEachTrack > 4)
            this.choiceCountForEachTrack = 4;
        else if (choiceCountForEachTrack < 2)
            this.choiceCountForEachTrack = 2;
        else
            this.choiceCountForEachTrack = choiceCountForEachTrack;
        return this;
    }

    public int getGameTrackCount() {
        return gameTrackCount;
    }

    public BlindTestBuilder setGameTrackCount(int gameTrackCount) {
        this.gameTrackCount = gameTrackCount;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public BlindTestBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public int getTrackPlayDurationSec() {
        return trackPlayDurationSec;
    }

    public BlindTestBuilder setTrackPlayDurationSec(int trackPlayDurationSec) {
        if(trackPlayDurationSec < 4)
            trackPlayDurationSec = 4;
        this.trackPlayDurationSec = trackPlayDurationSec;
        return this;
    }
}
