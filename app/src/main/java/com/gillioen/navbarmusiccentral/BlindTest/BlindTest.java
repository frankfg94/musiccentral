package com.gillioen.navbarmusiccentral.BlindTest;

import android.util.Log;

import com.gillioen.navbarmusiccentral.AudioTrack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/***
 * Uses the class BlindTestBuilder to be instanciated
 */
public class BlindTest {
    private int gameTrackCount = 10;
    private String title = "Undefined BlindTest";
    private int trackPlayDurationSec = 10;
    private int choiceCountForEachTrack = 4;
    private List<BlindTrack> blindTracks = new ArrayList<>(); // Audiotracks that are configured for a blindtest game
    private List<String> assignedAnswers = new ArrayList<>();
    private Date createdDate;

    BlindTest(int gameTrackCount, String title, int trackPlayDurationSec, int choiceCountForEachTrack, List<AudioTrack> tracks) {
        this.gameTrackCount = gameTrackCount;
        this.title = title;
        this.trackPlayDurationSec = trackPlayDurationSec;
        this.choiceCountForEachTrack = choiceCountForEachTrack;
        assignBlindTracks(tracks);
    }

    BlindTest(List<AudioTrack> tracks)
    {
        assignBlindTracks(tracks);
        createdDate = new Date();
    }

    private void assignBlindTracks(List<AudioTrack> tracks)
    {
        List<AudioTrack> copy = new ArrayList<>();
        for(AudioTrack t : tracks)
            copy.add(t);

        getBlindTracks().clear();
        for(int i = 0; i < getGameTrackCount(); i++)
        {
            Log.i("BLINDTEST","Assigning track number : " + i);
            getBlindTracks().add(new BlindTrack(copy.get(i),getTrackPlayDurationSec(), choiceCountForEachTrack));
            getBlindTracks().get(i).assignAnswers(this);
        }
    }


    public void randomizeTracks()
    {
       Collections.shuffle(getBlindTracks());
    }

    public int getGameTrackCount() {
        return gameTrackCount;
    }

    public void setGameTrackCount(int gameTrackCount) {
        this.gameTrackCount = gameTrackCount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTrackPlayDurationSec() {
        return trackPlayDurationSec;
    }

    public void setTrackPlayDurationSec(int trackPlayDurationSec) {
        this.trackPlayDurationSec = trackPlayDurationSec;
    }

    public int getChoiceCountForEachTrack() {
        return choiceCountForEachTrack;
    }

    public void setChoiceCountForEachTrack(int choiceCountForEachTrack) {
        this.choiceCountForEachTrack = choiceCountForEachTrack;
    }

    public List<BlindTrack> getBlindTracks() {
        return blindTracks;
    }

    public void setBlindTracks(List<BlindTrack> blindTracks) {
        this.blindTracks = blindTracks;
    }

    public List<String> getAssignedAnswers() {
        return assignedAnswers;
    }

    public void setAssignedAnswers(List<String> assignedAnswers) {
        this.assignedAnswers = assignedAnswers;
    }

    @Override
    public String toString()
    {
        StringBuilder b = new StringBuilder();
        b.append(String.format("Titre du Blindtest : %s / %d musiques \n", getTitle(), getGameTrackCount()));
        b.append(String.format("Nombre de réponses possibles : %d \n", getChoiceCountForEachTrack()));
        b.append(String.format("Date de création : %s \n", createdDate));
        List<BlindTrack> bTracks = getBlindTracks();
        for(BlindTrack track : bTracks)
        {
            b.append( track.title);
            for(String answer : track.possibleAnswers)
            {
                b.append(String.format("  Answer -> %s \n", answer));
            }
        }
        return b.toString();
    }
}
