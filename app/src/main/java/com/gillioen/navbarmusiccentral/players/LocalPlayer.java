package com.gillioen.navbarmusiccentral;

import android.media.MediaPlayer;

import java.io.IOException;

public class LocalPlayer implements BaseAudioPlayer {

    MediaPlayer player = new MediaPlayer();
    @Override
    public void Play(String path) throws IOException {
        player.stop();
        player.reset();
        player.setDataSource( "file://"+ path);
        player.prepare();
        player.start();
    }

    @Override
    public void Pause() {
        player.pause();
    }

    @Override
    public void Stop() {
        player.stop();
        player.reset();
    }
}
