package com.cisco.wear_on.networking;


import com.cisco.wear_on.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JSONResponseParser {

    private final static String LOG_TAG = "com.cisco.wear_on";

    //login
    private final static String LOGIN_ID = "id";
    private final static String LOGIN_TTL = "ttl";
    private final static String LOGIN_CREATED = "created";
    private final static String LOGIN_USERID = "userId";

    //Reg sensors
    private final static String SENSORREG_DEVICEID = "deviceId";

    private final static String SENSORREG_SENSORS = "sensors";
    private final static String SENSORREG_SENSORKEY = "sensorKey";
    private final static String SENSORREG_TYPE = "type";
    private final static String SENSORREG_SUBTYPE = "subtype";
    private final static String SENSORREG_MODE = "mode";
    private final static String SENSORREG_ID = "id";
    private final static String SENSORREG_ENDDEVICEID = "endDeviceId";

    //occupy release
    private final static String OR_STATUS = "status";

    //Blood pressure
    private final static String BLOOD_PRESSURE_NAME  = "name";
    private final static String BLOOD_PRESSURE_VALUE  = "value";
    private final static String BLOOD_PRESSURE_TIMESTAMP  = "timestamp";
    private final static String BLOOD_PRESSURE_UNIT  = "unit";
    private final static String BLOOD_PRESSURE_SENSORID  = "sensorId";
    private final static String BLOOD_PRESSURE_OWNERID  = "ownerId";




    public static LoginData.ResponseData parseLoginResponse(String json){
        JSONObject json_obj;
        LoginData.ResponseData data;
        try{
            json_obj = new JSONObject(json);
            data = new LoginData.ResponseData();
            data.LonginId = json_obj.getString(LOGIN_ID);
            data.Ttl = json_obj.getLong(LOGIN_TTL);
            data.CreatedTime = json_obj.getString(LOGIN_CREATED);
            data.UserId = json_obj.getInt(LOGIN_USERID);

        }catch(JSONException ex){
            Utils.S_Log.e(LOG_TAG,ex.getMessage());
            return null;
        }
        return data;
    }

    public static ArrayList<SensorRegData.ResponseData> parseSensorRegResponse(String json){
        JSONObject json_obj;
        int devicesId;
        ArrayList<SensorRegData.ResponseData> sensor_lists= new ArrayList<SensorRegData.ResponseData>();

        try{
            json_obj = new JSONObject((json));
            devicesId = json_obj.getInt(SENSORREG_DEVICEID);
            JSONArray sensors = json_obj.getJSONArray(SENSORREG_SENSORS);
            for(int i = 0; i < sensors.length(); i++){
                JSONObject sensor_obj = sensors.getJSONObject(i);
                SensorRegData.ResponseData sensor_data = new SensorRegData.ResponseData();
                sensor_data.SensorKey = sensor_obj.getString(SENSORREG_SENSORKEY);
                sensor_data.Type = sensor_obj.getString(SENSORREG_TYPE);
                sensor_data.SubType = sensor_obj.getString(SENSORREG_SUBTYPE);
                sensor_data.Mode = sensor_obj.getString(SENSORREG_MODE);
                sensor_data.Id = sensor_obj.getInt(SENSORREG_ID);
                sensor_data.EndDeviceId = sensor_obj.getInt(SENSORREG_ENDDEVICEID);
                sensor_lists.add(sensor_data);
            }
        }catch (JSONException ex){
            Utils.S_Log.e(LOG_TAG,ex.getMessage());
            return null;
        }

        return sensor_lists;
    }

    public static boolean parseORResponse(String json){
        JSONObject json_obj;
        String success;
        try{
            json_obj = new JSONObject(json);
            success = json_obj.getString(OR_STATUS);

        }catch (JSONException ex){
            Utils.S_Log.e(LOG_TAG,ex.getMessage());
            return false;
        }

        if(success.equals("success")){
            return true;
        }else{
            return false;
        }
    }


    private static final String tmp = "{\n" +
            "    \"name\":\"blood_pressure”,\n" +
            "     \"value\":\"{\\\"systolic\\\":113,\\\"diastolic\\\":90,\\\"mean\\\":100}”,\n" +
            "     \"timestamp\":\"2016-01-21T04:20:56.000Z”,\n" +
            "     \"unit\":”\",\n" +
            "     \"sensorId\":1,\n" +
            "     \"ownerId”:1\n" +
            "}";

    private static final String tmp2 = "{\\\"systolic\\\":113,\\\"diastolic\\\":90,\\\"mean\\\":100}";
    public static BloodMeasurementData parseBloodMeasurement(String json){
        JSONObject json_obj;
        BloodMeasurementData data = new BloodMeasurementData();
        String name;
        String value;
        try {
            json_obj = new JSONObject(json);
            name = json_obj.getString(BLOOD_PRESSURE_NAME);
            if(name.equals("blood_pressure")){
                value = json_obj.getString(BLOOD_PRESSURE_VALUE);
            }else{
                value = null;
            }
        }catch (JSONException ex){
            Utils.S_Log.e(LOG_TAG,ex.getMessage());
            return null;
        }

        if(value != null){
            value.replaceAll("\\\"","\"");
            try {
                JSONObject json_obj_value = new JSONObject(value);
                data.High_pressure = json_obj_value.getInt("systolic");
                data.Low_pressure = json_obj_value.getInt("diastolic");
            }catch (JSONException ex){
                Utils.S_Log.e(LOG_TAG,ex.getMessage());
                return null;
            }
        }else {
            return null;
        }

        return data;

    }



}
