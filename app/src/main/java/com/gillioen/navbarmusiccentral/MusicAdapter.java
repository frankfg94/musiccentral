package com.gillioen.navbarmusiccentral;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MusicAdapter  extends RecyclerView.Adapter<MusicHolder> {

    private List<AudioTrack> tracks;
    BaseAudioPlayer localPlayer;
    BaseAudioPlayer currentPlayer;
    BaseAudioPlayer deezerPlayer;
    BaseAudioPlayer spotifyPlayer;
    private Context c;
    Boolean isSpotifyPremium;

    public MusicAdapter(List<AudioTrack> tracks, BaseAudioPlayer localPlayer, BaseAudioPlayer currentPlayer, BaseAudioPlayer deezerPlayer, Boolean isSpotifyPremium, BaseAudioPlayer spotifyPlayer, Context c){
        this.tracks = tracks;
        this.localPlayer = localPlayer;
        this.currentPlayer = currentPlayer;
        this.deezerPlayer = deezerPlayer;
        this.isSpotifyPremium = isSpotifyPremium;
        this.spotifyPlayer = spotifyPlayer;
        this.c = c;
    }

    @NonNull
    @Override
    public MusicHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_row, parent,false);
        final MusicHolder musicHolder = new MusicHolder(layoutView, localPlayer, currentPlayer, deezerPlayer, isSpotifyPremium, spotifyPlayer, c);
        return musicHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MusicHolder holder, int position) {
        AudioTrack myTrack = tracks.get(position);
        holder.display(myTrack, c);
    }

    @Override
    public int getItemCount() {
        Log.i("size", Integer.toString(tracks.size()));
        return tracks.size();
    }

    public void filterList(ArrayList<AudioTrack> filteredList) {
        tracks = filteredList;
        notifyDataSetChanged(); // On affiche les résultats de la recherche immédiatement
    }


}
