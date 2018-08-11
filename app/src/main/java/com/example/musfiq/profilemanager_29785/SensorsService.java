package com.example.musfiq.profilemanager_29785;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

public class SensorsService extends Service implements SensorEventListener{
    SensorManager sensorManager;
    Sensor sensorAcclerometer,sensorProximiter;
    AudioManager aMan;
    Notification notification = new Notification();

    private int Z_AXIX=0,Y_AXIX=0,X_AXIX=0;
    private  boolean  IS_SILENT = false,IS_HIGH=false,IS_MEDIUM=false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate(){
        super.onCreate();
        inItSensor();
        inItAudioManager();

        Log.d("a","Created");

    }
    private void inItAudioManager(){
        aMan = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
    }
    private void inItSensor(){
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorAcclerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorProximiter = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
    }
    private   void makeHigh(){
        for (int i=0;i<aMan.getStreamMaxVolume(AudioManager.STREAM_RING);i++){
            aMan.adjustVolume(AudioManager.ADJUST_RAISE,AudioManager.FLAG_SHOW_UI);
        }

    }
    public  void makeMediam(){
        for (int i=0;i<(aMan.getStreamMaxVolume(AudioManager.STREAM_RING));i++){
            aMan.adjustVolume(AudioManager.ADJUST_RAISE,AudioManager.FLAG_SHOW_UI);

        }

        for (int i=0;i<(aMan.getStreamMaxVolume(AudioManager.STREAM_RING)/2);i++){
            aMan.adjustVolume(AudioManager.ADJUST_LOWER,AudioManager.FLAG_SHOW_UI);

        }

    }
    public  void makeSilent(){

        for (int i=aMan.getStreamVolume(AudioManager.STREAM_RING);i>=0;i--){
            //  aMan.setStreamMute(AudioManager.STREAM_RING,true);

            aMan.adjustVolume(AudioManager.ADJUST_LOWER,AudioManager.FLAG_SHOW_UI);
            //  aMan.setRingerMode(0);
        }
        try{
            // aMan.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            aMan.setStreamMute(AudioManager.STREAM_RING,true);

        }
        catch (Exception e){
            Toast.makeText(this,"ERROR", Toast.LENGTH_SHORT).show();
        }

    }
    public int onStartCommand(Intent intent,int flags,int startId){
        Toast.makeText(this,"Started",Toast.LENGTH_SHORT).show();
        sensorManager.registerListener(this,sensorAcclerometer,SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this,sensorProximiter,SensorManager.SENSOR_DELAY_NORMAL);
        return START_STICKY;
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
            Z_AXIX=(int)event.values[2];
            Y_AXIX = (int)event.values[1];
            if (Z_AXIX<0&&Y_AXIX>-5&&IS_SILENT==false){
                makeSilent();
                IS_HIGH = false;
                IS_MEDIUM = false;
                IS_SILENT = true;

            }
            else if (Y_AXIX<-5&&IS_MEDIUM==false){
                IS_HIGH = false;
                IS_SILENT = false;
                IS_MEDIUM = true;
                makeMediam();
            }
        }
        else if (event.sensor.getType()==Sensor.TYPE_PROXIMITY){
            if (event.values[0]==0){
                makeHigh();
                IS_MEDIUM=false;
                IS_SILENT=false;
                IS_HIGH=true;
            }
            else {
                makeSilent();
                IS_HIGH = false;
                IS_MEDIUM = false;
                IS_SILENT = true;
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
