package com.gillioen.navbarmusiccentral;

import android.os.AsyncTask;
import android.util.Log;

import com.deezer.sdk.model.Track;
import com.deezer.sdk.network.connect.DeezerConnect;
import com.deezer.sdk.network.request.DeezerRequest;
import com.deezer.sdk.network.request.DeezerRequestFactory;
import com.deezer.sdk.network.request.JsonUtils;
import com.deezer.sdk.network.request.event.DeezerError;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DeezerGetTracksTask extends AsyncTask<DeezerConnect,Integer, List<AudioTrack>> {


    @Override
    protected List<AudioTrack> doInBackground(DeezerConnect... deezerConnects) {
        ArrayList<AudioTrack> tracks = new ArrayList<>();
        DeezerRequest request = DeezerRequestFactory.requestCurrentUserPlaylists();
        List<com.deezer.sdk.model.Playlist> deezPlaylists = null;
        try {
            deezPlaylists = (List<com.deezer.sdk.model.Playlist>) JsonUtils.deserializeJson(deezerConnects[0].requestSync(request));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DeezerError deezerError) {
            deezerError.printStackTrace();
        }
        Log.i("DEEZER","Found" + deezPlaylists.size() + " playlists");
        for(com.deezer.sdk.model.Playlist pl : deezPlaylists)
        {
            Playlist p = new Playlist();
            p.name = pl.getTitle();
            p.description = pl.getDescription();
            p.imgURL = pl.getMediumImageUrl();
            DeezerRequest deezerRequest = DeezerRequestFactory.requestPlaylistTracks(pl.getId());
            List<Track> plListTracks = null;
            try {
                plListTracks = (List<Track>) JsonUtils.deserializeJson(deezerConnects[0].requestSync(deezerRequest));
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (DeezerError deezerError) {
                deezerError.printStackTrace();
            }

            Log.i("DEEZER","Found " +  plListTracks.size() + " tracks in playlist : " + pl.getTitle() );
            for(Track t : plListTracks)
            {
                AudioTrack track = new AudioTrack();
                track.api = ApiType.Deezer;
                track.title = t.getTitle();
                track.description = "Deezer track";
                track.artist = t.getArtist().getName();
                track.audioPath = String.valueOf(t.getId());
                track.playListPath =  String.valueOf(pl.getId());
                track.imgPath = t.getArtist().getSmallImageUrl();
                Log.i("DEEZER",track.toString());
                tracks.add(track);
            }
        }
        return tracks;
    }
}
