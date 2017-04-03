package com.cisco.wear_on.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;

import com.cisco.wear_on.Utils;

/**
 * Created by cymszb on 15-12-21.
 */
public class WearSensorPedometer extends WearSensor {

    private final static String TAG = "WearSensorHeartrate";

    private static WearSensorPedometer mInstance = null;
    private Sensor mPedometerSensor = null;
    private String mSensorKey;

    private WearSensorPedometer(Context context){
        super(context);
        mSensorKey = "pedometer_0_" + Utils.Network.Wifi_getMac(context);
    }

    public static synchronized WearSensorPedometer getInstance(Context context){
        if(mInstance == null){
            if(context == null){
                throw new IllegalStateException("Creating BodySensor need a valid Context.");
            }
            mInstance = new WearSensorPedometer(context);
        }
        return mInstance;
    }

    @Override
    public void connectSensor() {
        mPedometerSensor = getSensorManager().getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        getSensorManager().registerListener(this, mPedometerSensor, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void disconnectSensor() {
        getSensorManager().unregisterListener(this, mPedometerSensor);
    }

    @Override
    public String getSensorKey(){ return mSensorKey; }
}
