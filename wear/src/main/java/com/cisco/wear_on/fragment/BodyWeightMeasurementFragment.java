package com.cisco.wear_on.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.wearable.view.DelayedConfirmationView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import android.os.Message;
import android.os.Handler;

import com.cisco.wear_on.R;
import com.cisco.wear_on.Ringtone;
import com.cisco.wear_on.Utils;
import com.cisco.wear_on.WearApplication;
import com.cisco.wear_on.networking.BloodMeasurementData;
import com.cisco.wear_on.networking.JSONResponseParser;
import com.cisco.wear_on.networking.RestClient;
import com.cisco.wear_on.networking.WebSocketClient;

import org.json.JSONObject;

import java.lang.ref.WeakReference;

import io.socket.emitter.Emitter;

/**
 * Created by cymszb on 16-1-19.
 */
public class BodyWeightMeasurementFragment extends  WearOnFragmentBase
        implements DelayedConfirmationView.DelayedConfirmationListener,
        Emitter.Listener{

    final static String LOG_TAG = "com.cisco.wear_on";

    private View mFragmentView;
    private DelayedConfirmationView mDelayedView;

    private LinearLayout mWeightFrame;
    private TextView mWeightValueTextHigh;
    private TextView mWeightValueTextLow;
    private ImageView mWeightImage;

    private static final String WEBSOCKET_SERVER_URL = "http://188.166.225.62:3000";



    //private Activity mActivityRunOn;

    private RestClient mRestClient;

    private boolean isInProccess = false;

    private WebSocketClient mWebSocketClient;

    private UiHandler mUiHandler = new UiHandler(this);
    public static final int UI_ON_NEW_BLOODPRESURE = 1;


    private static class UiHandler extends Handler {

        private WeakReference<BodyWeightMeasurementFragment> mOuter;

        public UiHandler(BodyWeightMeasurementFragment outer){
            mOuter = new WeakReference<BodyWeightMeasurementFragment>(outer);
        }

        @Override
        public void handleMessage(Message msg) {

            BodyWeightMeasurementFragment outer = mOuter.get();

            switch (msg.what) {

                case UI_ON_NEW_BLOODPRESURE:{
                    BloodMeasurementData data = (BloodMeasurementData)msg.obj;
                    if (outer.isInProccess) {
                        Ringtone.play(WearApplication.getAppContext(),Ringtone.MEASUREMENT_DONE);
                        outer.mDelayedView.reset();
                        outer.mWeightValueTextHigh.setText(String.valueOf(data.High_pressure));
                        outer.mWeightValueTextLow.setText(String.valueOf(data.Low_pressure));
                        outer.mWeightImage.setVisibility(View.INVISIBLE);
                        outer.mWeightFrame.setVisibility(View.VISIBLE);
                        outer.isInProccess = false;
                }
                    break;
            }
        }
    }
    }

/*
    private Emitter.Listener onNewBoodPressure = new Emitter.Listener() {
        public void call(final Object... args) {
            Utils.S_Log.d("com.cisco.wear_on", "onNewBoodPressure:" + args[0]);
            String json = ((JSONObject)args[0]).toString();

            final BloodMeasurementData data = JSONResponseParser.parseBloodMeasurement(json);

            if(data!=null) {

                mWebSocketClient.disconnect();
                mActivityRunOn.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isInProccess) {
                            Ringtone.play(WearApplication.getAppContext(),Ringtone.MEASUREMENT_DONE);
                            mDelayedView.reset();
                            mWeightValueTextHigh.setText(String.valueOf(data.High_pressure));
                            mWeightValueTextLow.setText(String.valueOf(data.Low_pressure));
                            mWeightImage.setVisibility(View.INVISIBLE);
                            mWeightFrame.setVisibility(View.VISIBLE);
                            isInProccess = false;
                        }
                    }
                });
            }

        }
    };
*/
    public BodyWeightMeasurementFragment(){
        super();
        Utils.S_Log.d(LOG_TAG, "BodyWeightMeasurementFragment(),constructor");
        mRestClient = RestClient.getInstance(WearApplication.getAppContext());
        //mRestClient.addRestServerEventListener(this);
        mWebSocketClient = new WebSocketClient(WEBSOCKET_SERVER_URL);
        //mWebSocketClient.SubscribeEvent(WebSocketClient.EVENT_BLOOD_PRESSURE, this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        //mActivityRunOn = this.getActivity();
        Utils.S_Log.d(LOG_TAG, "Welcome Fragment created:" + this + "," + mRestClient);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        Utils.S_Log.d(LOG_TAG, "onCreateView()");
        mFragmentView = inflater.inflate(R.layout.weight, container, false);
        initFragmentView(mFragmentView);

        return mFragmentView;
    }


    private void initFragmentView(View v){
        mDelayedView = (DelayedConfirmationView)v.findViewById(R.id.weight_confirmation_view);
        mWeightFrame = (LinearLayout)v.findViewById(R.id.weight_text);
        mWeightValueTextHigh = (TextView)v.findViewById(R.id.weight_text_value_high);
        mWeightValueTextLow = (TextView)v.findViewById(R.id.weight_text_value_low);
        mWeightImage = (ImageView)v.findViewById(R.id.weight_image);
        mDelayedView.setListener(this);
    }

    @Override
    public void onResume(){
        super.onResume();
        mRestClient.addRestServerEventListener(this);
        mWebSocketClient.SubscribeEvent(WebSocketClient.EVENT_BLOOD_PRESSURE, this);
    }

    @Override
    public void onPause(){
        super.onPause();
        mDelayedView.reset();
        mRestClient.removeServerEventListener(this);
        mWebSocketClient.UnSubscribeEvent(WebSocketClient.EVENT_BLOOD_PRESSURE, this);
        mWebSocketClient.disconnect();

    }

    @Override
    public void onOccupyResult(boolean success, int errCode){
        //ignore
        Utils.S_Log.d(LOG_TAG, "Occupy result:" + success);
        if(success){
            mWebSocketClient.connect();
        }else{
            //mWebSocketClient.connect();
        }
    }
    @Override
    public void onReleaseResult(boolean success, int errCode) {
        //ignore
        Utils.S_Log.d(LOG_TAG,"Release result:" + success);
        if(success){
            //mWebSocketClient.disconnect();
        }else{
            //mWebSocketClient.connect();
        }
        mWebSocketClient.disconnect();
    }


    @Override
    public void onTimerFinished(View view) {
        // User didn't cancel, perform the action
        Utils.S_Log.d(LOG_TAG, "onTimerFinished..");
        if(isInProccess)
            mDelayedView.start();

    }

    @Override
    public void onTimerSelected(View view) {
        // User canceled, abort the action
        Utils.S_Log.d(LOG_TAG, "onTimerSelected!!");
        mDelayedView.setTotalTimeMs(5000);
        if(isInProccess == false){
            Ringtone.play(WearApplication.getAppContext(), Ringtone.MESUREMENT_START);
            mDelayedView.start();
            isInProccess = true;
            mRestClient.AsyncOccupyDevice(1);
        }else{
            Ringtone.play(WearApplication.getAppContext(),Ringtone.MEASUREMENT_DONE);
            mRestClient.AsyncReleaseDevice(1);
            mDelayedView.reset();
            isInProccess = false;
        }

    }

    public void updateUiOnNewBlood(BloodMeasurementData data){
        mUiHandler.sendMessage(mUiHandler.obtainMessage(UI_ON_NEW_BLOODPRESURE,data));
    }


    @Override
    public void call(Object... args) {
        Utils.S_Log.d("com.cisco.wear_on", "onNewBoodPressure:" + args[0]);
        String json = ((JSONObject)args[0]).toString();

        final BloodMeasurementData data = JSONResponseParser.parseBloodMeasurement(json);

        if(data!=null) {

            mWebSocketClient.disconnect();
            /*
            mActivityRunOn.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (isInProccess) {
                        Ringtone.play(WearApplication.getAppContext(),Ringtone.MEASUREMENT_DONE);
                        mDelayedView.reset();
                        mWeightValueTextHigh.setText(String.valueOf(data.High_pressure));
                        mWeightValueTextLow.setText(String.valueOf(data.Low_pressure));
                        mWeightImage.setVisibility(View.INVISIBLE);
                        mWeightFrame.setVisibility(View.VISIBLE);
                        isInProccess = false;
                    }
                }
            });
            */
            updateUiOnNewBlood(data);
        }
    }
}
