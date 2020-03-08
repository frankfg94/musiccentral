package com.gillioen.navbarmusiccentral;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.squareup.picasso.Picasso;

import java.io.IOException;

public class MusicHolder extends RecyclerView.ViewHolder {

    private TextView tviewTitle = null;
    private TextView tviewAuthor = null;
    private ImageView apiIcon = null;
    private ImageView imageView = null;
    LinearLayoutCompat myElement = null;

    private AudioTrack currentTrack = null;
    private BaseAudioPlayer localPlayer = null;
    private BaseAudioPlayer currentPlayer = null;
    private BaseAudioPlayer deezerPlayer = null;
    private BaseAudioPlayer spotifyPlayer = null;
    private Boolean isSpotifyPremium = null;

    public MusicHolder(@NonNull View itemView, BaseAudioPlayer localPlayer, BaseAudioPlayer currentPlayer, BaseAudioPlayer deezerPlayer, Boolean isSpotifyPremium, BaseAudioPlayer spotifyPlayer, Context context) {
        super(itemView);
        tviewTitle = itemView.findViewById(R.id.tviewTitle);
        tviewAuthor = itemView.findViewById(R.id.tviewAuthor);
        imageView = itemView.findViewById(R.id.imageView);
        apiIcon = itemView.findViewById(R.id.apiIcon);
        myElement = itemView.findViewById(R.id.myElement);

        this.localPlayer = localPlayer;
        this.currentPlayer = currentPlayer;
        this.deezerPlayer = deezerPlayer;
        this.isSpotifyPremium = isSpotifyPremium;
        this.spotifyPlayer = spotifyPlayer;
    }

    public void display(AudioTrack track, Context c) {
        currentTrack = track;
        tviewTitle.setText(currentTrack.getTitle());
        tviewAuthor.setText(currentTrack.getArtist());

        if(currentTrack.imgPath!=null)
            Picasso.with(c)
                    .load(currentTrack.imgPath)
                    .fit()
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(imageView);
        else
            imageView.setImageResource(R.drawable.ic_launcher_background);

        if(currentTrack.api == ApiType.Spotify )
            apiIcon.setImageResource(R.drawable.spotify);
        else if (currentTrack.api == ApiType.Deezer)
            apiIcon.setImageResource(R.drawable.deezer);
        else
            apiIcon.setImageResource(0);

        myElement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                localPlayer.Stop();
                spotifyPlayer.Stop();
                if(deezerPlayer != null)
                    deezerPlayer.Stop();
                try {
                    switch (currentTrack.api)
                    {
                        case None:
                            currentPlayer = localPlayer;
                            localPlayer.Play(currentTrack.audioPath);
                            Log.i("Play Spot", "YES");
                            break;
                        case Spotify:
                            if(isSpotifyPremium)
                                spotifyPlayer.Play(currentTrack.audioPath);
                            else
                                spotifyPlayer.Play("spotify:playlist:"+currentTrack.playListPath);
                            break;
                        case Deezer:
                            deezerPlayer.Play(currentTrack.audioPath);
                            break;
                    }

                    // NotificationAudioBar.createNotification(,track,R.drawable.ic_music_note,1,1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });
    }
}

