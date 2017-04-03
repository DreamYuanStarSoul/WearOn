package com.cisco.wear_on.networking;

/**
 * Created by cymszb on 16-1-21.
 */
public class BloodMeasurementData {
    public int High_pressure;
    public int Low_pressure;
    public BloodMeasurementData(){
        High_pressure = 0;
        Low_pressure = 0;
    }

    public BloodMeasurementData(int high, int low){
        High_pressure = high;
        Low_pressure = low;
    }

}
