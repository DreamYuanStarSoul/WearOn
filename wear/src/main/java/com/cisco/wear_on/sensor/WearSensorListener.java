package com.cisco.wear_on.sensor;

/**
 * Created by cymszb on 15-12-17.
 */
public interface WearSensorListener {
    public void onStatusChanged(int value, int accuracy);
}
