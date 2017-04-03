package com.cisco.wear_on.networking;

/**
 * Created by cymszb on 16-1-20.
 */
public class SensorRegData {

    public static class RequestData{

    }

    public static class ResponseData{
        ResponseData(){
            SensorKey = null;
            Type = null;
            SubType = null;
            Mode = null;
            Id = -1;
            EndDeviceId = -1;
        }

        ResponseData(String sensorKey,
                     String Type,
                     String SubType,
                     String Mode,
                     int Id,
                     int EndDeviceId) {
            SensorKey = null;
            Type = null;
            SubType = null;
            Mode = null;
            Id = -1;
            EndDeviceId = -1;
        }

        String SensorKey;
        String Type;
        String SubType;
        String Mode;
        int Id;
        int EndDeviceId;
    }
}
