package com.cisco.wear_on.sensor;

import java.util.Calendar;

/**
 * Created by cymszb on 15-12-16.
 */
public abstract class Measurement {
    private long timestamp;
    public Measurement(){
        timestamp = Calendar.getInstance().getTime().getTime();
    }
    public long getTimestamp(){
        return timestamp;
    }

    abstract public int getValue();
    abstract public String getUnit();
}
