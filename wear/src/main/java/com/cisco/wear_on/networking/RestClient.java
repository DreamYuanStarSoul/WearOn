package com.cisco.wear_on.networking;

import java.net.URL;
import java.net.HttpURLConnection;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeoutException;


import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.Looper;

import com.cisco.wear_on.Utils;
import com.cisco.wear_on.sensor.Measurement;
import com.cisco.wear_on.sensor.MeasurementHeartRate;
import com.cisco.wear_on.sensor.MeasurementPedometer;
import com.cisco.wear_on.sensor.WearSensorListener;

/**
 * Created by cymszb on 15-12-7.
 */
public class RestClient implements RestServerEventListener{

    public static final String TAG = "com.cisco.wear_on";

    private Context mContext;

    private static  RestClient mInstance = null;

    private  RestClient(Context context){
        mContext = context;
        initNetworkThread();
        this.addRestServerEventListener(this);
    }

    public static synchronized RestClient getInstance(Context context) {
        if(mInstance == null){
            if(context == null){
                throw new IllegalStateException("Creating BodySensor need a valid Context.");
            }
            mInstance = new RestClient(context);
        }
        return mInstance;
    }


    private ArrayList<RestServerEventListener> mRestServerEventListeners = new ArrayList<RestServerEventListener>();

    private void postLoginDone(LoginData.ResponseData data){
        Iterator<RestServerEventListener> itor = mRestServerEventListeners.iterator();
        while(itor.hasNext()){
            RestServerEventListener listener = itor.next();
            listener.onLoginDone(data);
        }
    }

    private void postLoginFailed(int errCode){
        Iterator<RestServerEventListener> itor = mRestServerEventListeners.iterator();
        while(itor.hasNext()){
            RestServerEventListener listener = itor.next();
            listener.onLoginFailed(errCode);
        }
    }


    private void postSensorRegDone(ArrayList<SensorRegData.ResponseData> data){
        Iterator<RestServerEventListener> itor = mRestServerEventListeners.iterator();
        while(itor.hasNext()){
            RestServerEventListener listener = itor.next();
            listener.onSensorRegDone(data);
        }
    }

    private void postSensorRegFailed(int errCode){
        Iterator<RestServerEventListener> itor = mRestServerEventListeners.iterator();
        while(itor.hasNext()){
            RestServerEventListener listener = itor.next();
            listener.onSensorRegFaild(errCode);
        }
    }

    private void postOccupyDeviceResult(boolean success,int errCode){
        Iterator<RestServerEventListener> itor = mRestServerEventListeners.iterator();
        while(itor.hasNext()){
            RestServerEventListener listener = itor.next();
            listener.onOccupyResult(success, errCode);
        }
    }

    private void postReleaseDeviceResult(boolean success,int errCode){
        Iterator<RestServerEventListener> itor = mRestServerEventListeners.iterator();
        while(itor.hasNext()){
            RestServerEventListener listener = itor.next();
            listener.onReleaseResult(success, errCode);
        }
    }

    private void postDataPostDone(){
        Iterator<RestServerEventListener> itor = mRestServerEventListeners.iterator();
        while(itor.hasNext()){
            RestServerEventListener listener = itor.next();
            listener.onDataPostDone();
        }
    }

    private void postDataPostFailed(int errCode){
        Iterator<RestServerEventListener> itor = mRestServerEventListeners.iterator();
        while(itor.hasNext()){
            RestServerEventListener listener = itor.next();
            listener.onDataPostFailed(errCode);
        }
    }



    public void addRestServerEventListener(RestServerEventListener listener){
        mRestServerEventListeners.add(listener);
    }

    public void removeServerEventListener(RestServerEventListener listener){
        mRestServerEventListeners.remove(listener);
    }


    public void AsyncLogin(String user, String passwd){
        //clear login status first
        //isOnline = false;
        mNetworkWorkerHandler.sendMessage(
                mNetworkWorkerHandler.obtainMessage(REST_USER_LOGIN,
                        new LoginData.RequestData(user, passwd)));
    }

    public void AsyncRegisterDevice(){
        mNetworkWorkerHandler.sendEmptyMessage(REST_REG_DEVICE);
    }

    public void AsyncOccupyDevice(int deviceId) {

        mNetworkWorkerHandler.sendMessage(
                mNetworkWorkerHandler.obtainMessage(REST_OCCUPY_DEVICE,deviceId,0));
    }

