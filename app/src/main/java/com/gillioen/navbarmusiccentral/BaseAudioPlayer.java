package com.gillioen.navbarmusiccentral;

import java.io.IOException;
import java.io.Serializable;

public interface BaseAudioPlayer extends Serializable {
    public void Play(String path) throws IOException;
    public void Pause();
    public void Stop();
}
