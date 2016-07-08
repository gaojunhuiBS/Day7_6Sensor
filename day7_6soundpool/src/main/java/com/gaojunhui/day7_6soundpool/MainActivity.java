package com.gaojunhui.day7_6soundpool;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private SoundPool soundPool;
    private boolean isCanPlay=false;
    private Button bt_play;
    //蜂鸣器
    private Vibrator vibrator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vibrator= (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        bt_play= (Button) findViewById(R.id.bt_play);
        //并发流、音频输出类型，采样率（默认为0）
        soundPool=new SoundPool(1, AudioManager.STREAM_MUSIC,0);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                isCanPlay=true;
            }
        });
        //上下文，资源Id,优先级u
        //返回值是该音频在SoundPool中的Id,播放时使用该Id;
        final int beepId=soundPool.load(this,R.raw.outgoing , 0);
        bt_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCanPlay) {
                    //播放音频的id,左声道，右声道，优先级，循环次数（0：不循环，-1无限循环），比率
                    soundPool.play(beepId, 1, 1, 0, 2, 1);
                    vibrator.vibrate(2000);
                }
            }
        });
    }
}
