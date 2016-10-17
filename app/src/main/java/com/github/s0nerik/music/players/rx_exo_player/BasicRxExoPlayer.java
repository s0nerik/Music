package com.github.s0nerik.music.players.rx_exo_player;

import android.content.Context;
import android.net.Uri;

import com.google.android.exoplayer.MediaCodecAudioTrackRenderer;
import com.google.android.exoplayer.MediaCodecSelector;
import com.google.android.exoplayer.TrackRenderer;
import com.google.android.exoplayer.extractor.ExtractorSampleSource;
import com.google.android.exoplayer.upstream.Allocator;
import com.google.android.exoplayer.upstream.DefaultAllocator;
import com.google.android.exoplayer.upstream.DefaultUriDataSource;

public class BasicRxExoPlayer extends RxExoPlayer {

    private final Context context;
    private int bufferSegmentSize = 64 * 1024;
    private int bufferSegmentCount = 256;

    public BasicRxExoPlayer(Context context) {
        this.context = context;
    }

    public BasicRxExoPlayer(Context context, int bufferSegmentSize, int bufferSegmentCount) {
        this.context = context;
        this.bufferSegmentSize = bufferSegmentSize;
        this.bufferSegmentCount = bufferSegmentCount;
    }

    @Override
    protected TrackRenderer getRenderer(Uri uri) {
        Allocator allocator = new DefaultAllocator(bufferSegmentSize);
        return new MediaCodecAudioTrackRenderer(
                new ExtractorSampleSource(uri,
                                          new DefaultUriDataSource(context, "RxExoPlayer"),
                                          allocator,
                                          bufferSegmentSize * bufferSegmentCount
                ), MediaCodecSelector.DEFAULT
        );
    }
}
