package com.gillioen.navbarmusiccentral;

import android.os.Parcelable;

import java.io.Serializable;
import java.util.Date;

public class AudioTrack implements Serializable
{
    public String audioPath;
    public String title;
    private Date date;
    public String description;
    public String imgPath;
    public String artist;
    public String genre;
    public String playListPath; // Spotify only
    public ApiType api = ApiType.None;


    public String getAudioPath() {
        return audioPath;
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getPlayListPath() {
        return playListPath;
    }

    public void setPlayListPath(String playListPath) {
        this.playListPath = playListPath;
    }

    public ApiType getApi() {
        return api;
    }

    public void setApi(ApiType api) {
        this.api = api;
    }

    @Override
    public String toString()
    {
        return title+" ("+audioPath+")";
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    // The path of an audiotrack is its identifier
    @Override
    public boolean equals(Object o)
    {
        if( o == this)
            return true;

        if(!(o instanceof AudioTrack))
            return false;

        AudioTrack t = (AudioTrack)o;
        return getAudioPath().equals(t.getAudioPath());
    }
}


