package com.example.aparn.bakingapp.Fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aparn.bakingapp.DetailActivity;
import com.example.aparn.bakingapp.Model.RecipeSteps;
import com.example.aparn.bakingapp.R;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InstructionFragment extends Fragment {

    private SimpleExoPlayer simpleExoPlayer;
    @BindView(R.id.video_pv)
    PlayerView PlayerView;
    @BindView(R.id.thumbnail_frame)
    ImageView ThumbnailView;
    @BindView(R.id.instruction_text)
    TextView InstructionText;
    @BindView(R.id.navigation_frame)
    FrameLayout NavigationLayout;
    @BindView(R.id.prev_step_button)
    Button PreviousButton;
    @BindView(R.id.next_step_button)
    Button NextButton;
    @BindView(R.id.current_step_text)
    TextView CurrentStepText;

    private DetailActivity detailActivity;
    private int pos;
    private long LastPlayerPos;
    private boolean LastPlayerState;

    public InstructionFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.instructions, container, false);
        ButterKnife.bind(this, view);
        detailActivity = (DetailActivity) getActivity();
        pos = detailActivity.getCurrentPosition();
        String instructionString = detailActivity.getRecipeStepList().get(pos).getDescription();
        InstructionText.setText(instructionString);
        setThumbnailView(fetchThumbnailLink());
        initializePlayer(fetchVideoLink());
        setListeners();
        updateViews();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (simpleExoPlayer == null) {
            initializePlayer(fetchVideoLink());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (simpleExoPlayer == null) {
            initializePlayer(fetchVideoLink());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        releaseExoPlayer();
    }

    @Override
    public void onStop() {
        super.onStop();
        releaseExoPlayer();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(getString(R.string.position_value), pos);
        if (simpleExoPlayer != null) {
            outState.putLong(getString(R.string.player_position), simpleExoPlayer.getCurrentPosition());
            outState.putBoolean(getString(R.string.player_play_state),
                    simpleExoPlayer.getPlayWhenReady());
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            pos = savedInstanceState.getInt(getString(R.string.position_value));
            LastPlayerPos = savedInstanceState.getLong(getString(R.string.player_position));
            LastPlayerState = savedInstanceState.
                    getBoolean(getString(R.string.player_play_state));
            onPositionChanged(pos);
        }
    }

    public void setListeners() {
        PreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pos > 0) {
                    pos--;
                }
                updateViews();
                resetPlayerState();
                onPositionChanged(pos);
            }
        });

        NextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pos < detailActivity.getRecipeStepList().size() - 1) {
                    pos++;
                }
                updateViews();
                resetPlayerState();
                onPositionChanged(pos);
            }
        });
    }

    public void setThumbnailView(String thumbnailUrl) {
        Picasso.get()
                .load(Uri.parse(thumbnailUrl))
                .placeholder(R.drawable.ic_cake_black_24dp)
                .error(R.drawable.ic_cake_black_24dp)
                .into(ThumbnailView);
    }

    public void releaseExoPlayer() {
        if (simpleExoPlayer != null)
            simpleExoPlayer.release();
    }

    public void resetPlayerState() {
        LastPlayerPos = 0;
        LastPlayerState = false;
    }

    public void onPositionChanged(int newPosition) {
        pos = newPosition;
        releaseExoPlayer();
        initializePlayer(fetchVideoLink());
        simpleExoPlayer.seekTo(LastPlayerPos);
        simpleExoPlayer.setPlayWhenReady(LastPlayerState);
        if (pos > -1 && pos < detailActivity.getRecipeStepList().size()) {
            RecipeSteps newRecipeStep = detailActivity.getRecipeStepList().get(pos);
            InstructionText.setText(newRecipeStep.getDescription());
        }
        updateViews();
    }

    public void updateViews() {
        String currentPositionText = "Step " + Integer.toString(pos);
        CurrentStepText.setText(currentPositionText);
        if (pos == 0) {
            PreviousButton.setVisibility(View.INVISIBLE);
            NextButton.setVisibility(View.VISIBLE);
        } else if (pos == detailActivity.getRecipeStepList().size() - 1) {
            PreviousButton.setVisibility(View.VISIBLE);
            NextButton.setVisibility(View.INVISIBLE);
        } else {
            PreviousButton.setVisibility(View.VISIBLE);
            NextButton.setVisibility(View.VISIBLE);
        }
    }

    public String fetchVideoLink() {
        RecipeSteps currentStep = detailActivity.getRecipeStepList().get(pos);
        String videoUrl = currentStep.getVideoUrl();
        if (TextUtils.isEmpty(videoUrl)) {
            videoUrl = currentStep.getThumbnailUrl();
        }
        return videoUrl;
    }

    public String fetchThumbnailLink() {
        RecipeSteps currentStep = detailActivity.getRecipeStepList().get(pos);
        return currentStep.getThumbnailUrl();
    }

    public void initializePlayer(String videoUrl) {
        if (TextUtils.isEmpty(videoUrl)) {
            PlayerView.setVisibility(View.GONE);
            ThumbnailView.setVisibility(View.VISIBLE);
        } else {
            ThumbnailView.setVisibility(View.GONE);
            PlayerView.setVisibility(View.VISIBLE);
            Uri videoUri = Uri.parse(videoUrl);
            DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
            TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
            PlayerView.setPlayer(simpleExoPlayer);
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(), Util.getUserAgent(getContext(), getString(R.string.app_name)), bandwidthMeter);
            MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(videoUri);
            simpleExoPlayer.prepare(mediaSource);
        }
    }
}
