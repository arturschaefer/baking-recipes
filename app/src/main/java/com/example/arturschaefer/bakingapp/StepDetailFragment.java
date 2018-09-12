package com.example.arturschaefer.bakingapp;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.arturschaefer.bakingapp.model.Step;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepDetailFragment extends Fragment implements ExoPlayer.EventListener{
    public static final String LOG_TAG = StepDetailFragment.class.getSimpleName();
    private static final String CURRENT_STEP = "current_step";

    @BindView(R.id.video_player_view)
    SimpleExoPlayerView mExoPlayerView;
    @BindView(R.id.tv_step_short_description)
    TextView mStepShortTextView;
    @BindView(R.id.tv_step_description)
    TextView mStepDescriptionTextView;
    @BindView(R.id.iv_no_video)
    ImageView mImageView;

    private BandwidthMeter mBandwidthMeter;
    private TrackSelector mTrackSelector;
    private Step mStep;
    private ExoPlayer mExoPlayer;
    private Uri mVideoUri;
    private DefaultHttpDataSourceFactory mDefaultHttpDataSourceFactory;
    private ExtractorsFactory mExtractorsFactory;
    private MediaSource mMediaSource;
    private long mVideoPosition;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_steps_details, container, false);
        ButterKnife.bind(this, view);

        try{
            Bundle args = getArguments();
            mStep = args.getParcelable(CURRENT_STEP);
            mStepDescriptionTextView.setText(mStep.getmDescription());
            mStepShortTextView.setText(mStep.getmShortDescription());

            if(mStep.getmVideoURL().isEmpty()){
                mExoPlayerView.setVisibility(View.GONE);
                mImageView.setVisibility(View.VISIBLE);
            } else {
                mImageView.setVisibility(View.GONE);
                mBandwidthMeter = new DefaultBandwidthMeter();
                mTrackSelector = new DefaultTrackSelector();
                mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity().getApplicationContext(), mTrackSelector);
                mVideoUri = Uri.parse(mStep.getmVideoURL());

                mDefaultHttpDataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");
                mExtractorsFactory = new DefaultExtractorsFactory();
                mMediaSource = new ExtractorMediaSource(mVideoUri, mDefaultHttpDataSourceFactory, mExtractorsFactory, null, null);

                mExoPlayerView.setPlayer(mExoPlayer);
                mExoPlayer.prepare(mMediaSource);
                mExoPlayer.setPlayWhenReady(true);
            }
        } catch (Exception e){
            Log.e(LOG_TAG, e.toString());
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onTimelineChanged(Timeline timeline, @Nullable Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == ExoPlayer.STATE_ENDED) {
            mExoPlayer.seekTo(0);
            mExoPlayer.setPlayWhenReady(false);
        }
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }

    private void releasePlayer(){
        if(mExoPlayer != null){
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
        mVideoPosition = 0;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mExoPlayer != null) {
            mVideoPosition = mExoPlayer.getCurrentPosition();
        }
        releasePlayer();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if (mExoPlayer != null){
            releasePlayer();
        }
    }
    @Override
    public void onResume(){
        super.onResume();
        if (!mStep.getmVideoURL().isEmpty()){
            if(mExoPlayer != null){
                mExoPlayer.seekTo(mVideoPosition);
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        releasePlayer();
    }
}
