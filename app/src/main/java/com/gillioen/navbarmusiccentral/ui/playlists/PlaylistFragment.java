package com.gillioen.navbarmusiccentral.ui.playlists;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.gillioen.navbarmusiccentral.ApiType;
import com.gillioen.navbarmusiccentral.AudioTrack;
import com.gillioen.navbarmusiccentral.MainActivity;
import com.gillioen.navbarmusiccentral.Playlist;
import com.gillioen.navbarmusiccentral.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class PlaylistFragment extends Fragment {

    private PlaylistViewModel galleryViewModel;
    GridLayout gridLayout;
    CardView dummyCardView;

    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel = ViewModelProviders.of(this).get(PlaylistViewModel.class);

        View root = inflater.inflate(R.layout.fragment_playlists, container, false);
        final TextView textView = root.findViewById(R.id.text_gallery);
        galleryViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        gridLayout = root.findViewById(R.id.playlistGrid);
        int count = gridLayout.getChildCount();
        CardView child = (CardView)gridLayout.getChildAt(0);
        dummyCardView = child;
        LoadPlaylistLocal();
        try {
            AddAPISPlayLists(gridLayout,((MainActivity) getActivity()).allPlaylists);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return root;
    }

    private void AddAPISPlayLists(GridLayout layout, ArrayList<Playlist> pLists) throws ExecutionException, InterruptedException, JSONException {
        for(Playlist pl : pLists) {
            CardView cv = generateCardViewFromPlaylist(pl);
            layout.addView(cv);
        }
    }
    private void LoadPlaylistLocal() {
        // TODO
    }

    public int POS_IMAGE = 0;
    public int POS_TEXTVIEW = 1;
    private CardView generateCardViewFromPlaylist(Playlist pl)
    {

        CardView cView = (CardView) LayoutInflater.from(getActivity()).inflate(R.layout.playlist_cardview, null);

        // On stocke les données de la playlist
        cView.setTag(pl);
        LinearLayout parentLayout = (LinearLayout) cView.getChildAt(0);
        ImageView img = (ImageView) parentLayout.getChildAt(POS_IMAGE);

        if(pl.imgURL != null )
        {
            Picasso.with(getActivity())
                    .load(pl.imgURL)
                    .fit()
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(img);
        }

        ImageView apiIcon = cView.findViewById(R.id.apiIconCard);

        if(pl.tracks.size() > 0 )
        {
            AudioTrack firstTrack = pl.tracks.get(0);
            if(firstTrack.api == ApiType.Spotify )
                apiIcon.setImageResource(R.drawable.spotify);
            else if (firstTrack.api == ApiType.Deezer)
                apiIcon.setImageResource(R.drawable.deezer);
            else
                apiIcon.setImageResource(0);

            // Playing music on click
            cView.setOnClickListener(v -> {
                MainActivity act = (MainActivity)getActivity();
                try {
                    switch (firstTrack.api)
                    {
                        case Deezer:
                            act.spotifyPlayer.Stop();
                            act.localPlayer.Stop();
                            act.deezerPlayer.PlayPlaylist(pl.url);
                            break;
                        case Spotify:
                            act.deezerPlayer.Stop();
                            act.localPlayer.Stop();
                            act.spotifyPlayer.Play(pl.url);
                            break;
                        case None:
                            act.spotifyPlayer.Stop();
                            act.localPlayer.Stop();
                            act.localPlayer.Play(pl.url);
                            break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        TextView tv =  cView.findViewById(R.id.textViewCard);
        tv.setText(pl.name);

        Log.i("CVIEW","Generated Card view for " + pl.name + "'\'" + pl.tracks.size() + " tracks");
        return  cView;
    }
}