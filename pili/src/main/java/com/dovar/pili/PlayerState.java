package com.dovar.pili;

/**
 * Created by heweizong on 2017/9/29.
 */

public enum  PlayerState {
    IDLE,
    PREPARING,
    PREPARED,
    PLAYING,
    BUFFERING,
    PAUSED,
    COMPLETED,
    ERROR;

    private PlayerState() {
    }
}
