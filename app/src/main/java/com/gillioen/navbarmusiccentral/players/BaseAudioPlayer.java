package com.gillioen.navbarmusiccentral.players;

import java.io.IOException;
import java.io.Serializable;

public interface BaseAudioPlayer extends Serializable {
    void Play(String path) throws IOException;
    void Pause();
    void Stop();
}
