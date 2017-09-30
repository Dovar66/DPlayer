package com.dovar.pili.qos;

/**
 * Created by heweizong on 2017/9/29.
 */

public class StreamParam {
    private int t = 3000;
    public long beginAt;
    public long endAt;
    public long bufferingTimes;
    public int videoSourceFps;
    public int dropVideoFrames;
    public int audioSourceFps;
    public int audioDropFrames;
    public int videoRenderFps;
    public int audioRenderFps;
    public int videoBufferTime;
    public int audioBufferTime;
    public long videoBitrate;
    public long audioBitrate;
    public long firstVideoRenderedTime;
    public long o;
    public long p;
    public long totalRecvBytes;
    public String format1 = "ffmpeg";
    public String format2 = "ffmpeg";

    public StreamParam() {
    }

    public void reset() {
        this.videoSourceFps = 0;
        this.audioSourceFps = 0;
        this.videoRenderFps = 0;
        this.audioRenderFps = 0;
        this.bufferingTimes = 0L;
    }
}
