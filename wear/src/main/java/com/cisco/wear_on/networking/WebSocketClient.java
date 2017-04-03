package com.cisco.wear_on.networking;

import com.cisco.wear_on.Utils;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by cymszb on 16-1-21.
 */
public class WebSocketClient {

    private final static String LOG_TAG = "com.cisco.wear_on";

    private Socket mSocket;

    public final static int EVENT_BLOOD_PRESSURE = 1;
    public final static int EVENT_OXI_METER = 2;
    public final static int EVENT_BODY_WEIGHT = 3;

    private final static String TARGET_BLOOD_PRESSURE = "/Measurement/1/POST";
    private final static String TARGET_OXI_METER = "/Measurement/2/POST";
    private final static String TARGET_BODY_WEIGHT = "/Measurement/3/POST";


    public WebSocketClient(String serverUrl){
        try {
            //mSocket = IO.socket("http://188.166.225.62:3000");
            mSocket = IO.socket(serverUrl);
            Utils.S_Log.d(LOG_TAG,"Create websocket." + mSocket.toString());

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private Emitter.Listener onNewBoodPressure = new Emitter.Listener() {
        public void call(final Object... args) {
            Utils.S_Log.d("com.cisco.wear_on", "onNewBoodPressure:" + args[0]);
        }
    };

    private Emitter.Listener onNewOxi = new Emitter.Listener() {
        public void call(final Object... args) {
            Utils.S_Log.d("com.cisco.wear_on", "onNewOxi:" + args[0]);
        }
    };

    private Emitter.Listener onNewBodyWeight = new Emitter.Listener() {
        public void call(final Object... args) {
            Utils.S_Log.d("com.cisco.wear_on", "onNewBodyWeight:" + args[0]);
        }
    };

    public void SubscribeEvent(int event,Emitter.Listener eventListener){
        switch (event){
            case EVENT_BLOOD_PRESSURE:
                mSocket.on(TARGET_BLOOD_PRESSURE,eventListener);
                break;
            case EVENT_OXI_METER:
                mSocket.on(TARGET_BLOOD_PRESSURE,eventListener);
                break;
            case EVENT_BODY_WEIGHT:
                mSocket.on(TARGET_BLOOD_PRESSURE,eventListener);
                break;
            default:
                break;
        }
    }

    public void UnSubscribeEvent(int event,Emitter.Listener eventListener){
        switch (event){
            case EVENT_BLOOD_PRESSURE:
                mSocket.off(TARGET_BLOOD_PRESSURE, eventListener);
                break;
            case EVENT_OXI_METER:
                mSocket.off(TARGET_BLOOD_PRESSURE, eventListener);
                break;
            case EVENT_BODY_WEIGHT:
                mSocket.off(TARGET_BLOOD_PRESSURE,eventListener);
                break;
            default:
                break;
        }
    }

    public void disconnect(){
        mSocket.disconnect();

    }

    public void connect(){
        mSocket.connect();
    }


}
