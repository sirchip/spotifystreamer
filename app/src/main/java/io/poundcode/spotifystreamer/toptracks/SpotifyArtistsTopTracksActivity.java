package io.poundcode.spotifystreamer.toptracks;

import android.os.Bundle;

import io.poundcode.spotifystreamer.Constants;
import io.poundcode.spotifystreamer.R;
import io.poundcode.spotifystreamer.base.SpotifyActivity;
import io.poundcode.spotifystreamer.toptracks.view.SpotifyArtistsTopTracksFragment;

public class SpotifyArtistsTopTracksActivity extends SpotifyActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            String artist = getIntent().getExtras().getString(Constants.ARTIST);
            getFragmentManager()
                .beginTransaction()
                .replace(R.id.toptracks_container, SpotifyArtistsTopTracksFragment.getInstance(artist))
                .commit();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_top_tracks;
    }

    @Override
    public String getViewTitle() {
        return null;
    }

}
