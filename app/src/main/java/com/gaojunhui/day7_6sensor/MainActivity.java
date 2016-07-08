package com.gaojunhui.day7_6sensor;

import android.animation.AnimatorSet;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;


public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor sensorAccelerometer;
    private boolean isCanSHAKE=true;
    private boolean isCanPlay=false;
    private Handler handler=new Handler();
    private SoundPool soudPool;
    private int deepId;
    private Vibrator vibrator;
    private ImageView iv_up,iv_down;
    private AnimationSet animatorSet_up=new AnimationSet(true);
    private AnimationSet animatorSet_down=new AnimationSet(true);
    private TranslateAnimation t_up1,t_up2,t_down1,t_down2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv_down= (ImageView) findViewById(R.id.iv_down);
        iv_up= (ImageView) findViewById(R.id.iv_up);
        //获得蜂鸣器
        vibrator= (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        //并发流，音频输出类型，采样率（默认为0）
        soudPool=new SoundPool(1, AudioManager.STREAM_MUSIC,0);
        soundPoolLisner();
        //设置动画
        setAnimation();
        //传感器
        Sensor();


    }

    private void setAnimation() {
        //上部图片动画
        t_up1=new TranslateAnimation(Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,0f,
                Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,-0.5f);
        t_up1.setDuration(1000);
        t_up2=new TranslateAnimation(Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,0f,
                Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,0.5f);
        t_up2.setDuration(1000);
        t_up2.setStartOffset(1000);
        animatorSet_up.addAnimation(t_up1);
        animatorSet_up.addAnimation(t_up2);
        //下部动画
        t_down1=new TranslateAnimation(Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,0f,
                Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,0.5f);
        t_down2=new TranslateAnimation(Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,0f,
                Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,-0.5f);
        t_down1.setDuration(1000);
        t_down2.setDuration(1000);
        t_down2.setStartOffset(1000);
        animatorSet_down.addAnimation(t_down1);
        animatorSet_down.addAnimation(t_down2);
    }

    /**
     * soundPool监听
     */
    private void soundPoolLisner() {
        soudPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
         @Override
         public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
           isCanPlay=true;
         }
     });
        //播放音乐的Id
       deepId=soudPool.load(this, R.raw.outgoing,0);
    }

    /**
     * 得到和获得加速度传感器
     */
    private void Sensor() {
        sensorManager= (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        //打印手机传感器配置
//        bt_get.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                 List<Sensor> sensorList=sensorManager.getSensorList(Sensor.TYPE_ALL);
//                for (Sensor sensor:sensorList){
//                    Log.i("---", "---名称："+sensor.getName()+"厂商："+sensor.getVendor()+"类型：");
//                }
//            }
//        });
        //得到加速度传感器
        sensorAccelerometer=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //注册传感器(监听器，监听的传感器，传感器的精度)
        sensorManager.registerListener(this,sensorAccelerometer,SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //注销传感器
        sensorManager.unregisterListener(this);
    }
//传感器返回的值
    @Override
    public void onSensorChanged(SensorEvent event) {
        //加速度传感器有三个值
        if((Math.abs(event.values[0])>19||Math.abs(event.values[1])>19||Math.abs(event.values[2])>19)&&isCanSHAKE){
            isCanSHAKE=false;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    isCanSHAKE=true;
                    if (isCanPlay){
                        //播放音乐的Id、左声道、右声道、优先级、循环次数、比率
                        soudPool.play(deepId, 1, 1, 0, 1, 1);
                        vibrator.vibrate(2000);
                        iv_up.startAnimation(animatorSet_up);
                        iv_down.startAnimation(animatorSet_down);
                    }
                }
            },1000);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
