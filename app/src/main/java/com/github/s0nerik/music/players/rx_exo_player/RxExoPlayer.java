package com.github.s0nerik.music.players.rx_exo_player;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.exoplayer.ExoPlaybackException;
import com.google.android.exoplayer.ExoPlayer;
import com.google.android.exoplayer.MediaFormat;
import com.google.android.exoplayer.TrackRenderer;

import rx.Observable;
import rx.functions.Action0;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;

import static com.google.android.exoplayer.ExoPlayer.Factory;
import static com.google.android.exoplayer.ExoPlayer.Listener;
import static com.google.android.exoplayer.ExoPlayer.STATE_BUFFERING;
import static com.google.android.exoplayer.ExoPlayer.STATE_ENDED;
import static com.google.android.exoplayer.ExoPlayer.STATE_IDLE;
import static com.google.android.exoplayer.ExoPlayer.STATE_PREPARING;
import static com.google.android.exoplayer.ExoPlayer.STATE_READY;

public abstract class RxExoPlayer {

    private ExoPlayer innerPlayer;
    private TrackRenderer currentRenderer;

    private SerializedSubject<PlayerEvent, PlayerEvent> playerSubject = PublishSubject.<PlayerEvent>create().toSerialized();
    private SerializedSubject<ExoPlaybackException, ExoPlaybackException> errorSubject = PublishSubject.<ExoPlaybackException>create().toSerialized();

    private Observable<PlayerEvent> readyObservable       = eventProvider(PlayerEvent.READY     );
    private Observable<PlayerEvent> preparingObservable   = eventProvider(PlayerEvent.PREPARING );
    private Observable<PlayerEvent> bufferingObservable   = eventProvider(PlayerEvent.BUFFERING );
    private Observable<PlayerEvent> startedObservable     = eventProvider(PlayerEvent.STARTED   );
    private Observable<PlayerEvent> pausedObservable      = eventProvider(PlayerEvent.PAUSED    );
    private Observable<PlayerEvent> endedObservable       = eventProvider(PlayerEvent.ENDED     );
    private Observable<PlayerEvent> idleObservable        = eventProvider(PlayerEvent.IDLE      );

    private int lastState = STATE_IDLE;

    public enum PlayerEvent {
        READY, PREPARING, BUFFERING, STARTED, PAUSED, ENDED, IDLE
    }

    private Observable<PlayerEvent> eventProvider(final PlayerEvent e) {
        return events().filter(new Func1<PlayerEvent, Boolean>() {
            @Override
            public Boolean call(PlayerEvent event) {
                return event == e;
            }
        }).onBackpressureBuffer();
    }

    public Observable<PlayerEvent> events() {
        return playerSubject.asObservable();
    }

    public Observable<PlayerEvent> event(final PlayerEvent e) {
        switch (e) {
            case READY:
                return readyObservable;
            case PREPARING:
                return preparingObservable;
            case BUFFERING:
                return bufferingObservable;
            case STARTED:
                return startedObservable;
            case PAUSED:
                return pausedObservable;
            case ENDED:
                return endedObservable;
            case IDLE:
                return idleObservable;
            default:
                return null;
        }
    }

    public Observable<PlayerEvent> eventOnce(final PlayerEvent e) {
        return event(e).take(1);
    }

    public Observable<ExoPlaybackException> errors() {
        return errorSubject;
    }

    private Listener listener = new Listener() {
        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            switch (playbackState) {
                case STATE_ENDED:
                    playerSubject.onNext(PlayerEvent.ENDED);
                    break;
                case STATE_IDLE:
                    playerSubject.onNext(PlayerEvent.IDLE);
                    break;
                case STATE_PREPARING:
                    playerSubject.onNext(PlayerEvent.PREPARING);
                    break;
                case STATE_BUFFERING:
                    playerSubject.onNext(PlayerEvent.BUFFERING);
                    break;
                case STATE_READY:
                    playerSubject.onNext(PlayerEvent.READY);
                    if (playWhenReady) {
                        playerSubject.onNext(PlayerEvent.STARTED);
                    }
                    break;
            }
            lastState = playbackState;
        }

        @Override
        public void onPlayWhenReadyCommitted() {
            if (!innerPlayer.getPlayWhenReady())
                playerSubject.onNext(PlayerEvent.PAUSED);
        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {
            playerSubject.onError(error);
            errorSubject.onNext(error);
        }
    };

    public RxExoPlayer() {
        innerPlayer = Factory.newInstance(1);
//        innerPlayer = Factory.newInstance 1, 30 * 1000, 60 * 1000
        innerPlayer.addListener(listener);
        innerPlayer.setPlayWhenReady(false);
    }

    // region Player state
    public boolean isPaused() { return !innerPlayer.getPlayWhenReady(); }

    public boolean isPlaying() { return isReady() && !isPaused(); }

    public boolean isReady() { return innerPlayer.getPlaybackState() == STATE_READY; }

    public long getCurrentPosition() { return innerPlayer.getCurrentPosition(); }

    public TrackRenderer getCurrentRenderer() {
        return currentRenderer;
    }
    // endregion

    protected abstract TrackRenderer getRenderer(Uri uri);

    //region Main functionality
    public Observable<PlayerEvent> start() {
        return Observable.defer(new Func0<Observable<PlayerEvent>>() {
            @Override
            public Observable<PlayerEvent> call() {
                if (innerPlayer.getPlayWhenReady()) {
                    return Observable.just(PlayerEvent.STARTED);
                } else {
                    return eventOnce(PlayerEvent.STARTED)
                            .doOnSubscribe(new Action0() {
                                @Override
                                public void call() {
                                    innerPlayer.setPlayWhenReady(true);
                                }
                            });
                }
            }
        });
    }

