package com.cisco.wear_on.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.support.wearable.view.DelayedConfirmationView;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.lang.ref.WeakReference;

import com.cisco.wear_on.R;
import com.cisco.wear_on.Ringtone;
import com.cisco.wear_on.Utils;
import com.cisco.wear_on.WearApplication;
import com.cisco.wear_on.networking.BloodMeasurementData;
import com.cisco.wear_on.networking.LoginData;
import com.cisco.wear_on.networking.RestClient;
import com.cisco.wear_on.networking.RestServerEventListener;

/**
 * Created by cymszb on 16-1-14.
 */
public class WelcomeFragment extends WearOnFragmentBase
        implements DelayedConfirmationView.DelayedConfirmationListener {

    final static String LOG_TAG = "com.cisco.wear_on";

    private View mFragmentView;
    private DelayedConfirmationView mDelayedView;
    private TextView mWelcomeText;
    //private ImageView mWelcomeStatus;
    private TextView mWelcomeStatus;

    private RestClient mLoginClient;


    private UiHandler mUiHandler = new UiHandler(this);
    public static final int UI_ON_LOGIN_DONE = 1;
    public static final int UI_ON_LOGIN_FAILED = 2;


    private static class UiHandler extends Handler {

        private WeakReference<WelcomeFragment> mOuter;

        public UiHandler(WelcomeFragment outer){
            mOuter = new WeakReference<WelcomeFragment>(outer);
        }

        @Override
        public void handleMessage(Message msg) {
            WelcomeFragment outer = mOuter.get();

            switch (msg.what) {

                case UI_ON_LOGIN_DONE:{
                    outer.mWelcomeText.setText(R.string.welcome_connected);
                    //mWelcomeStatus.setImageDrawable(WearApplication.getAppContext().getDrawable(R.drawable.ic_cloud_queue_white_18dp));
                    outer.mWelcomeStatus.setCompoundDrawablesWithIntrinsicBounds(WearApplication.getAppContext().getDrawable(R.drawable.ic_cloud_queue_white_18dp), null, null, null);
                    outer.mDelayedView.reset();

                }
                break;
                case UI_ON_LOGIN_FAILED:{
                    outer.mWelcomeText.setText(R.string.welcome_connected);
                    //mWelcomeStatus.setImageDrawable(WearApplication.getAppContext().getDrawable(R.drawable.ic_cloud_off_white_18dp));
                    outer.mWelcomeStatus.setCompoundDrawablesWithIntrinsicBounds(WearApplication.getAppContext().getDrawable(R.drawable.ic_cloud_off_white_18dp), null, null, null);
                    outer.mDelayedView.reset();

                }
                break;

            }

        }

    }


    public WelcomeFragment(){
        super();
        Utils.S_Log.d(LOG_TAG, "WelcomeFragment(),constructor");
        mLoginClient = RestClient.getInstance(WearApplication.getAppContext());
        //mLoginClient.addRestServerEventListener(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        super.onCreate(savedInstanceState);

        //mLoginClient = RestClient.getInstance(WearApplication.getAppContext());

        //mActivityRunOn = this.getActivity();
        //Utils.S_Log.d(LOG_TAG, "Welcome Fragment created:" + this + "," + mLoginClient);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        Utils.S_Log.d(LOG_TAG, "onCreateView()");
        mFragmentView = inflater.inflate(R.layout.welcome, container, false);
        initFragmentView(mFragmentView);

        return mFragmentView;
    }

    private void initFragmentView(View v){
        mDelayedView = (DelayedConfirmationView)v.findViewById(R.id.delayed_confirm);
        mWelcomeText = (TextView)v.findViewById(R.id.welcome_text);
        mWelcomeStatus = (TextView)v.findViewById(R.id.welcome_status);
        mDelayedView.setListener(this);
    }

    @Override
    public void onTimerFinished(View view) {
        // User didn't cancel, perform the action
        Utils.S_Log.d(LOG_TAG,"onTimerFinished..");
        mDelayedView.start();

    }

    @Override
    public void onTimerSelected(View view) {
        // User canceled, abort the action
        Utils.S_Log.d(LOG_TAG, "onTimerSelected!!");
        //mDelayedView.start();
    }

    @Override
    public void onLoginDone(LoginData.ResponseData data){
        Utils.S_Log.d(LOG_TAG,"onLoginDone.");
        super.onLoginDone(data);
        Utils.S_Log.d(LOG_TAG, "LoginResponseData:" + data.UserId + ","
                + data.CreatedTime + ","
                + data.LonginId + ","
                + data.Ttl);
        /*
        mActivityRunOn.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mWelcomeText.setText(R.string.welcome_connected);
                mWelcomeStatus.setImageDrawable(WearApplication.getAppContext().getDrawable(R.drawable.ic_cloud_queue_white_18dp));
                mDelayedView.reset();
            }
        });
        */
        updateOnLoginDone();
    }

    @Override
    public void onLoginFailed(int errCode){
        Utils.S_Log.d(LOG_TAG,"onLoginFailed.");
        super.onLoginFailed(errCode);
        /*
        mActivityRunOn.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mWelcomeText.setText(R.string.welcome_connected);
                mWelcomeStatus.setImageDrawable(WearApplication.getAppContext().getDrawable(R.drawable.ic_cloud_off_white_18dp));
                mDelayedView.reset();
            }
        });
        */
        updateOnLoginFailed();
    }


    @Override
    public void onResume () {
        super.onResume();

        mLoginClient.addRestServerEventListener(this);

        if (!mLoginClient.isOnline()) {
            Utils.S_Log.d(LOG_TAG,"is offline.");
            mDelayedView.setTotalTimeMs(5000);
            mDelayedView.start();
            mWelcomeText.setText(WearApplication.getAppContext().getText(R.string.welcome_connecting));
            mLoginClient.AsyncLogin(WearApplication.getAppContext().getString(R.string.user_name),
                                    WearApplication.getAppContext().getString(R.string.password));
        }else{
            Utils.S_Log.d(LOG_TAG, "is online.");
            mWelcomeText.setText(R.string.welcome_connected);
            //mWelcomeStatus.setImageDrawable(WearApplication.getAppContext().getDrawable(R.drawable.ic_cloud_queue_white_18dp));
            mWelcomeStatus.setCompoundDrawablesWithIntrinsicBounds(WearApplication.getAppContext().getDrawable(R.drawable.ic_cloud_queue_white_18dp), null, null, null);

        }

    }

    @Override
    public void onPause(){
        super.onPause();
        mLoginClient.removeServerEventListener(this);
        mDelayedView.reset();
        mDelayedView.setListener(null);
    }

    public void updateOnLoginDone(){
        mUiHandler.sendEmptyMessage(UI_ON_LOGIN_DONE);
    }

    public void updateOnLoginFailed(){
        mUiHandler.sendEmptyMessage(UI_ON_LOGIN_FAILED);
    }


}
