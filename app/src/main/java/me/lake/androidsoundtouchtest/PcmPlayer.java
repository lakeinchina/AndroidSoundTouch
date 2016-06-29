package me.lake.androidsoundtouchtest;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

/**
 * Created by lake on 22/06/16.
 */
public class PcmPlayer {
    private AudioTrack trackplayer;

    PcmPlayer() {

    }

    public void prepare() {
        int bufsize = AudioTrack.getMinBufferSize(44100, AudioFormat.CHANNEL_CONFIGURATION_MONO,
                AudioFormat.ENCODING_PCM_16BIT);

        trackplayer = new AudioTrack(AudioManager.STREAM_MUSIC,
                44100,
                AudioFormat.CHANNEL_CONFIGURATION_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufsize,
                AudioTrack.MODE_STREAM);
    }

    public void write(byte[] data) {
        trackplayer.write(data, 0, data.length);
    }

    public void start() {
        trackplayer.play();
    }

    public void stop() {
        trackplayer.stop();
    }

    public void destroy() {
        trackplayer.release();
    }
}
