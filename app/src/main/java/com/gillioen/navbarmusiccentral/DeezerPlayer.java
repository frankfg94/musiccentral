package com.gillioen.navbarmusiccentral;

import android.app.Application;

import com.deezer.sdk.network.connect.DeezerConnect;
import com.deezer.sdk.network.request.event.DeezerError;
import com.deezer.sdk.player.PlaylistPlayer;
import com.deezer.sdk.player.TrackPlayer;
import com.deezer.sdk.player.exception.TooManyPlayersExceptions;
import com.deezer.sdk.player.networkcheck.NetworkStateCheckerFactory;

import java.io.IOException;

public class DeezerPlayer implements  BaseAudioPlayer{
    private Application app;
    private final DeezerConnect api;
    TrackPlayer tp = null;
    PlaylistPlayer pp = null;
    public DeezerPlayer(Application app, DeezerConnect api) {
        this.app = app;
        this.api = api;
    }

    @Override
    public void Play(String songId) throws IOException {
        try {
             tp = new TrackPlayer(app,api, NetworkStateCheckerFactory.wifiAndMobile());
             tp.playTrack(Long.parseLong(songId));
        } catch (TooManyPlayersExceptions tooManyPlayersExceptions) {
            tooManyPlayersExceptions.printStackTrace();
        } catch (DeezerError deezerError) {
            deezerError.printStackTrace();
        }
    }

    @Override
    public void Pause() {
        tp.pause();
    }

    @Override
    public void Stop() {
        if(tp!=null)
        tp.stop();

        if(pp != null)
            pp.stop();
    }

    public void PlayPlaylist(String audioPath) {
        try {
            pp = new PlaylistPlayer(app,api, NetworkStateCheckerFactory.wifiAndMobile());
            pp.playPlaylist(Long.parseLong(audioPath));
        } catch (TooManyPlayersExceptions tooManyPlayersExceptions) {
            tooManyPlayersExceptions.printStackTrace();
        } catch (DeezerError deezerError) {
            deezerError.printStackTrace();
        }
    }
}