    public Observable<PlayerEvent> restart() {
        return seekTo(0)
                .concatMap(new Func1<PlayerEvent, Observable<PlayerEvent>>() {
                    @Override
                    public Observable<PlayerEvent> call(PlayerEvent event) {
                        return start();
                    }
                });
    }

    public Observable<PlayerEvent> pause() {
        return Observable.defer(new Func0<Observable<PlayerEvent>>() {
            @Override
            public Observable<PlayerEvent> call() {
                if (!innerPlayer.getPlayWhenReady()) {
                    return Observable.just(PlayerEvent.PAUSED);
                } else {
                    return eventOnce(PlayerEvent.PAUSED)
                            .doOnSubscribe(new Action0() {
                                @Override
                                public void call() {
                                    innerPlayer.setPlayWhenReady(false);
                                }
                            });
                }
            }
        });
    }

    public Observable<PlayerEvent> setPaused(final boolean flag) {
        return Observable.defer(new Func0<Observable<PlayerEvent>>() {
            @Override
            public Observable<PlayerEvent> call() {
                return flag ? pause() : start();
            }
        });
    }

    public Observable<PlayerEvent> togglePause() {
        return Observable.defer(new Func0<Observable<PlayerEvent>>() {
            @Override
            public Observable<PlayerEvent> call() {
                return setPaused(!isPaused());
            }
        });
    }

    public Observable<PlayerEvent> stop() {
        return Observable.defer(new Func0<Observable<PlayerEvent>>() {
            @Override
            public Observable<PlayerEvent> call() {
                if (innerPlayer.getPlaybackState() == STATE_IDLE) {
                    return Observable.just(PlayerEvent.IDLE);
                } else {
                    return eventOnce(PlayerEvent.IDLE)
                            .doOnSubscribe(new Action0() {
                                @Override
                                public void call() {
                                    innerPlayer.stop();
                                }
                            });
                }
            }
        });
    }

    public Observable<PlayerEvent> prepare(@NonNull final Uri uri) {
        return eventOnce(PlayerEvent.PREPARING)
                .concatMap(new Func1<PlayerEvent, Observable<PlayerEvent>>() {
                    @Override
                    public Observable<PlayerEvent> call(PlayerEvent event) {
                        return eventOnce(PlayerEvent.READY);
                    }
                })
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        currentRenderer = getRenderer(uri);
                        innerPlayer.prepare(currentRenderer);
                    }
                });
    }

    public Observable<PlayerEvent> seekTo(final long msec) {
        return Observable.defer(new Func0<Observable<PlayerEvent>>() {
            @Override
            public Observable<PlayerEvent> call() {
                if (lastState == STATE_IDLE) {
                    return Observable.just(PlayerEvent.IDLE);
                } else if (lastState == STATE_PREPARING) {
                    return Observable.just(PlayerEvent.PREPARING);
                } else if (innerPlayer.getCurrentPosition() == msec) {
                    return Observable.just(PlayerEvent.READY);
                } else {
                    return eventOnce(PlayerEvent.BUFFERING)
                            .concatMap(new Func1<PlayerEvent, Observable<PlayerEvent>>() {
                                @Override
                                public Observable<PlayerEvent> call(PlayerEvent event) {
                                    return eventOnce(PlayerEvent.READY);
                                }
                            })
                            .doOnSubscribe(new Action0() {
                                @Override
                                public void call() {
                                    innerPlayer.seekTo(msec);
                                }
                            });
                }
            }
        });
    }

    public Observable<PlayerEvent> reset() {
        return pause()
                .concatMap(new Func1<PlayerEvent, Observable<PlayerEvent>>() {
                    @Override
                    public Observable<PlayerEvent> call(PlayerEvent event) {
                        return seekTo(0);
                    }
                })
                .concatMap(new Func1<PlayerEvent, Observable<PlayerEvent>>() {
                    @Override
                    public Observable<PlayerEvent> call(PlayerEvent event) {
                        return stop();
                    }
                });
    }
    //endregion

    // region Delegated methods

    public void blockingSendMessage(ExoPlayer.ExoPlayerComponent target, int messageType, Object message) {
        innerPlayer.blockingSendMessage(target, messageType, message);
    }

    public long getDuration() {
        return innerPlayer.getDuration();
    }

    public long getBufferedPosition() {
        return innerPlayer.getBufferedPosition();
    }

    public void setSelectedTrack(int rendererIndex, int trackIndex) {
        innerPlayer.setSelectedTrack(rendererIndex, trackIndex);
    }

    public int getSelectedTrack(int rendererIndex) {
        return innerPlayer.getSelectedTrack(rendererIndex);
    }

    public int getBufferedPercentage() {
        return innerPlayer.getBufferedPercentage();
    }

    public void release() {
        innerPlayer.release();
    }

    public int getPlaybackState() {
        return innerPlayer.getPlaybackState();
    }

    public int getTrackCount(int rendererIndex) {
        return innerPlayer.getTrackCount(rendererIndex);
    }

    public MediaFormat getTrackFormat(int rendererIndex, int trackIndex) {
        return innerPlayer.getTrackFormat(rendererIndex, trackIndex);
    }

    public void sendMessage(ExoPlayer.ExoPlayerComponent target, int messageType, Object message) {
        innerPlayer.sendMessage(target, messageType, message);
    }

    // endregion

}