package me.lake.androidsoundtouchtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import me.lake.soundtouch.SoundTouch;

public class MainActivity extends AppCompatActivity implements RESFlvDataCollecter{
    boolean isStarted = false;
    RESAudioClient audioClient;
    PcmPlayer pcmPlayer;
    Button btn_toggle;
    SoundTouch soundTouch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        soundTouch = new SoundTouch(1, 44100);
        soundTouch.create();
        soundTouch.setPitchSemiTones(-0.318f);
        soundTouch.setSpeed(1/0.7f);
        soundTouch.setTempo(0.7f);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_toggle = (Button) findViewById(R.id.btn_toggle);
        btn_toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isStarted) {
                    btn_toggle.setText("Start");
                    audioClient.stop();
                    pcmPlayer.stop();
                } else {
                    btn_toggle.setText("Stop");
                    pcmPlayer.start();
                    audioClient.start(MainActivity.this);
                }
                isStarted = !isStarted;
            }
        });
        audioClient = new RESAudioClient();
        audioClient.prepare();
        pcmPlayer = new PcmPlayer();
        pcmPlayer.prepare();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isStarted) {
            audioClient.stop();
            pcmPlayer.stop();
            btn_toggle.setText("Start");
            isStarted = !isStarted;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        audioClient.destroy();
        pcmPlayer.destroy();
        soundTouch.destroy();
    }

    @Override
    public void collect(byte[] audioData) {
        Log.e("aa", "collect1");
        soundTouch.putData(audioData, audioData.length / 2);
        Log.e("aa", "collect2");
        for(int i =0;i<audioData.length;i++)
        {
            audioData[i]=0;
        }
        int a = soundTouch.getData(audioData,audioData.length/2);
        pcmPlayer.write(audioData);
        Log.e("aa", "collect3=" + audioData[0] + audioData[1]+"a="+a);
    }
}
