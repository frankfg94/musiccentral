package com.gillioen.navbarmusiccentral;

import java.io.Serializable;
import java.util.ArrayList;

public class Playlist implements Serializable {

    public ArrayList<AudioTrack> tracks = new ArrayList<>();
    public String name;
    public String imgURL;
    public String description;
    public String url;
}
