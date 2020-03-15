package com.gillioen.navbarmusiccentral.players;

import android.util.Log;

import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.types.Track;

public class SpotifyPlayer implements BaseAudioPlayer {

    public final SpotifyAppRemote remote;

    public SpotifyPlayer(SpotifyAppRemote remote)
    {
        this.remote =  remote;

        remote.getPlayerApi()
                .subscribeToPlayerState()
                .setEventCallback(playerState -> {
                    final Track track = playerState.track;
                    if (track != null) {
                        Log.d("MainActivity", track.name + " by " + track.artist.name);
                    }
                });
    }

    @Override
    public void Play(String path) {
            remote.getPlayerApi().play(path);
    }

    @Override
    public void Pause() {
        remote.getPlayerApi().pause();
    }

    @Override
    public void Stop() {
        remote.getPlayerApi().pause();
        remote.getPlayerApi().seekTo(0);
    }

    public void Disconnect(){
        SpotifyAppRemote.disconnect(remote);
    }
}