    public void AsyncReleaseDevice(int deviceId) {
        mNetworkWorkerHandler.sendMessage(
                mNetworkWorkerHandler.obtainMessage(REST_RELEASE_DEVICE,deviceId,0));
    }

    public void AsyncPostHeartRate(Measurement mm){
        mNetworkWorkerHandler.sendMessage(mNetworkWorkerHandler.obtainMessage(REST_UPLOAD_HEARTRATE,mm));
    }

    public void AsyncPostPedometer(Measurement mm){
        mNetworkWorkerHandler.sendMessage(mNetworkWorkerHandler.obtainMessage(REST_UPLOAD_PEDOMETER,mm));
    }





    private  String httpGet(String urlStr) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn =
                (HttpURLConnection) url.openConnection();

        if (conn.getResponseCode() != 200) {
            throw new IOException(conn.getResponseMessage());
        }

        // Buffer the result into a string
        BufferedReader rd = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();

        conn.disconnect();
        return sb.toString();
    }

    private  String httpPost(String urlStr, String payload, String access_token) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn =
                (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setUseCaches(false);
        conn.setAllowUserInteraction(false);
        conn.setRequestProperty("Content-Type",
                "application/json");

        if(access_token!= null)
            conn.setRequestProperty("Authorization",access_token);


        // Create the json content
        OutputStream out = conn.getOutputStream();
        Writer writer = new OutputStreamWriter(out, "UTF-8");
        writer.write(payload);
        writer.close();
        out.close();

        if (conn.getResponseCode() != 200) {
            throw new IOException(conn.getResponseMessage());
        }

        // Buffer the result into a string
        BufferedReader rd = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();

        conn.disconnect();
        return sb.toString();


    }

    public final static int REST_REQ_OK = 0;
    public final static int REST_REQ_TIMEOUT = 1;
    public final static int REST_REQ_SERVER_FAIL = 2;
    public final static int REST_REQ_LOCAL_FAIL = 3;
    public final static int REST_REQ_NETWORK_FAIL = 4;
    public final static int REST_REQ_FORMAT_ERROR = 5;
    public final static int REST_REQ_UNKNOWN_ERROR = 6;

    //private final static String REST_TARGET_DEVICEINFOS = "http://Connected-Life:3000/api/DeviceInfos";
    //private final static String REST_TARGET_MEATUREMENTS = "http://Connected-Life:3000/api/Measurements";

    private final static String REST_TARGET_USERLOGIN = "http://188.166.225.62:3000/api/v1/Accounts/login";

    private final static String REST_TARGET_DEVICEINFOS = "http://188.166.225.62:3000/api/v1/EndDevices/registerIPDevice";
    private final static String REST_TARGET_MEATUREMENTS ="http://188.166.225.62:3000/api/v1/Measurements";

    //private final static String REST_TARGET_OCCUPY_BLOODPRESURE_BASE = "http://188.166.225.62:3000/api/v1/EndDevices/1/applyToUser?access_token=";
    //private final static String REST_TARGET_RELEASE_BLOODPRESURE_BASE = "http://188.166.225.62:3000/api/v1/EndDevices/1/releaseToUser?access_token=";

    private final static String REST_TARGET_OCCUPY_BASE = "http://188.166.225.62:3000/api/v1/EndDevices/";
    private final static String REST_TARGET_RELEASE_BASE = "http://188.166.225.62:3000/api/v1/EndDevices/";


    private boolean isOnline = false;

    private LoginData.ResponseData mLoginInfo = null;
    private SensorRegData.ResponseData mRegHeartRateInfo = null;
    private SensorRegData.ResponseData mRegPedometerInfo = null;
    //private ArrayList<SensorRegData.ResponseData> mRegSensorInfo = null;

    public boolean isOnline(){
        //Utils.S_Log.d(TAG,"Online?:" + isOnline);
        return (isOnline!=false)&&(mLoginInfo != null)?true:false;
    }

    public boolean isRegHeartRateSensorDone(){
        return (isOnline!=false)&&(mRegHeartRateInfo != null)?true:false;
    }
    public boolean isRegPedometerSensorDone(){
        return (isOnline!=false)&&(mRegPedometerInfo != null)?true:false;
    }


    public String getUserToken(){
        if(isOnline && mLoginInfo != null)
            return mLoginInfo.LonginId;
        else
            return null;
    }


    public final static int SENSOR_TYPE_HEARTRATE = 1;
    public final static int SENSOR_TYPE_PEDOMETER = 2;

    public int getSensorRegId(int sensor_type){
        if(sensor_type == SENSOR_TYPE_HEARTRATE && isRegHeartRateSensorDone())
            return mRegHeartRateInfo.Id;
        if(sensor_type == SENSOR_TYPE_PEDOMETER && isRegPedometerSensorDone())
            return mRegPedometerInfo.Id;
        else
            return 0;
    }


    @Override
    public void onLoginDone(LoginData.ResponseData data) {
        isOnline = true;
        mLoginInfo = data;

        //reg sensor devices here
        AsyncRegisterDevice();
    }

    @Override
    public void onLoginFailed(int errCode) {
        isOnline = false;
        mLoginInfo = null;
    }

    @Override
    public void onSensorRegDone(ArrayList<SensorRegData.ResponseData> reg_sensors) {
        //ignore
        //mRegSensorInfo = reg_sensors;
        Iterator<SensorRegData.ResponseData> itor = reg_sensors.iterator();
        while(itor.hasNext()){
            SensorRegData.ResponseData sensor = itor.next();
            if(sensor.Type.equals("heart_rate"))
                mRegHeartRateInfo = sensor;
            else if(sensor.Type.equals("pedometer"))
                mRegPedometerInfo = sensor;
            else
                continue;
        }

    }
    @Override
    public void onSensorRegFaild(int errCode) {
        //ignore
        //mRegSensorInfo = null;
        mRegHeartRateInfo = null;
        mRegPedometerInfo = null;
    }

    @Override
    public void onDataPostDone() {
        //ignore
    }

    @Override
    public void onDataPostFailed(int errCode) {
        //ignore
    }

    @Override
    public void onOccupyResult(boolean success,int errCode){
        //ignore
    }
    @Override
    public void onReleaseResult(boolean success, int errCode) {
        //ignore
    }

    private int UserLogin(LoginData.RequestData data){
        String payload = JSONPayloadBuilder.createLoginInfoOPayload(data.Username,data.Password);
        String result;
        if(payload == null)
            return REST_REQ_LOCAL_FAIL;
        try{
            result = httpPost(REST_TARGET_USERLOGIN,payload,null);
        }catch ( UnknownHostException ex){
            Utils.S_Log.e(TAG,ex.getMessage());
            postLoginFailed(REST_REQ_NETWORK_FAIL);
            return REST_REQ_NETWORK_FAIL;
        }catch (IOException ex){
            Utils.S_Log.e(TAG,ex.getMessage());
            postLoginFailed(REST_REQ_SERVER_FAIL);
            return REST_REQ_SERVER_FAIL;
        }catch (TimeoutException ex){
            Utils.S_Log.e(TAG,ex.getMessage());
            postLoginFailed(REST_REQ_TIMEOUT);
            return REST_REQ_TIMEOUT;
        } catch (Exception ex){
            ex.printStackTrace();
            Utils.S_Log.e(TAG, ex.getMessage());
            postLoginFailed(REST_REQ_UNKNOWN_ERROR);
            return REST_REQ_UNKNOWN_ERROR;
        }

        Utils.S_Log.d(TAG,result);

        LoginData.ResponseData response = JSONResponseParser.parseLoginResponse(result);
        if(response == null){
            postLoginFailed(REST_REQ_FORMAT_ERROR);
            return REST_REQ_FORMAT_ERROR;
        }

        postLoginDone(response);

        return  REST_REQ_OK;
    }


    private int RegDevice(){
        String payload = JSONPayloadBuilder.createDeviceInfoPayload();
        String result;
        if(!isOnline())
            return REST_REQ_NETWORK_FAIL;

        if(payload == null)
            return REST_REQ_LOCAL_FAIL;
        try {
            result = httpPost(REST_TARGET_DEVICEINFOS,payload,getUserToken());
        }catch ( UnknownHostException ex){
            Utils.S_Log.e(TAG, ex.getMessage());
            postSensorRegFailed(REST_REQ_NETWORK_FAIL);
            return REST_REQ_NETWORK_FAIL;
        }catch (IOException ex){
            Utils.S_Log.e(TAG, ex.getMessage());
            postSensorRegFailed(REST_REQ_SERVER_FAIL);
            return REST_REQ_SERVER_FAIL;
        }catch (TimeoutException ex){
            Utils.S_Log.e(TAG, ex.getMessage());
            postSensorRegFailed(REST_REQ_TIMEOUT);
            return REST_REQ_TIMEOUT;
        } catch (Exception ex){
            ex.printStackTrace();
            Utils.S_Log.e(TAG, ex.getMessage());
            postSensorRegFailed(REST_REQ_UNKNOWN_ERROR);
            return REST_REQ_UNKNOWN_ERROR;
        }

        Utils.S_Log.d(TAG, result);
        ArrayList<SensorRegData.ResponseData> response= JSONResponseParser.parseSensorRegResponse(result);
        if(response == null){
            postSensorRegFailed(REST_REQ_FORMAT_ERROR);
            return REST_REQ_FORMAT_ERROR;
        }

        postSensorRegDone(response);

        return REST_REQ_OK;
    }

    //private final static String REST_TARGET_OCCUPY_BLOODPRESURE_BASE = "http://188.166.225.62:3000/api/v1/EndDevices/1/applyToUser?access_token=";

    private int OccupyDevice(int deviceId){
        String payload = JSONPayloadBuilder.createOccupyReleasePayload(deviceId);
        String result;
        if(!isOnline())
            return REST_REQ_NETWORK_FAIL;

        if(payload == null)
            return REST_REQ_LOCAL_FAIL;

        String rest_url_target = REST_TARGET_OCCUPY_BASE + deviceId + "/applyToUser?access_token=" + getUserToken();
        Utils.S_Log.d(TAG,"OccupyDevice target url: "+ rest_url_target);
        try {
            result = httpPost(rest_url_target,payload,getUserToken());
        }catch ( UnknownHostException ex){
            Utils.S_Log.e(TAG,ex.getMessage());
            postOccupyDeviceResult(false,REST_REQ_NETWORK_FAIL);
            return REST_REQ_NETWORK_FAIL;
        }catch (IOException ex){
            Utils.S_Log.e(TAG,ex.getMessage());
            postOccupyDeviceResult(false,REST_REQ_SERVER_FAIL);
            return REST_REQ_SERVER_FAIL;
        }catch (TimeoutException ex){
            Utils.S_Log.e(TAG,ex.getMessage());
            postOccupyDeviceResult(false,REST_REQ_TIMEOUT);
            return REST_REQ_TIMEOUT;
        } catch (Exception ex){
            ex.printStackTrace();
            Utils.S_Log.e(TAG, ex.getMessage());
            postOccupyDeviceResult(false,REST_REQ_UNKNOWN_ERROR);
            return REST_REQ_UNKNOWN_ERROR;
        }

        Utils.S_Log.d(TAG, result);
        postOccupyDeviceResult(JSONResponseParser.parseORResponse(result),REST_REQ_OK);

        return REST_REQ_OK;
    }

    private int ReleaseDevice(int deviceId){
        String payload = JSONPayloadBuilder.createOccupyReleasePayload(deviceId);
        String result;
        if(!isOnline())
            return REST_REQ_NETWORK_FAIL;

        if(payload == null)
            return REST_REQ_LOCAL_FAIL;

        String rest_url_target = REST_TARGET_RELEASE_BASE + deviceId + "/releaseToUser?access_token=" + getUserToken();
        Utils.S_Log.d(TAG,"ReleaseDevice target url: "+ rest_url_target);
        try {
            result = httpPost(rest_url_target,payload,getUserToken());
        }catch ( UnknownHostException ex){
            Utils.S_Log.e(TAG,ex.getMessage());
            postReleaseDeviceResult(false,REST_REQ_NETWORK_FAIL);
            return REST_REQ_NETWORK_FAIL;
        }catch (IOException ex){
            Utils.S_Log.e(TAG,ex.getMessage());
            postReleaseDeviceResult(false, REST_REQ_SERVER_FAIL);
            return REST_REQ_SERVER_FAIL;
        }catch (TimeoutException ex){
            Utils.S_Log.e(TAG,ex.getMessage());
            postReleaseDeviceResult(false, REST_REQ_TIMEOUT);
            return REST_REQ_TIMEOUT;
        } catch (Exception ex){
            ex.printStackTrace();
            Utils.S_Log.e(TAG, ex.getMessage());
            postReleaseDeviceResult(false, REST_REQ_UNKNOWN_ERROR);
            return REST_REQ_UNKNOWN_ERROR;
        }

        Utils.S_Log.d(TAG, result);
        postReleaseDeviceResult(JSONResponseParser.parseORResponse(result),REST_REQ_OK);

        return REST_REQ_OK;
    }

    private int PostHeartRate(Measurement mm){
        String payload;
        String result;

        if(!isRegHeartRateSensorDone())
            return REST_REQ_NETWORK_FAIL;

        if(mm instanceof MeasurementHeartRate) {
            MeasurementHeartRate hear_rate = (MeasurementHeartRate) mm;
            payload = JSONPayloadBuilder.createHeartRateMeasurementPayload(hear_rate);
            if(payload == null) {
                Utils.S_Log.e(TAG,"JSONPayloadBuilder.createHeartRateMeasurementPayload failed");
                return REST_REQ_LOCAL_FAIL;
            }
            try {
                result = httpPost(REST_TARGET_MEATUREMENTS,payload,getUserToken());
            }catch (Exception ex){
                ex.printStackTrace();
                Utils.S_Log.e(TAG,ex.getMessage());
                return REST_REQ_SERVER_FAIL;
            }
            Utils.S_Log.d(TAG,result);
        }else {
            Utils.S_Log.e(TAG,"Input type for PostHeartRate is wrong!");
            return REST_REQ_LOCAL_FAIL;
        }
        Utils.S_Log.d(TAG, result);

        return REST_REQ_OK;
    }

    private int PostStepCount(Measurement mm){
        String payload;
        String result;

        if(!isRegPedometerSensorDone())
            return REST_REQ_NETWORK_FAIL;

        if(mm instanceof MeasurementPedometer) {
            MeasurementPedometer steps_counter = (MeasurementPedometer)mm;
            payload = JSONPayloadBuilder.createPedometerMeasurementPayload(steps_counter);
            if(payload == null){
                Utils.S_Log.e(TAG,"JSONPayloadBuilder.createPedometerMeasurementPayload failed");
                return REST_REQ_LOCAL_FAIL;
            }
            try {
                result = httpPost(REST_TARGET_MEATUREMENTS,payload,getUserToken());
            }catch (Exception ex){
                ex.printStackTrace();
                Utils.S_Log.e(TAG,ex.getMessage());
                return REST_REQ_SERVER_FAIL;
            }
            Utils.S_Log.d(TAG, result);


        }else {
            Utils.S_Log.e(TAG, "Input type for PostStepCount is wrong!");
            return REST_REQ_LOCAL_FAIL;
        }

        Utils.S_Log.d(TAG, result);
        return REST_REQ_OK;
    }

    private HandlerThread mNetworkWorker;
    private NetHandler mNetworkWorkerHandler;

    private static final int REST_USER_LOGIN = 0;
    private static final int REST_REG_DEVICE = 1;
    private static final int REST_UPLOAD_HEARTRATE = 2;
    private static final int REST_UPLOAD_PEDOMETER = 3;
    private static final int REST_OCCUPY_DEVICE = 4;
    private static final int REST_RELEASE_DEVICE = 5;

    private void initNetworkThread(){
        mNetworkWorker = new HandlerThread("REST Network");
        mNetworkWorker.start();
        mNetworkWorkerHandler = new NetHandler(mNetworkWorker.getLooper());
    }

    private class NetHandler extends Handler{
        public NetHandler(Looper looper){
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REST_USER_LOGIN:
                    UserLogin((LoginData.RequestData)msg.obj);
                    break;
                case REST_REG_DEVICE:
                    RegDevice();
                    break;
                case REST_UPLOAD_HEARTRATE:
                    PostHeartRate((Measurement)msg.obj);
                    break;
                case REST_UPLOAD_PEDOMETER:
                    PostStepCount((Measurement)msg.obj);
                    break;
                case REST_OCCUPY_DEVICE:
                    OccupyDevice(msg.arg1);
                    break;
                case REST_RELEASE_DEVICE:
                    ReleaseDevice(msg.arg1);
                    break;
                default:
                    break;

            }
        }

    }

}
