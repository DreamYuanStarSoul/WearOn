package com.cisco.wear_on.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.cisco.wear_on.R;
import com.cisco.wear_on.Ringtone;
import com.cisco.wear_on.Utils;
import com.cisco.wear_on.WearApplication;
import com.cisco.wear_on.networking.RestClient;
import com.cisco.wear_on.sensor.Measurement;
import com.cisco.wear_on.sensor.MeasurementHeartRate;
import com.cisco.wear_on.sensor.MeasurementPedometer;
import com.cisco.wear_on.sensor.WearSensorHeartrate;
import com.cisco.wear_on.sensor.WearSensorListener;
import com.cisco.wear_on.sensor.WearSensorPedometer;

/**
 * Created by cymszb on 16-1-19.
 */
public class PedometerMeasurementFragment extends WearOnFragmentBase {

    final static String LOG_TAG = "com.cisco.wear_on";


    private View mFragmentView;

    private ImageButton mPRButton;
    private ImageView mStatusImage;
    private TextView mValueText;

    private Measurement mPedometer;
    private boolean mIsRegstered = false;
    private boolean mInProccess =false;

    //private Activity mActivityRunOn;
    private RestClient mRestClient;

    private  View.OnClickListener PRButtonClickListener =  new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!mInProccess) {
                Ringtone.play(WearApplication.getAppContext(), Ringtone.MESUREMENT_START);
                startMeasuring();
            } else {
                Ringtone.play(WearApplication.getAppContext(),Ringtone.MEASUREMENT_DONE);
                stopMeasuring();
            }
            refreshView();
        }
    };


    private WearSensorListener mPedometerListener = new WearSensorListener(){
        @Override
        public void onStatusChanged(int value, int accuracy){

            mPedometer = new MeasurementPedometer(value);
            mValueText.setText(String.valueOf(mPedometer.getValue()));
            mRestClient.AsyncPostPedometer(mPedometer);
        }
    };


    public PedometerMeasurementFragment(){
        super();
        mRestClient = RestClient.getInstance(WearApplication.getAppContext());
        mPedometer = new MeasurementPedometer(0);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        Utils.S_Log.d(LOG_TAG, "PedometerMeasurementFragment created");
        super.onCreate(savedInstanceState);
        //mActivityRunOn = this.getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        mFragmentView = inflater.inflate(R.layout.steps_measurement, container, false);
        initFragmentView(mFragmentView);
        return mFragmentView;

    }

    private void initFragmentView(View v){
        mPRButton = (ImageButton)mFragmentView.findViewById(R.id.pedometer_pr_button);
        mStatusImage = (ImageView)mFragmentView.findViewById(R.id.pedometer_status_image);
        mValueText = (TextView)mFragmentView.findViewById(R.id.pedometer_text_value);
        mPRButton.setOnClickListener(PRButtonClickListener);
    }

    private void refreshStatus(){
        mIsRegstered = mRestClient.isRegPedometerSensorDone();
    }

    private void refreshView(){
        mValueText.setText(String.valueOf(mPedometer.getValue()));
        if(mIsRegstered)
            mStatusImage.setImageDrawable(WearApplication.getAppContext().getDrawable(R.drawable.ic_cloud_queue_white_18dp));
        else
            mStatusImage.setImageDrawable(WearApplication.getAppContext().getDrawable(R.drawable.ic_cloud_off_white_18dp));

        if(mInProccess)
            mPRButton.setImageDrawable(WearApplication.getAppContext().getDrawable(R.drawable.ic_pause_circle_outline_white_18dp));
        else
            mPRButton.setImageDrawable(WearApplication.getAppContext().getDrawable(R.drawable.ic_play_circle_outline_white_18dp));

    }


    private void startMeasuring(){
        WearSensorPedometer.getInstance(WearApplication.getAppContext()).connectSensor();
        WearSensorPedometer.getInstance(WearApplication.getAppContext()).addSensorListener(mPedometerListener);
        mInProccess = true;
    }

    private void stopMeasuring(){
        WearSensorPedometer.getInstance(WearApplication.getAppContext()).removeSensorListener(mPedometerListener);
        WearSensorPedometer.getInstance(WearApplication.getAppContext()).disconnectSensor();
        mInProccess = false;
    }

    @Override
    public void onResume () {
        super.onResume();
        refreshStatus();
        refreshView();
    }

    @Override
    public void onPause(){
        super.onPause();
        if(mInProccess)
            stopMeasuring();
    }
}
