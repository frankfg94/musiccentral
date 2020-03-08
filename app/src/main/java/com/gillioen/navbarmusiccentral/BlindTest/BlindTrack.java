package com.gillioen.navbarmusiccentral.BlindTest;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.gillioen.navbarmusiccentral.AudioTrack;
import com.gillioen.navbarmusiccentral.MainActivity;
import com.gillioen.navbarmusiccentral.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TimerTask;

public class BlindTrack extends AudioTrack {
    public int playDurationSeconds;
    public List<String> possibleAnswers; // The different names that will be displayed on the buttons, excepting the name of the track itself
    private int playDurSeconds = 4;
    private int maxAnswersCount = 4;

    public BlindTrack(AudioTrack audioTrack, int playDurSeconds, int maxAnswersCount)
    {
        setTrackData(audioTrack);
        this.playDurSeconds = playDurSeconds;
        this.maxAnswersCount = maxAnswersCount;
        if(possibleAnswers == null)
            possibleAnswers = new ArrayList<>();
        possibleAnswers.add(getTitle());
    }

    private void setTrackData(AudioTrack track)
    {
        super.api = track.api;
        super.artist = track.artist;
        super.audioPath = track.audioPath;
        super.description = track.description;
        super.genre = track.genre;
        super.imgPath = track.imgPath;
        super.playListPath = track.playListPath;
        super.title = track.title;
    }

    public void playInFragment(Fragment frag, View root)
    {
        View v = root;
        ArrayList<Button> buttons = new ArrayList<>();
        ImageView trackCoverImgView = v.findViewById(R.id.trackImageBlindtest);
        TextView textView = v.findViewById(R.id.trackTitleBlindTest);
        Button b1 = v.findViewById(R.id.button);
        Button b2 = v.findViewById(R.id.button2);
        Button b3 = v.findViewById(R.id.button3);
        Button b4 = v.findViewById(R.id.button4);
        buttons.add(b1);
        buttons.add(b2);
        buttons.add(b3);
        buttons.add(b4);
        for(int i  = 0 ;  i < buttons.size(); i++)
        {
            if( i < maxAnswersCount )
                buttons.get(i).setText(getTitle());
            else
                buttons.get(i).setVisibility(View.INVISIBLE);
        }
        ((MainActivity)frag.getActivity()).stopTracks();
        ((MainActivity)frag.getActivity()).playTrack(this);

        trackCoverImgView.setImageResource(R.drawable.question_mark);

        // Timer after the track is played
        new java.util.Timer().schedule(new TimerTask(){
            @Override
            public void run() {
                ((MainActivity)frag.getActivity()).stopTracks();

                ((MainActivity)frag.getActivity()).runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        // Stuff that updates the UI
                        textView.setText(getTitle());

                        // Revealing the image of the track
                        if(imgPath!=null)
                            Picasso.with(frag.getContext())
                                    .load(imgPath)
                                    .fit()
                                    .placeholder(R.drawable.ic_launcher_background)
                                    .into(trackCoverImgView);
                        else
                            trackCoverImgView.setImageResource(R.drawable.ic_music_note);
                    }
                });
            }
        },1000*playDurSeconds,999999999);
    }

    public void assignAnswers(BlindTest blindTest) {

        // Normally set to 1 (the name of the track is there by default)
        int curAnswerCount = possibleAnswers.size();

        // We fetch the tracks that we want to check the titles
        List<String> trackTitles = new ArrayList<>();
        for(BlindTrack t : blindTest.getBlindTracks() )
        {
            trackTitles.add(t.getTitle());
        }
        Collections.sort(trackTitles);

        for(int i = 0 ; i < trackTitles.size() && curAnswerCount < maxAnswersCount; i++)
        {
            String curTrackTitle = trackTitles.get(i);
            if(!blindTest.getAssignedAnswers().contains(curTrackTitle))
            {
                this.possibleAnswers.add(curTrackTitle);
                blindTest.getAssignedAnswers().add(curTrackTitle);
                curAnswerCount++;
            }
        }
    }
}
