package com.cisco.wear_on.sensor;

/**
 * Created by cymszb on 15-12-16.
 */
public class MeasurementHeartRate extends Measurement {
    private int mHearRate;
    public MeasurementHeartRate(int value){
        super();
        mHearRate = value;
    }

    @Override
    public int getValue(){
        return mHearRate;
    }

    @Override
    public String getUnit(){
        return "times";
    }

}
