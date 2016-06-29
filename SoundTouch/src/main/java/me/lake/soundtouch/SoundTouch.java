////////////////////////////////////////////////////////////////////////////////
///
/// Example class that invokes native SoundTouch routines through the JNI
/// interface.
///
/// Author        : Copyright (c) Olli Parviainen
/// Author e-mail : oparviai 'at' iki.fi
/// WWW           : http://www.surina.net
///
////////////////////////////////////////////////////////////////////////////////
//
// $Id: SoundTouch.java 211 2015-05-15 00:07:10Z oparviai $
//
////////////////////////////////////////////////////////////////////////////////

package me.lake.soundtouch;

public final class SoundTouch {
    // Native interface function that returns SoundTouch version string.
    // This invokes the native c++ routine defined in "soundtouch-jni.cpp".
    public native final static String getVersionString();

    private native final static void setTempo(long handle, float tempo);

    private native final static void setPitchSemiTones(long handle, float pitch);

    private native final static void setSpeed(long handle, float speed);

    private native final static int setInputConfig(long handle, int sampleRate, int nChannels);

    private native final static int putData(long handle, byte[] audioData, int samples);

    private native final static int getData(long handle, byte[] audioData, int samples);

    public native final static String getErrorString();

    private native final static long newInstance();

    private native final static void deleteInstance(long handle);

    long handle = 0;

    int channel;
    int sampleRate;


    public SoundTouch(int channel, int sampleRate) {
        this.channel = channel;
        this.sampleRate = sampleRate;
    }

    public void create() {
        handle = newInstance();
        setInputConfig(handle, sampleRate, channel);
    }

    public void destroy() {
        deleteInstance(handle);
        handle = 0;
    }

    public int putData(byte[] data, int samples) {
        return putData(handle, data, samples);
    }

    public int getData(byte[] data, int samples) {
        return getData(handle, data, samples);
    }

    public void setTempo(float tempo) {
        setTempo(handle, tempo);
    }


    public void setPitchSemiTones(float pitch) {
        setPitchSemiTones(handle, pitch);
    }


    public void setSpeed(float speed) {
        setSpeed(handle, speed);
    }

    // Load the native library upon startup
    static {
        System.loadLibrary("soundtouch");
    }
}
