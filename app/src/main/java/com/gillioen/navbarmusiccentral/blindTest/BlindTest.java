package com.gillioen.navbarmusiccentral.blindTest;

import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

import com.gillioen.navbarmusiccentral.AudioTrack;
import com.gillioen.navbarmusiccentral.R;
import com.gillioen.navbarmusiccentral.ui.blindTest.BlindTrackFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/***
 * Uses the class BlindTestBuilder to be instanciated
 */
public class BlindTest {
    public static final int defaultGameTrackCount = 10;
    public static final String defaultTitle = "Undefined BlindTest";
    public static final int defaultTrackPlayDurationSec = 10;
    public static final int defaultChoiceCountForEachTrack = 4;
    public static final int defaultTrackRevealDurationSec = 10;

    private int gameTrackCount = defaultGameTrackCount;
    private String title = defaultTitle;
    private int trackPlayDurationSec = defaultTrackPlayDurationSec;
    private int choiceCountForEachTrack = defaultChoiceCountForEachTrack;
    private int trackRevealDurationSec = defaultTrackRevealDurationSec; // Of much seconds will we show the answer of the blindtrack question
    @NonNull
    private List<BlindTrack> blindTracks = new ArrayList<>(); // Audiotracks that are configured for a blindtest game
    private List<String> assignedAnswers = new ArrayList<>();
    private Date createdDate;
    ProgressBar bar;

    BlindTest(int gameTrackCount, String title, int trackPlayDurationSec, int choiceCountForEachTrack, int trackRevealDurationSec, @NonNull List<AudioTrack> tracks) {
        this.gameTrackCount = gameTrackCount;
        this.title = title;
        this.trackPlayDurationSec = trackPlayDurationSec;
        this.choiceCountForEachTrack = choiceCountForEachTrack;
        this.trackRevealDurationSec = trackRevealDurationSec;
        assignBlindTracks(tracks);
        createdDate = new Date();
    }

    BlindTest(@NonNull List<AudioTrack> tracks)
    {
        assignBlindTracks(tracks);
        createdDate = new Date();
    }

    private void assignBlindTracks(@NonNull List<AudioTrack> tracks)
    {
        getBlindTracks().clear();
        List<BlindTrack> fullSongList = new ArrayList<>();

        for(int i = 0 ; i < tracks.size(); i++)
        {
            BlindTrack btrack = new BlindTrack(tracks.get(i),getTrackPlayDurationSec(), choiceCountForEachTrack, trackRevealDurationSec);
            fullSongList.add(btrack);
        }

        Collections.shuffle(fullSongList);

        for(int i = 0; i < tracks.size(); i++)
        {
            Log.i("BLINDTEST","Assigning track number : " + i);
            if( i < getGameTrackCount())
            {
                blindTracks.add(fullSongList.get(i));
            }
        }

        for(BlindTrack track : blindTracks)
           track.assignAnswers(this,fullSongList);
    }
    public void startGame(@NonNull BlindTrackFragment blindTrackFragment, @NonNull View root){
        int i = 0;
        Log.d("BLINDTEST","Let the blindtest BEGIN");
        Log.d("BLINDTEST",toString());
        bar = root.findViewById(R.id.progressBarBlindTrack);
        bar.setMax(getGameTrackCount());
        startGameAtTrackNumber(blindTrackFragment,root,i);
    }

    private  void startGameAtTrackNumber(@NonNull BlindTrackFragment blindTrackFragment, View root, int i){
        bar.setProgress(i);
        List<BlindTrack> tracks = getBlindTracks();
        if(i < tracks.size() && i < gameTrackCount)
        {
            // We start a question
            BlindTrack curBlindTrack = tracks.get(i);
            curBlindTrack.playInFragment(blindTrackFragment,root);
            curBlindTrack.setCustomEventListener(() -> {
                // We start the next question after it is finished
                startGameAtTrackNumber(blindTrackFragment,root,i+1);
            });
        }
        else
        {
            Log.d("BLINDTEST","Finished playing the tracks");
        }
    }


    public int getGameTrackCount() {
        return gameTrackCount;
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

    @NonNull
    public List<BlindTrack> getBlindTracks() {
        return blindTracks;
    }

    public List<String> getAssignedAnswers() {
        return assignedAnswers;
    }

    public void setAssignedAnswers(List<String> assignedAnswers) {
        this.assignedAnswers = assignedAnswers;
    }


    public int getTrackRevealDurationSec() {
        return trackRevealDurationSec;
    }

    public void setTrackRevealDurationSec(int trackRevealDurationSec) {
        this.trackRevealDurationSec = trackRevealDurationSec;
    }

    @NonNull
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
            b.append( track.getTitle() + "\n");
            for(String answer : track.possibleAnswers)
            {
                b.append(String.format("  Answer -> %s \n", answer));
            }
        }
        return b.toString();
    }


}
