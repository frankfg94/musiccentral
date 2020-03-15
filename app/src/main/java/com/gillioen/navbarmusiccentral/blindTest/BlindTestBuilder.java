package com.gillioen.navbarmusiccentral.blindTest;

import androidx.annotation.NonNull;

import com.gillioen.navbarmusiccentral.AudioTrack;

import java.util.ArrayList;

public class BlindTestBuilder {
    private final ArrayList<AudioTrack> tracks;
    BlindTest bTest;
    private int gameTrackCount = 10;
    private String title = "Undefined BlindTest";
    private int trackPlayDurationSec = 10;
    private int choiceCountForEachTrack = 4;
    private int revealDurSeconds = 5; // The time you have to see the solution before going to the next solution
    public BlindTestBuilder(ArrayList<AudioTrack> tracks)
    {
        this.tracks = tracks;
    }

    @NonNull
    public BlindTest build()
    {
        return new BlindTest(getGameTrackCount(), getTitle(), getTrackPlayDurationSec(),choiceCountForEachTrack, revealDurSeconds,tracks);
    }

    public int getChoiceCountForEachTrack() {
        return choiceCountForEachTrack;
    }

    @NonNull
    public BlindTestBuilder setChoiceCountForEachTrack(int choiceCountForEachTrack) {
        if(choiceCountForEachTrack > 4)
            this.choiceCountForEachTrack = 4;
        else if (choiceCountForEachTrack < 2)
            this.choiceCountForEachTrack = 2;
        else
            this.choiceCountForEachTrack = choiceCountForEachTrack;
        return this;
    }

    @NonNull
    public BlindTestBuilder setRevealDurSeconds(int finishDurSeconds) {
        this.revealDurSeconds = finishDurSeconds;
        return this;
    }

    public int getGameTrackCount() {
        return gameTrackCount;
    }

    @NonNull
    public BlindTestBuilder setGameTrackCount(int gameTrackCount) {
        this.gameTrackCount = gameTrackCount;
        return this;
    }

    public String getTitle() {
        return title;
    }

    @NonNull
    public BlindTestBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public int getTrackPlayDurationSec() {
        return trackPlayDurationSec;
    }

    @NonNull
    public BlindTestBuilder setTrackPlayDurationSec(int trackPlayDurationSec) {
        if(trackPlayDurationSec < 4)
            trackPlayDurationSec = 4;
        this.trackPlayDurationSec = trackPlayDurationSec;
        return this;
    }

    public int getRevealDurSeconds() {
        return revealDurSeconds;
    }
}
