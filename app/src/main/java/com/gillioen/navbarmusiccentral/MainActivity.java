package com.gillioen.navbarmusiccentral;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import com.deezer.sdk.model.Permissions;
import com.deezer.sdk.network.connect.DeezerConnect;
import com.deezer.sdk.network.connect.event.DialogListener;
import com.deezer.sdk.network.request.event.DeezerError;
import com.gillioen.navbarmusiccentral.BlindTest.BlindTrack;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements ShakeEventManager.ShakeListener {


    private static final int  permissionRequest = 1;

    // Android creds
    private static final String CLIENT_ID_SPOTIFY = "02b11a0a9cfb496d99e345ac42ca6285";
    private static final String CLIENT_ID_DEEZER = "396644";
    private static final String REDIRECT_URI = "https://www.google.fr/";

    // Automatiquement attribué par le SDK spotify
    private String spotifyToken = null;
    private String deezerToken = null;

    public MusicAdapter musicAdapter;
    // Remote
    public SpotifyPlayer spotifyPlayer;
    public LocalPlayer localPlayer;
    public BaseAudioPlayer currentPlayer;
    public DeezerPlayer deezerPlayer;
    private boolean isSpotifyPremium = false;

    public ArrayList<Playlist> allPlaylists = new ArrayList<>();
    public ArrayList<AudioTrack> musicList = new ArrayList<>();

    // ReyclerView
    RecyclerView recycler = null;

    //Sensor
    private SensorManager sm;
    private float acelVal;  //Current acceleration
    private float acelLast; //Last acceleration
    private float shake; //Result

    //Sensor 2
    private ShakeEventManager sd;

    // Request code will be used to verify if result comes from the login activity. Can be set to any integer.
    public static final int REQUEST_CODE = 1334;

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_blindtrack,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        localPlayer = new LocalPlayer();

        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, permissionRequest);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, permissionRequest);
            }
        }
        else
        {
            SpotifyAuthenticateFull(CLIENT_ID_SPOTIFY,REDIRECT_URI);
            DeezerAuthenticateFull(CLIENT_ID_DEEZER);
        }

        sd = new ShakeEventManager();
        sd.setListener(this);
        sd.init(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.preferences: {
                Intent intent = new Intent();
                intent.setClassName(this, "com.gillioen.navbarmusiccentral.Preferences.MyPreferenceActivity");
                startActivity(intent);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    DeezerConnect deezerAPI;
    void DeezerAuthenticateFull(String clientId)
    {
        deezerAPI = new DeezerConnect(this, clientId);
        String[] permissions = new String[] {
                Permissions.BASIC_ACCESS,
                Permissions.MANAGE_LIBRARY,
                Permissions.EMAIL,
                Permissions.MANAGE_LIBRARY,
                Permissions.LISTENING_HISTORY,
                Permissions.OFFLINE_ACCESS};

        DialogListener listener = new DialogListener(){
            public void onComplete(Bundle values) {
                deezerPlayer = new DeezerPlayer(getApplication(),deezerAPI);
                Log.i("DEEZER","Connected " + deezerAPI.getAccessToken() + " / " +  deezerAPI.getCurrentUser().getFirstName() + " " + deezerAPI.getCurrentUser().getLastName());
                doStuff();
            }

            public void onCancel() {
                Log.i("DEEZER","canceled");

            }

            public void onException(Exception e) {
                Log.i("DEEZER","Error " + e.getMessage() + System.lineSeparator() + e.getStackTrace()) ;

            }
        };
        deezerAPI.authorize(this, permissions, listener);
        NotificationAudioBar.startAudioBar(this);
    }


    // L'application doit être enregistrée sur le site de spotify avec son nom de package, son hash et la redirect uri doit être whitelistée
    void SpotifyAuthenticateFull(String clientId,String redirectUri)
    {
        AuthenticationRequest.Builder builder =
                new AuthenticationRequest.Builder(clientId, AuthenticationResponse.Type.TOKEN, redirectUri);

        // On active toutes les autorisations spotify possibles
        builder.setScopes(new String[]{"streaming","ugc-image-upload","user-read-playback-state","user-modify-playback-state",
                "user-read-currently-playing","app-remote-control","user-read-email","user-read-private",
                "playlist-read-collaborative","playlist-modify-public","playlist-read-private","playlist-modify-private",
                "user-library-modify","user-library-read","user-top-read","user-read-recently-played","user-follow-read","user-follow-modify"});
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);

        ConnectionParams connectionParams =
                new ConnectionParams.Builder(CLIENT_ID_SPOTIFY)
                        .setRedirectUri(REDIRECT_URI)
                        .showAuthView(true)
                        .build();

        // Activation du lecteur audioSpotify
        SpotifyAppRemote.connect(this, connectionParams, new Connector.ConnectionListener() {
            @Override
            public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                spotifyPlayer = new SpotifyPlayer(spotifyAppRemote);
                Log.d("MainActivity", "Connected! Yay!");
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.e("MainActivity", throwable.getMessage(), throwable);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    // Handle successful response
                    Log.i("TOKSPOT",response.getAccessToken() +" / Expiration : " + response.getExpiresIn()+ "s");
                    this.spotifyToken = response.getAccessToken();
                    break;

                // Auth flow returned an error
                case ERROR:
                    // Handle error response
                    break;

                // Most likely auth flow was cancelled
                default:
                    // Handle other cases
            }
        }
    }

    public List<AudioTrack> getAllMusicsURISFromDeezer(String accessToken) throws ExecutionException, InterruptedException, JSONException, IOException, DeezerError {
        List<Playlist> pl = new DeezerGetPlaylistsTask().execute(deezerAPI).get();
        List<AudioTrack> tracks = new ArrayList<>();
        allPlaylists.addAll(pl);
        for(Playlist p : pl)
            tracks.addAll(p.tracks);
        return tracks;
    }

    final public static Uri sArtworkUri = Uri
            .parse("content://media/external/audio/albumart");
    public ArrayList<AudioTrack> getAllMusicsPathsFromPhone()
    {


        ArrayList<AudioTrack> songList = new ArrayList<>();
        ContentResolver contentResolver = getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(songUri,null,null,null,null);
        if(songCursor != null && songCursor.moveToFirst())
        {
            int titleIndex = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int artistIndex = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int locationIndex = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            int dateIndex = songCursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED);
            int albumIndex = songCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
            do {
                Integer dateTaken = songCursor.getInt(dateIndex);
                Calendar myCal = Calendar.getInstance();
                myCal.setTimeInMillis(dateTaken);
                Date creationDate = myCal.getTime();

                String curTitle = songCursor.getString(titleIndex);
                String curArtist = songCursor.getString(artistIndex);
                String curLocation = songCursor.getString(locationIndex);
                AudioTrack track = new AudioTrack();
                track.audioPath = curLocation;
                int albumId = songCursor.getInt(albumIndex);
                try {
                    Cursor cursor = getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                            new String[] {MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART},
                            MediaStore.Audio.Albums._ID+ "=?",
                            new String[] {String.valueOf(albumId)},
                            null);

                    if (cursor.moveToFirst()) {
                        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
                        track.imgPath = path;
                    }
                }catch (Exception e)
                {
                    Log.i("ERROR",e.getMessage());
                }

                track.title = curTitle;
                track.artist = curArtist;
                track.setDate(creationDate);
                songList.add(track);
            }while(songCursor.moveToNext());
        }
        return songList;
    }

    public void doStuff(){
        recycler =  findViewById(R.id.recyclerView);
         musicList = getAllMusicsPathsFromPhone();

        try {
            List<AudioTrack> spotifyURIS = getAllMusicsURISFromSpotify();
            musicList.addAll(spotifyURIS);
            List<AudioTrack> deezerURIS = getAllMusicsURISFromDeezer(deezerToken);
            musicList.addAll(deezerURIS);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (DeezerError deezerError) {
            deezerError.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //adapter = new AudioTrackListAdapter(this,R.layout.activity_row,musicList);
        musicAdapter = new MusicAdapter(musicList, localPlayer, currentPlayer, deezerPlayer, isSpotifyPremium, spotifyPlayer, this);
        recycler.setAdapter(musicAdapter);
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));


    }

    public boolean isSpotifyPremium(JSONObject jsonObject)
    {
        try {
            return jsonObject.getString("product").equals("premium");
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    private ArrayList<AudioTrack> getAllMusicsURISFromSpotify() throws ExecutionException, InterruptedException, JSONException, ParseException {

        //Get the URIs with AsyncTask and JSON Objects
        ArrayList<AudioTrack> spotMusics = new ArrayList<>();
        MusicDownloaderTask downloader = new MusicDownloaderTask(spotifyToken);
        AsyncTask<String,Integer,String> playlists  = downloader.execute("https://api.spotify.com/v1/me/playlists");
        MusicDownloaderTask downloaderProfile = new MusicDownloaderTask(spotifyToken);
        AsyncTask<String,Integer,String> profile  = downloaderProfile.execute("https://api.spotify.com/v1/me");
        isSpotifyPremium = isSpotifyPremium(new JSONObject(profile.get()));
        JSONObject playListsJson = new JSONObject(playlists.get());
        JSONArray playListsArray = playListsJson.getJSONArray("items");
        ArrayList<String> spotPlaylistsIDS = new ArrayList<>();
        for( int i = 0 ; i < playListsArray.length(); i++)
            spotPlaylistsIDS.add(playListsArray.getJSONObject(i).getString("id"));


        // ATTENTION IL VA FALLOIR ATTENDRE QU'ON AIT LA LISTE DES PLAYLISTS

        // Les allPlaylists seront transferées à l'activité playlist
        ArrayList<Playlist> playlistsToTransfer = new ArrayList<>();
        int y = 0;
        for( String playListURI : spotPlaylistsIDS)
        {
            Playlist pl = new Playlist();
            // For each playlist we use a GET request , to get all its tracks
            AsyncTask<String,Integer,String> tracks  = new MusicDownloaderTask(spotifyToken).execute("https://api.spotify.com/v1/playlists/"+playListURI+"/tracks");
            JSONObject tracksForCurPlaylistJSON = new JSONObject(tracks.get());
            JSONArray tracksArray = tracksForCurPlaylistJSON.getJSONArray("items");

            // Configuration de la playlist
            pl.name = playListsArray.getJSONObject(y).getString("name");
            pl.description = playListsArray.getJSONObject(y).getString("description");
            if(playListsArray.getJSONObject(y).getJSONArray("images").length() > 0 )
                pl.imgURL = playListsArray.getJSONObject(y).getJSONArray("images").getJSONObject(0).getString("url");

            ArrayList<AudioTrack> tracksForCurPlaylist = new ArrayList<>();
            for(int i = 0 ; i < tracksArray.length(); i++)
            {
                JSONObject curTrackJSON = tracksArray.getJSONObject(i).getJSONObject("track");
                AudioTrack track = new AudioTrack();
                Log.i("JSON",curTrackJSON.toString());
                track.title = curTrackJSON.getString("name") ;
                Log.i("CURTRACK",curTrackJSON.toString());
                JSONObject album = curTrackJSON.getJSONObject("album");
                JSONArray images = album.getJSONArray("images");
                JSONObject firstImg = images.getJSONObject(0);
                String url =   firstImg.getString("url");

                // Pour l'instant que bitmap donc à défaut d'avoir un path, on met une image
                track.imgPath = url;
                track.audioPath = curTrackJSON.getString("uri");
                track.artist = curTrackJSON.getJSONArray("artists").getJSONObject(0).getString("name");
                track.description = "description";
                track.playListPath = playListURI;
                track.api = ApiType.Spotify;
                String releaseDateString = curTrackJSON.getJSONObject("album").getString("release_date");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date  date= simpleDateFormat.parse(releaseDateString);
                track.setDate(date);
                spotMusics.add(track);
                tracksForCurPlaylist.add(track);
                Log.i("SMUSIC",track.toString());
            }

            pl.tracks.addAll(tracksForCurPlaylist);
            pl.url = "spotify:playlist:"+playListURI;
            playlistsToTransfer.add(pl);
            y++;
        }
        this.allPlaylists.addAll(playlistsToTransfer);
        return  spotMusics;

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == permissionRequest) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "Permission accordée", Toast.LENGTH_SHORT).show();
                SpotifyAuthenticateFull(CLIENT_ID_SPOTIFY,REDIRECT_URI);
            } else {
                // Permission Denied
                Toast.makeText(MainActivity.this, "Permission refusée", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onStart(){
        super.onStart();
    }

    @Override
    protected void onStop(){
        super.onStop();
        if(deezerPlayer != null && deezerPlayer.tp!=null)
            deezerPlayer.tp.release();

        if(spotifyPlayer != null && spotifyPlayer.remote != null)
            SpotifyAppRemote.disconnect(((SpotifyPlayer)spotifyPlayer).remote);
    }

    // Audio notification bar
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action  = intent.getExtras().getString("actionname");
            switch (action)
            {
                case NotificationAudioBar.ACTION_PLAY:
                    try {
                        currentPlayer.Play("");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        }
    };

    public void stopTracks()
    {
        localPlayer.Stop();
        spotifyPlayer.Stop();
        deezerPlayer.Stop();
    }

    public void playTrack(AudioTrack track)
    {
        localPlayer.Stop();
        spotifyPlayer.Stop();
        if(deezerPlayer != null)
            deezerPlayer.Stop();
        try {
            switch (track.api)
            {
                case None:
                    currentPlayer = localPlayer;
                    localPlayer.Play(track.audioPath);
                    break;
                case Spotify:
                    if(isSpotifyPremium)
                        spotifyPlayer.Play(track.audioPath);
                    else
                        spotifyPlayer.Play("spotify:playlist:"+track.playListPath);
                    break;
                case Deezer:
                    deezerPlayer.Play(track.audioPath);
                    break;
            }
            // NotificationAudioBar.createNotification(,track,R.drawable.ic_music_note,1,1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onShake() {
        if(musicList != null && musicList.size() > 0)
        {
            Random r = new Random();
            int randomTrack = r.nextInt(musicList.size() - 0 + 1) + 0;
            AudioTrack random = musicList.get(randomTrack);
            playTrack(random);
            Toast.makeText(getApplicationContext(), "Music shuffled!", Toast.LENGTH_SHORT).show();
        }
    }
}
