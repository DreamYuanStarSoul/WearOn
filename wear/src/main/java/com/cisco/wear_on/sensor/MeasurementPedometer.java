package com.cisco.wear_on.sensor;

/**
 * Created by cymszb on 15-12-17.
 */
public class MeasurementPedometer extends Measurement {

    private int mSteps;
    public MeasurementPedometer(int value){
        super();
        mSteps = value;
    }

    @Override
    public int getValue() {
        return mSteps;
    }

    @Override
    public String getUnit() {
        return "steps";
    }
}
