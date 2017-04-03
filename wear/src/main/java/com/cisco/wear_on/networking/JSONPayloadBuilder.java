package com.cisco.wear_on.networking;

import android.os.Build;
import android.content.Context;

import com.cisco.wear_on.Utils;
import com.cisco.wear_on.WearApplication;
import com.cisco.wear_on.sensor.MeasurementHeartRate;
import com.cisco.wear_on.sensor.MeasurementPedometer;
import com.cisco.wear_on.sensor.WearSensorHeartrate;
import com.cisco.wear_on.sensor.WearSensorPedometer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by cymszb on 15-12-11.
 */
public class JSONPayloadBuilder {
    private final static String LOG_TAG = "com.cisco.wear_on";

    //Login
    private final static String LOGIN_USERNAME = "username";
    private final static String LOGIN_PASSWORD = "password";

    //Occupy/Release
    private final static String OCCUPY_ID = "deviceId";


    /* measurement */
    private final static String MEASUREMENT_NAME        = "name";
    private final static String MEASUREMENT_VALUE       = "value";
    private final static String MEASUREMENT_TIMESTAMP   = "timestamp";
    private final static String MEASUREMENT_UNIT        = "unit";
    private final static String MEASUREMENT_SENSORID   = "sensorId";

    /* device info*/
    private final static String DEVICEINFO_manufacturer = "manufacturer";
    private final static String DEVICEINFO_model = "model";
    private final static String DEVICEINFO_description = "description";
    private final static String DEVICEINFO_serialNumber = "serialNumber";
    private final static String DEVICEINFO_softwareVersion = "softwareVersion";
    private final static String DEVICEINFO_hardwareVersion = "hardwareVersion";
    private final static String DEVICEINFO_additionalInfo = "additionalInfo";
    private final static String DEVICEINFO_battery = "battery";
    private final static String DEVICEINFO_otherInfo = "otherInfo";
    private final static String DEVICEINFO_address = "address";
    private final static String DEVICEINFO_radio = "radio";

    private final static String DEVICEINFO_sensors = "_sensors";
    private final static String DEVICEINFO_sensorkey = "sensorKey";
    private final static String DEVICEINFO_type = "type";
    private final static String DEVICEINFO_subtype = "subtype";
    private final static String DEVICEINFO_state = "state";





    public static JSONObject createLoginInfoObject(String user, String passwd) throws JSONException{
        JSONObject json_login = new JSONObject();
        json_login.put(LOGIN_USERNAME, user);
        json_login.put(LOGIN_PASSWORD,passwd);
        return json_login;
    }

    public static String createLoginInfoOPayload(String user, String passwd) {
        JSONObject obj_login;
        try {
            obj_login = createLoginInfoObject(user, passwd);
        }catch (JSONException ex){
            ex.printStackTrace();
            return null;
        }
        return obj_login.toString();
    }



