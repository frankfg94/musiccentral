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

public class DeezerGetPlaylistsTask extends AsyncTask<DeezerConnect,Integer, List<Playlist>> {


    @Override
    protected List<Playlist> doInBackground(DeezerConnect... deezerConnects) {
        DeezerRequest request = DeezerRequestFactory.requestCurrentUserPlaylists();
        List<Track> plListTracks = new ArrayList<>();
        List<com.deezer.sdk.model.Playlist> deezPlaylists = null;
        List<Playlist> deezLocalPlaylists = new ArrayList<>();
        try {
            deezPlaylists = (List<com.deezer.sdk.model.Playlist>) JsonUtils.deserializeJson(deezerConnects[0].requestSync(request));

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DeezerError deezerError) {
            deezerError.printStackTrace();
        }
        Log.d("DEEZER","Found" + deezPlaylists.size() + " playlists");
        for(com.deezer.sdk.model.Playlist pl : deezPlaylists)
        {
            ArrayList<AudioTrack> tracksForCurPlaylist = new ArrayList<>();
            Playlist p = new Playlist();
            p.name = pl.getTitle();
            p.description = pl.getDescription();
            p.imgURL = pl.getMediumImageUrl();
            p.url = String.valueOf(pl.getId());
            DeezerRequest deezerRequest = DeezerRequestFactory.requestPlaylistTracks(pl.getId());
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
                track.setDate(t.getReleaseDate());
                track.audioPath = String.valueOf(t.getId());
                track.playListPath =  String.valueOf(pl.getId());
                track.imgPath = t.getArtist().getSmallImageUrl();

                Log.i("DEEZER",track.toString());
                tracksForCurPlaylist.add(track);
            }
            p.tracks = tracksForCurPlaylist;
            deezLocalPlaylists.add(p);
        }
        return deezLocalPlaylists;
    }
}
