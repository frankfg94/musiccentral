package com.gillioen.navbarmusiccentral;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Playlist implements Serializable {

    public List<AudioTrack> tracks = new ArrayList<>();
    public String name;
    public String imgURL;
    public String description;
    public String url;

}
