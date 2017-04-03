package com.cisco.wear_on.sensor;

/**
 * Created by cymszb on 15-12-1.
 */
public interface HeartRateListener {
    public void onStatusChanged(int rate, int accuracy);
}
