package com.cisco.wear_on.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.cisco.wear_on.Utils;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * Created by cymszb on 15-12-17.
 */
public abstract class WearSensor implements SensorEventListener{

    private final static String TAG = "WearSensor";

    private Context mContext;
    private SensorManager mSensorManager = null;

    private int mAccurency = 0;
    private int mValue = 0;


    private ArrayList<WearSensorListener> mSensorListeners = new ArrayList<WearSensorListener>();

    public WearSensor(Context context){
        mContext = context;
        mSensorManager = (SensorManager)mContext.getSystemService(Context.SENSOR_SERVICE);
    }

    public SensorManager getSensorManager(){ return mSensorManager; }


    @Override
    public void onSensorChanged(SensorEvent event) {
        Utils.S_Log.d(TAG,"Sensor value Changed,type:" + event.sensor.getStringType() + ";value:" + event.values[0]);
        mValue = (int)event.values[0];
        postSensorChanged(mValue, mAccurency);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Utils.S_Log.d(TAG, "Sensor Accuracy Changed, type:" + sensor.getStringType() + ";Accuracy:" + accuracy);
        mAccurency = accuracy;
        postSensorChanged(mValue,mAccurency);
    }

    public void postSensorChanged(int value, int accurency){
        Iterator<WearSensorListener> itor = mSensorListeners.iterator();
        while(itor.hasNext()){
            WearSensorListener listener = itor.next();
            listener.onStatusChanged(value,accurency);
        }
    }

    public void addSensorListener(WearSensorListener listener){
        mSensorListeners.add(listener);
    }

    public void removeSensorListener(WearSensorListener listener){
        mSensorListeners.remove(listener);
    }


    public abstract void connectSensor();

    public abstract void disconnectSensor();

    public abstract String getSensorKey();

}