    public static JSONObject createDeviceInfoObject()throws JSONException{
        JSONObject json_deviceInfo = new JSONObject();
        JSONArray json_sensorList = new JSONArray();
        JSONObject json_sensorInfo1 = new JSONObject();
        JSONObject json_sensorInfo2 = new JSONObject();
        //common info

        Context appCtx = WearApplication.getAppContext();
        String mac_addr = Utils.Network.Wifi_getMac(appCtx);
        String serial = Build.SERIAL;
        boolean isEmulator = Utils.isEmulator();


        json_deviceInfo.put(DEVICEINFO_manufacturer,"ticwear");
        json_deviceInfo.put(DEVICEINFO_model,"ticwatch");
        json_deviceInfo.put(DEVICEINFO_description,"SmartWatch based on Android wear");
        if(isEmulator)
            json_deviceInfo.put(DEVICEINFO_serialNumber,"123456");
        else
            json_deviceInfo.put(DEVICEINFO_serialNumber,serial);

        json_deviceInfo.put(DEVICEINFO_softwareVersion,"5.1");
        json_deviceInfo.put(DEVICEINFO_hardwareVersion,"1.0");
        json_deviceInfo.put(DEVICEINFO_additionalInfo,"");
        json_deviceInfo.put(DEVICEINFO_battery,50);
        json_deviceInfo.put(DEVICEINFO_otherInfo,"no");
        if(isEmulator)
            json_deviceInfo.put(DEVICEINFO_address,"123456789592");
        else
            json_deviceInfo.put(DEVICEINFO_address,mac_addr);

        json_deviceInfo.put(DEVICEINFO_radio,"ip");
        //heart rate sensor
        if(isEmulator)
            json_sensorInfo1.put(DEVICEINFO_sensorkey,"emulator_hear_rate_0");
        else
            json_sensorInfo1.put(DEVICEINFO_sensorkey, WearSensorHeartrate.getInstance(appCtx).getSensorKey());

        json_sensorInfo1.put(DEVICEINFO_type,"heart_rate");
        json_sensorInfo1.put(DEVICEINFO_subtype,"ambient");
        json_sensorInfo1.put(DEVICEINFO_state,"enabled");
        //pedometer sensor
        if(isEmulator)
            json_sensorInfo2.put(DEVICEINFO_sensorkey,"emulator_pedometer_0");
        else
            json_sensorInfo2.put(DEVICEINFO_sensorkey, WearSensorPedometer.getInstance(appCtx).getSensorKey());
        json_sensorInfo2.put(DEVICEINFO_type,"pedometer");
        json_sensorInfo2.put(DEVICEINFO_subtype,"pedometer");
        json_sensorInfo2.put(DEVICEINFO_state,"enabled");

        json_sensorList.put(json_sensorInfo1);
        json_sensorList.put(json_sensorInfo2);

        json_deviceInfo.put(DEVICEINFO_sensors,json_sensorList);

        Utils.S_Log.d(LOG_TAG,json_deviceInfo.toString());

        return json_deviceInfo;
    }


    public static String createDeviceInfoPayload(){
        JSONObject obj_deviceInfo;
        try {
            obj_deviceInfo = createDeviceInfoObject();
        }catch (JSONException ex){
            ex.printStackTrace();
            return null;
        }
        return obj_deviceInfo.toString();
    }

    public static JSONObject createOccupyReleaseObject(int id)throws JSONException{
        JSONObject json_or = new JSONObject();
        json_or.put(OCCUPY_ID, id);
        return json_or;
    }

    public static String createOccupyReleasePayload(int id) {
        JSONObject obj_or;
        try {
            obj_or = createOccupyReleaseObject(id);
        }catch (JSONException ex){
            ex.printStackTrace();
            return null;
        }
        return obj_or.toString();
    }

    private static JSONObject createHeartRateMeasurementObject(MeasurementHeartRate mHR) throws JSONException{
        JSONObject json_measurement = new JSONObject();
        json_measurement.put(MEASUREMENT_NAME, "heart_rate");
        json_measurement.put(MEASUREMENT_VALUE,mHR.getValue());
        json_measurement.put(MEASUREMENT_UNIT, mHR.getUnit());
        json_measurement.put(MEASUREMENT_TIMESTAMP,mHR.getTimestamp());
        json_measurement.put(MEASUREMENT_SENSORID,
                RestClient.getInstance(WearApplication.getAppContext()).getSensorRegId(RestClient.SENSOR_TYPE_HEARTRATE));
        return json_measurement;
    }

    public static String createHeartRateMeasurementPayload(MeasurementHeartRate mHR) {
        JSONObject obj_measurement;
        try {
            obj_measurement = createHeartRateMeasurementObject(mHR);
        }catch (JSONException ex){
            ex.printStackTrace();
            return null;
        }
        return obj_measurement.toString();
    }

    private static JSONObject createPedometerMeasurementObject(MeasurementPedometer mP) throws  JSONException{
        JSONObject json_measurement = new JSONObject();
        json_measurement.put(MEASUREMENT_NAME, "pedometer");
        json_measurement.put(MEASUREMENT_VALUE,mP.getValue());
        json_measurement.put(MEASUREMENT_UNIT, mP.getUnit());
        json_measurement.put(MEASUREMENT_TIMESTAMP,mP.getTimestamp());
        json_measurement.put(MEASUREMENT_SENSORID,
                RestClient.getInstance(WearApplication.getAppContext()).getSensorRegId(RestClient.SENSOR_TYPE_PEDOMETER));
        return json_measurement;
    }

    public static String createPedometerMeasurementPayload(MeasurementPedometer mp){
        JSONObject obj_measurement;
        try {
            obj_measurement = createPedometerMeasurementObject(mp);
        }catch (JSONException ex){
            ex.printStackTrace();
            return null;
        }
        return obj_measurement.toString();
    }





}
