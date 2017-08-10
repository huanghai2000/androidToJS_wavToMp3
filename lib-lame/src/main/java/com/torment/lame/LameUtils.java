package com.torment.lame;

/**
 * Created by wangz on 2017/7/27.
 */

public class LameUtils {

    static {
        System.loadLibrary("lamemp3");
    }

    public native void initLame(int inSamplerate, int outSamplerate, int channels);

    public native void wavTomp3(String wavPath, String mp3Path);

    public native void closeLame();
}
