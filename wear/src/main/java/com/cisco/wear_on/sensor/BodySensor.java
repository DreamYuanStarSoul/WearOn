package com.cisco.wear_on.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.cisco.wear_on.Utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by cymszb on 15-11-26.
 */
public class BodySensor implements SensorEventListener {

    public final static String TAG = "BodySensor";

    private Context  mContext;
    private SensorManager mSensorManager = null;
    private List<Sensor> mTotalSensor;
    private Sensor mHeartbeartSensor = null;


    private static BodySensor mInstance = null;

    private BodySensor(Context context){
        mContext = context;
    }

    public static synchronized BodySensor getInstance(Context context){
        if(mInstance == null){
            if(context == null){
                throw new IllegalStateException("Creating BodySensor need a valid Context.");
            }
            mInstance = new BodySensor(context);
            mInstance.initSensor();
        }
        return mInstance;
    }

    private void initSensor(){
        mSensorManager = (SensorManager)mContext.getSystemService(Context.SENSOR_SERVICE);
        mHeartbeartSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);

        mTotalSensor = mSensorManager.getSensorList(Sensor.TYPE_ALL);


        Iterator<Sensor> itor = mTotalSensor.iterator();
        int index = 0;
        while (itor.hasNext()){
            Sensor sensor = itor.next();
            Utils.S_Log.d(TAG,"Sensor index:" + index + ", type:" + sensor.getType());
            index++;
        }
    }

    public void connectHeartRateSensor(){
        if(mSensorManager != null) mSensorManager.registerListener(this, mHeartbeartSensor, SensorManager.SENSOR_DELAY_UI);
    }

    public void disconnectHeartRateSensor(){
        if(mSensorManager != null) mSensorManager.unregisterListener(this, mHeartbeartSensor);

    }


    private int mAccurency = 0;
    private int mRate = 0;



    @Override
    public void onSensorChanged(SensorEvent event) {
        Utils.S_Log.d(TAG,"onSensorChanged");
        if(event.sensor!=mHeartbeartSensor)return;
        mRate = (int)event.values[0];
        postHearRateChanged(mRate, mAccurency);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Utils.S_Log.d(TAG, "onAccuracyChanged");
        if(sensor!=mHeartbeartSensor)return;
        mAccurency = accuracy;
        postHearRateChanged(mRate,mAccurency);
    }


    private ArrayList<HeartRateListener> mHearRateListeners = new ArrayList<HeartRateListener>();

    private void postHearRateChanged(int rate, int accurency){
        Iterator<HeartRateListener> itor = mHearRateListeners.iterator();
        while(itor.hasNext()){
            HeartRateListener listener = itor.next();
            listener.onStatusChanged(rate,accurency);
        }
    }

    public void addHeartRateListener(HeartRateListener listener){
        mHearRateListeners.add(listener);
        //postHearRateChanged(70);
    }

    public void removeHeartRateListener(HeartRateListener listener){
        mHearRateListeners.remove(listener);
    }



}
