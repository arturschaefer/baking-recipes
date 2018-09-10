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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.arturschaefer.bakingapp.model.Step;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
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

import java.net.URI;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepDetailFragment extends Fragment{
    public static final String LOG_TAG = StepDetailFragment.class.getSimpleName();
    private static final String CURRENT_STEP = "current_step";

    @BindView(R.id.video_player_view)
    SimpleExoPlayerView mExoPlayerView;
    @BindView(R.id.pb_buffering)
    ProgressBar mProgressBar;
    @BindView(R.id.tv_step_short_description)
    TextView mStepShortTextView;
    @BindView(R.id.tv_step_description)
    TextView mStepDescriptionTextView;

    BandwidthMeter mBandwidthMeter;
    TrackSelector mTrackSelector;
    private Step mStep;
    private ExoPlayer mExoPlayer;

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
            } else {
                mBandwidthMeter = new DefaultBandwidthMeter();
                mTrackSelector = new DefaultTrackSelector();
                mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity().getApplicationContext(), mTrackSelector);
                Uri uri = Uri.parse(mStep.getmVideoURL());

                DefaultHttpDataSourceFactory defaultHttpDataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");
                ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
                MediaSource mediaSource = new ExtractorMediaSource(uri, defaultHttpDataSourceFactory, extractorsFactory, null, null);

                mExoPlayerView.setPlayer(mExoPlayer);
                mExoPlayer.prepare(mediaSource);
                mExoPlayer.setPlayWhenReady(true);
            }
        } catch (Exception e){
            Log.e(LOG_TAG, e.toString());
        }

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }
}
