package me.lake.androidsoundtouchtest;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

/**
 * Created by lake on 16-5-24.
 */
public class RESAudioClient {
    private final Object syncOp = new Object();
    private AudioRecordThread audioRecordThread;
    private AudioRecord audioRecord;
    private byte[] audioBuffer;
    private int audioRecoderBufferSize;
    private int audioRecoderSliceSize;
    private RESFlvDataCollecter flvDataCollecter;

    public RESAudioClient() {
    }

    public boolean prepare() {
        synchronized (syncOp) {
            audioRecoderSliceSize = 44100 / 2;
            audioRecoderBufferSize = audioRecoderSliceSize;
            prepareAudio();
            return true;
        }
    }

    public boolean start(RESFlvDataCollecter flvDataCollecter) {
        synchronized (syncOp) {
            audioRecord.startRecording();
            audioRecordThread = new AudioRecordThread();
            audioRecordThread.start();
            this.flvDataCollecter = flvDataCollecter;
            return true;
        }
    }

    public boolean stop() {
        synchronized (syncOp) {
            audioRecordThread.quit();
            try {
                audioRecordThread.join();
            } catch (InterruptedException ignored) {
            }
            audioRecordThread = null;
            audioRecord.stop();
            return true;
        }
    }

    public boolean destroy() {
        synchronized (syncOp) {
            audioRecord.release();
            return true;
        }
    }

    private boolean prepareAudio() {
        int minBufferSize = AudioRecord.getMinBufferSize(44100,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT);
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.DEFAULT,
                44100,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                minBufferSize * 5);
        audioBuffer = new byte[audioRecoderBufferSize];
        if (AudioRecord.STATE_INITIALIZED != audioRecord.getState()) {
            Log.e("aa", "audioRecord.getState()!=AudioRecord.STATE_INITIALIZED!");
            return false;
        }
        if (AudioRecord.SUCCESS != audioRecord.setPositionNotificationPeriod(audioRecoderSliceSize)) {
            Log.e("aa", "AudioRecord.SUCCESS != audioRecord.setPositionNotificationPeriod(" + audioRecoderSliceSize + ")");
            return false;
        }
        return true;
    }

    class AudioRecordThread extends Thread {
        private boolean isRunning = true;

        AudioRecordThread() {
            isRunning = true;
        }

        public void quit() {
            isRunning = false;
        }

        @Override
        public void run() {
            while (isRunning) {
                int size = audioRecord.read(audioBuffer, 0, audioBuffer.length);
                if (isRunning && size > 0) {
                    flvDataCollecter.collect(audioBuffer);
                }
            }
        }
    }
}
