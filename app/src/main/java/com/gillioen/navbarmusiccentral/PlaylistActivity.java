package com.gillioen.navbarmusiccentral;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class PlaylistActivity extends AppCompatActivity {

    private LocalPlayer playerLocal;
    //private static BaseAudioPlayer playerSpotify;
    private SpotifyAppRemote remote;
    private String spotifyToken;
    private ArrayList<Playlist> playlists;
    GridLayout gridLayout;
    CardView dummyCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);


        gridLayout = findViewById(R.id.playlistGrid);
        int count = gridLayout.getChildCount();
            CardView child = (CardView)gridLayout.getChildAt(0);
        dummyCardView = child;

        playerLocal = new LocalPlayer();
        //playerSpotify = MainActivity.spotifyPlayer;
        spotifyToken = getIntent().getStringExtra("spotifyToken");
        playlists = (ArrayList<Playlist>) getIntent().getSerializableExtra("allPlaylists");


        LoadPlaylistLocal();
        try {
            AddSpotifyPlayLists(gridLayout,playlists);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        addListeners(gridLayout);
    }

    private void addListeners(GridLayout gridLayout) {
        for(int i = 0; i<gridLayout.getChildCount();i++){
            CardView cardView=(CardView)gridLayout.getChildAt(i);
            final int finalI= i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Playlist pl = (Playlist)view.getTag();
                    int trackCount =  pl.tracks.size();
                    String text = "Nombre de morceaux :  " +trackCount;
                    Toast.makeText(getApplicationContext(),text,Toast.LENGTH_SHORT).show();
                    try {

                        //Remplacer par parselable
                        MainActivity.spotifyPlayer.Play(pl.url);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void AddSpotifyPlayLists(GridLayout layout,ArrayList<Playlist> pLists) throws ExecutionException, InterruptedException, JSONException {
        for(Playlist pl : pLists) {
            CardView cv = generateCardViewFromPlaylist(pl);
            layout.addView(cv);
            Log.i("CVIEW","Added Cur Child count : " + layout.getChildCount() );

        }
    }
    private void LoadPlaylistLocal() {
    // TODO
    }


    public int POS_IMAGE = 0;
    public int POS_TEXTVIEW = 1;
    private CardView generateCardViewFromPlaylist(Playlist pl)
    {

        CardView cView = (CardView) LayoutInflater.from(this).inflate(R.layout.playlist_cardview, null);

        // On stocke les données de la playlist
        cView.setTag(pl);
        LinearLayout parentLayout = (LinearLayout) cView.getChildAt(0);
        ImageView img = (ImageView) parentLayout.getChildAt(POS_IMAGE);

        if(pl.imgURL != null )
        {
            Picasso.with(this)
                    .load(pl.imgURL)
                    .fit()
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(img);
        }
       TextView tv = (TextView) parentLayout.getChildAt(POS_TEXTVIEW);
        tv.setText(pl.name);
        Log.i("CVIEW","Generated Card view for " + pl.name + "'\'" + pl.tracks.size() + " tracks");
        return  cView;
    }

}
