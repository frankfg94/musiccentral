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
import android.widget.Toast;

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

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;
    GridLayout gridLayout;
    CardView dummyCardView;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);

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
        addListeners(gridLayout);
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
                    Toast.makeText(getActivity().getApplicationContext(),text,Toast.LENGTH_SHORT).show();
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

    private void AddAPISPlayLists(GridLayout layout, ArrayList<Playlist> pLists) throws ExecutionException, InterruptedException, JSONException {
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
        }


        TextView tv = (TextView) parentLayout.getChildAt(POS_TEXTVIEW);
        tv.setText(pl.name);
        Log.i("CVIEW","Generated Card view for " + pl.name + "'\'" + pl.tracks.size() + " tracks");
        return  cView;
    }
}