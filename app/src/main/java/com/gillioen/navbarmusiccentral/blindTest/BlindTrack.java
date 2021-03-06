package com.gillioen.navbarmusiccentral.blindTest;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.gillioen.navbarmusiccentral.ApiType;
import com.gillioen.navbarmusiccentral.AudioTrack;
import com.gillioen.navbarmusiccentral.MainActivity;
import com.gillioen.navbarmusiccentral.NotificationGenerator;
import com.gillioen.navbarmusiccentral.R;
import com.gillioen.navbarmusiccentral.ui.blindTest.BlindTrackFragment;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TimerTask;

public class BlindTrack extends AudioTrack {
    public List<String> possibleAnswers; // The different names that will be displayed on the buttons, excepting the name of the track itself
    private int playDurSeconds = 4; // The time you have to guess a track
    private int finishDurSeconds = 5; // The time you have to see the solution before going to the next solution
    private int maxAnswersCount = 4; // The number of results that can be displayed on screen
    public OnBlindtrackFinished  blindtrackFinished;
    @NonNull
    private String defaultText = "Guess this Track";

    public void setCustomEventListener(OnBlindtrackFinished eventListener) {
        blindtrackFinished = eventListener;
    }

    public BlindTrack(@NonNull AudioTrack audioTrack, int playDurSeconds, int maxAnswersCount, int finishDurSeconds)
    {
        setTrackData(audioTrack);
        this.playDurSeconds = playDurSeconds;
        this.maxAnswersCount = maxAnswersCount;
        this.finishDurSeconds = finishDurSeconds;
        if(possibleAnswers == null)
            possibleAnswers = new ArrayList<>();
        possibleAnswers.add(getTitle());

        this.blindtrackFinished = () -> Log.d("BLINDTEST","Finished playing the track " + audioTrack);
    }

    private void setTrackData(@NonNull AudioTrack track)
    {
        super.api = track.getApi();
        super.artist = track.getArtist();
        super.audioPath = track.getAudioPath();
        super.description = track.getDescription();
        super.genre = track.getGenre();
        super.imgPath = track.getImgPath();
        super.playListPath = track.getPlayListPath();
        super.title = track.getTitle();
    }

    Drawable background;
    public void playInFragment(@NonNull BlindTrackFragment frag, @NonNull View root)
    {
        MainActivity ma = (MainActivity)frag.getActivity();
        if(ma != null)
        {
            ma.stopTracks();
            NotificationGenerator.setEnabled(false);
            Log.d("BLINDTEST","Starting question for Track " + getTitle());
            View v = root;
            ArrayList<Button> buttons = new ArrayList<>();
            ImageView trackCoverImgView = v.findViewById(R.id.trackImageBlindtest);
            TextView textView = v.findViewById(R.id.trackTitleBlindText);
            Button b1 = v.findViewById(R.id.button);
            Button b2 = v.findViewById(R.id.button2);
            Button b3 = v.findViewById(R.id.button3);
            Button b4 = v.findViewById(R.id.button4);
            buttons.add(b1);
            buttons.add(b2);
            buttons.add(b3);
            buttons.add(b4);

            // Whe shufffle the anwers so that the good answer won't be the first button that will be displayed
            Collections.shuffle(possibleAnswers);
            background = b1.getBackground();

            for(Button b : buttons)
            {
                b.setOnClickListener(v1 -> {
                    ma.runOnUiThread(() -> {
                        if(b.getText().equals(getTitle()))
                            b.setBackgroundColor(Color.rgb(0,255,0));
                        else
                            b.setBackgroundColor(Color.rgb(255,0,0));
                        b.setClickable(false);
                    });
                });
            }


            ma.runOnUiThread(() -> {
                TextView tv = root.findViewById(R.id.trackTitleBlindText);
                tv.setText(defaultText);
                for(int i  = 0 ;  i < buttons.size(); i++)
                {
                    if (possibleAnswers != null && possibleAnswers.size() > 1 && i < maxAnswersCount && i < possibleAnswers.size()) {
                        buttons.get(i).setText(possibleAnswers.get(i));
                    } else if (i < maxAnswersCount)
                        buttons.get(i).setText(getTitle());
                    else
                        buttons.get(i).setVisibility(View.INVISIBLE);
                }
                trackCoverImgView.setImageResource(R.drawable.question_mark);
            });
            ma.stopTracks();
            ma.playTrack(this);

            if(ma != null)
            {
                // Timer after the track is played
                new java.util.Timer().schedule(new TimerTask(){
                    @Override
                    public void run() {
                        Log.i("BLINDTEST","Showing the answer for this question");

                        ma.stopTracks();
                        ma.runOnUiThread(() -> {
                            textView.setText(getTitle());

                            // Revealing the image of the track
                            if(imgPath!=null)
                            {
                                if(getApi() != ApiType.None)
                                {
                                    // Online image
                                    Picasso.with(frag.getContext())
                                            .load(imgPath)
                                            .fit()
                                            .placeholder(R.drawable.ic_launcher_background)
                                            .into(trackCoverImgView);
                                }
                                else
                                {
                                    // Local image
                                    Picasso.with(frag.getContext())
                                            .load(new File(imgPath))
                                            .fit()
                                            .placeholder(R.drawable.ic_launcher_background)
                                            .into(trackCoverImgView);
                                }
                            }
                            else
                                trackCoverImgView.setImageResource(R.drawable.ic_music_note);

                            new java.util.Timer().schedule(new TimerTask(){
                                @Override
                                public void run() {
                                    // We tell the listeners that the track finished playing
                                    ma.runOnUiThread(() -> {
                                        for (Button b : buttons)
                                        {
                                            b.setClickable(true);
                                            b.setBackgroundColor(Color.parseColor("#FAFAFA"));
                                            b.refreshDrawableState();
                                        }
                                    });
                                    blindtrackFinished.onEvent();
                                }
                            },1000*finishDurSeconds,999999999);
                        });
                    }

                },1000*playDurSeconds,999999999);
            }
        }

    }

    public void assignAnswers(@NonNull BlindTest blindTest, @NonNull List<BlindTrack> allTracksFromPhone) {

        // Normally set to 1 (the name of the track is there by default)
        int curAnswerCount = possibleAnswers.size();

        // We fetch the tracks that we want to check the titles
        List<String> trackTitles = new ArrayList<>();
        for(AudioTrack t : allTracksFromPhone)
        {
            trackTitles.add(t.getTitle());
        }
        Log.i("BLINDTEST","Assign List : \n"+trackTitles);
        Collections.sort(trackTitles);

        // Assign an answer
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
        Log.i("BLINDTEST","Assigned "+possibleAnswers.size()+" answers for track"  + getTitle());

    }
}
