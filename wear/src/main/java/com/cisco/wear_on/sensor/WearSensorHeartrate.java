package com.cisco.wear_on.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;

import com.cisco.wear_on.Utils;

/**
 * Created by cymszb on 15-12-21.
 */

public class WearSensorHeartrate extends WearSensor {

    private final static String TAG = "WearSensorHeartrate";

    private static WearSensorHeartrate mInstance = null;
    private Sensor mHeartbeartSensor = null;
    private String mSensorKey;

    private WearSensorHeartrate(Context context) {
        super(context);
        mSensorKey = "heartrate_0_" + Utils.Network.Wifi_getMac(context);
    }

    public static synchronized WearSensorHeartrate getInstance(Context context) {
        if (mInstance == null) {
            if (context == null) {
                throw new IllegalStateException("Creating BodySensor need a valid Context.");
            }
            mInstance = new WearSensorHeartrate(context);
        }
        return mInstance;
    }


    @Override
    public void connectSensor() {
        mHeartbeartSensor = getSensorManager().getDefaultSensor(Sensor.TYPE_HEART_RATE);
        //mHeartbeartSensor = getSensorManager().getDefaultSensor(92);
        getSensorManager().registerListener(this, mHeartbeartSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void disconnectSensor() {
        getSensorManager().unregisterListener(this, mHeartbeartSensor);
    }

    @Override
    public String getSensorKey() {
        return mSensorKey;
    }
}
