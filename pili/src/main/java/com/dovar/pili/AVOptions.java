package com.dovar.pili;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by heweizong on 2017/9/29.
 */

public class AVOptions {
    public static final int MEDIA_CODEC_SW_DECODE = 0;
    public static final int MEDIA_CODEC_HW_DECODE = 1;
    public static final int MEDIA_CODEC_AUTO = 2;
    public static final String KEY_BUFFER_TIME = "rtmp_buffer";
    public static final String KEY_START_ON_PREPARED = "start-on-prepared";
    public static final String KEY_PREPARE_TIMEOUT = "timeout";
    public static final String KEY_GET_AV_FRAME_TIMEOUT = "get-av-frame-timeout";
    public static final String KEY_LIVE_STREAMING = "live-streaming";
    public static final String KEY_MEDIACODEC = "mediacodec";
    public static final String KEY_DELAY_OPTIMIZATION = "delay-optimization";
    public static final String KEY_CACHE_BUFFER_DURATION = "cache-buffer-duration";
    public static final String KEY_MAX_CACHE_BUFFER_DURATION = "max-cache-buffer-duration";
    public static final String KEY_RECONNECT = "reconnect";
    public static final String KEY_PROBESIZE = "probesize";
    public static final String KEY_AUDIO_DATA_CB_ENABLE = "audio-data-cb-enable";
    public static final String KEY_VIDEO_DATA_CB_ENABLE = "video-data-cb-enable";
    public static final String KEY_AUDIO_RENDER_MSG = "audio-render-msg-cb";
    public static final String KEY_VIDEO_RENDER_MSG = "video-render-msg-cb";
    public static final String KEY_VIDEO_DISPLAY_DISABLE = "nodisp";
    public static final String KEY_RTMP_LIVE = "rtmp_live";
    /** @deprecated */
    public static final String KEY_FFLAGS = "fflags";
    /** @deprecated */
    public static final String VALUE_FFLAGS_NOBUFFER = "nobuffer";
    private Map<String, Object> mMap = new HashMap();

    public AVOptions() {
    }

    public final boolean containsKey(String var1) {
        return this.mMap.containsKey(var1);
    }

    public final int getInteger(String var1) {
        return ((Integer)this.mMap.get(var1)).intValue();
    }

    public final int getInteger(String var1, int var2) {
        try {
            return this.getInteger(var1);
        } catch (NullPointerException var4) {
            ;
        } catch (ClassCastException var5) {
            ;
        }

        return var2;
    }

    public final long getLong(String var1) {
        return ((Long)this.mMap.get(var1)).longValue();
    }

    public final float getFloat(String var1) {
        return ((Float)this.mMap.get(var1)).floatValue();
    }

    public final String getString(String var1) {
        return (String)this.mMap.get(var1);
    }

    public final void setInteger(String var1, int var2) {
        this.mMap.put(var1, Integer.valueOf(var2));
    }

    public final void setLong(String var1, long var2) {
        this.mMap.put(var1, Long.valueOf(var2));
    }

    public final void setFloat(String var1, float var2) {
        this.mMap.put(var1, Float.valueOf(var2));
    }

    public final void setString(String var1, String var2) {
        this.mMap.put(var1, var2);
    }

    public Map<String, Object> getMap() {
        return this.mMap;
    }
}
