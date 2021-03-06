package io.poundcode.spotifystreamer.player.view;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import io.poundcode.spotifystreamer.Constants;
import io.poundcode.spotifystreamer.R;
import io.poundcode.spotifystreamer.model.SpotifyTrack;
import io.poundcode.spotifystreamer.player.presenter.SpotifyPlayerPresenter;

public class SpotifyPlayerDialogFragment extends DialogFragment implements SpotifyPlayerView, SeekBar.OnSeekBarChangeListener {

    @InjectView(R.id.track_image)
    ImageView mTrackImage;
    @InjectView(R.id.track)
    TextView mTrackName;
    @InjectView(R.id.artist)
    TextView mArtistName;
    @InjectView(R.id.seek_bar)
    SeekBar mSeekBar;
    @InjectView(R.id.play_pause_track)
    ImageButton mPlayPauseTrack;
    @InjectView(R.id.trackTime)
    TextView mTrackTime;
    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    private SpotifyPlayerPresenter mPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_track_player, container, false);
        ButterKnife.inject(this, view);
        if (getResources().getBoolean(R.bool.isLargeLayout)) {
            mToolbar.setVisibility(View.GONE);
        }
        mSeekBar.setMax(Constants.SAMPLE_TRACK_LENGTH);
        mSeekBar.setOnSeekBarChangeListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() instanceof SpotifyPlayerPresenter) {
            mPresenter = (SpotifyPlayerPresenter) getActivity();
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        return dialog;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (getActivity() != null && !getActivity().isFinishing()) {
            getActivity().onBackPressed();
        }
    }

    @Override
    public void updateTrackPlaying(SpotifyTrack track) {
        Picasso.with(getActivity())
            .load(track.getTrackImageUrlAsUri())
            .placeholder(R.drawable.spotify_logo)
            .error(R.drawable.spotify_logo)
            .into(mTrackImage);
        mArtistName.setText(track.artistName);
        mTrackName.setText(track.trackName);
        mTrackTime.setText(":0");
        mToolbar.setTitle(track.trackName);
        mSeekBar.setProgress(0);
    }

    @Override
    public void updateIsPlaying(boolean isPaused) {
        if (isPaused) {
            mPlayPauseTrack.setImageResource(R.drawable.ic_pause);
        } else {
            mPlayPauseTrack.setImageResource(R.drawable.ic_play);
        }
    }

    @Override
    public void updateSeekBar(int position) {
        mSeekBar.setProgress(position);
        mTrackTime.setText(":" + position);
    }

    @Override
    @OnClick(R.id.next_track)
    public void onNextClicked() {
        mPresenter.playNextTrack();
    }

    @Override
    @OnClick(R.id.previous_track)
    public void onPreviousClicked() {
        mPresenter.playPreviousTrack();
    }

    @Override
    @OnClick(R.id.play_pause_track)
    public void onPausePlayClicked() {
        mPresenter.playOrPause();
    }

    @Override
    public void onSeekStopped(int position) {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            mTrackTime.setText(":" + progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        //do nothing
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int position = seekBar.getProgress();
        mPresenter.seek(position);
    }


}
