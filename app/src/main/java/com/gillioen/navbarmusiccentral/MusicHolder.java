package com.gillioen.navbarmusiccentral;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.gillioen.navbarmusiccentral.players.BaseAudioPlayer;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.io.IOException;

public class MusicHolder extends RecyclerView.ViewHolder {

    @Nullable
    private TextView tviewTitle = null;
    @Nullable
    private TextView tviewAuthor = null;
    @Nullable
    private ImageView apiIcon = null;
    @Nullable
    private ImageView imageView = null;
    @Nullable
    LinearLayoutCompat myElement = null;

    @Nullable
    private AudioTrack currentTrack = null;
    @Nullable
    private BaseAudioPlayer localPlayer = null;
    @Nullable
    private BaseAudioPlayer deezerPlayer = null;
    @Nullable
    private BaseAudioPlayer spotifyPlayer = null;
    @Nullable
    private Boolean isSpotifyPremium = null;

    public MusicHolder(@NonNull View itemView, BaseAudioPlayer localPlayer, BaseAudioPlayer deezerPlayer, Boolean isSpotifyPremium, BaseAudioPlayer spotifyPlayer, Context context) {
        super(itemView);
        tviewTitle = itemView.findViewById(R.id.tviewTitle);
        tviewAuthor = itemView.findViewById(R.id.tviewAuthor);
        imageView = itemView.findViewById(R.id.imageView);
        apiIcon = itemView.findViewById(R.id.apiIcon);
        myElement = itemView.findViewById(R.id.myElement);

        this.localPlayer = localPlayer;
        this.deezerPlayer = deezerPlayer;
        this.isSpotifyPremium = isSpotifyPremium;
        this.spotifyPlayer = spotifyPlayer;
    }

    public void display(@NonNull AudioTrack track, Context c, int pos) {
        currentTrack = track;
        tviewTitle.setText(currentTrack.getTitle());
        tviewAuthor.setText(currentTrack.getArtist());

        if(currentTrack.imgPath!=null) {

            // Online image fetch
            if(currentTrack.api != ApiType.None)
            {
                Picasso.with(c)
                        .load(currentTrack.imgPath)
                        .fit()
                        .placeholder(R.drawable.ic_action_audiotrack)
                        .into(imageView);
            }
            // Offline image fetch
            else
            {
                File f = new File(currentTrack.imgPath);
                Picasso.with(c)
                        .load(f)
                        .fit()
                        .placeholder(R.drawable.ic_action_audiotrack)
                        .into(imageView);
            }
        }
        else
        {
            Picasso.with(c)
                    .load(R.drawable.ic_action_audiotrack)
                    .fit()
                    .placeholder(R.drawable.ic_action_audiotrack)
                    .into(imageView);
        }

        if(currentTrack.api == ApiType.Spotify )
            apiIcon.setImageResource(R.drawable.spotify);
        else if (currentTrack.api == ApiType.Deezer)
            apiIcon.setImageResource(R.drawable.deezer);
        else
            apiIcon.setImageResource(0);

        myElement.setOnClickListener(v -> {

            try {
                MainActivity.curTrackIndex = pos;
            }
            catch (Exception ex)
            {
                Log.d("AUDIO",ex.getMessage() + " \n" + ex.getStackTrace());
                MainActivity.curTrackIndex = -1;
            }

            if(localPlayer != null)
                localPlayer.Stop();
            if(spotifyPlayer != null)
                spotifyPlayer.Stop();
            if(deezerPlayer != null)
                deezerPlayer.Stop();
            MainActivity.syncWithService(track,false,c);
            try {
                switch (track.api)
                {
                    case None:
                        localPlayer.Play(track.audioPath);
                        break;
                    case Spotify:
                        if(isSpotifyPremium)
                            spotifyPlayer.Play(track.audioPath);
                        else
                            spotifyPlayer.Play("spotify:playlist:"+track.playListPath);
                        break;
                    case Deezer:
                        deezerPlayer.Play(track.audioPath);
                        break;
                }
                MainActivity.audiobarIconModePlay = true;
            } catch (IOException e) {
                Log.d("EXCEPTION",e.getMessage());
                e.printStackTrace();
            }
        });
    }
}

