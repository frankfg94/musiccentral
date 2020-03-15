package com.gillioen.navbarmusiccentral;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gillioen.navbarmusiccentral.players.BaseAudioPlayer;

import java.util.ArrayList;
import java.util.List;

public class MusicAdapter  extends RecyclerView.Adapter<MusicHolder> {

    private List<AudioTrack> tracks;
    BaseAudioPlayer localPlayer;
    BaseAudioPlayer deezerPlayer;
    BaseAudioPlayer spotifyPlayer;
    private Context c;
    Boolean isSpotifyPremium;

    public MusicAdapter(List<AudioTrack> tracks, BaseAudioPlayer localPlayer, BaseAudioPlayer deezerPlayer, Boolean isSpotifyPremium, BaseAudioPlayer spotifyPlayer, Context c){
        this.tracks = tracks;
        this.localPlayer = localPlayer;
        this.deezerPlayer = deezerPlayer;
        this.isSpotifyPremium = isSpotifyPremium;
        this.spotifyPlayer = spotifyPlayer;
        this.c = c;
    }

    @NonNull
    @Override
    public MusicHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_audiotrack_row, parent,false);
        final MusicHolder musicHolder = new MusicHolder(layoutView, localPlayer, deezerPlayer, isSpotifyPremium, spotifyPlayer, c);
        return musicHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MusicHolder holder, int position) {
        AudioTrack myTrack = tracks.get(position);
        holder.display(myTrack, c, position);
    }

    @Override
    public int getItemCount() {
        return tracks.size();
    }

    public void filterList(ArrayList<AudioTrack> filteredList) {
        tracks = filteredList;
        notifyDataSetChanged(); // We tell the recycler view to update its view
    }


}
