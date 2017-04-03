package com.cisco.wear_on.activity;

import android.app.Activity;
import android.os.Bundle;

import android.app.FragmentManager;
import android.app.FragmentTransaction;

import android.support.wearable.view.CardFragment;

import android.support.wearable.view.GridViewPager;

import com.cisco.wear_on.R;
import com.cisco.wear_on.Utils;
import com.cisco.wear_on.WearApplication;
import com.cisco.wear_on.fragment.FitGridPagerAdapter;
import com.cisco.wear_on.networking.LoginData;
import com.cisco.wear_on.networking.RestClient;
import com.cisco.wear_on.networking.RestServerEventListener;
import com.cisco.wear_on.networking.SensorRegData;

import java.net.URISyntaxException;
import java.util.ArrayList;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by cymszb on 16-1-11.
 */
public class StepsActivity extends Activity implements RestServerEventListener {


    private final static String TAG = "com.cisco.wear_on";


    //private RestClient mLoginClient;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container);

        Utils.S_Log.d(TAG,"StepsActivity Created." + this);


        final GridViewPager pager = (GridViewPager) findViewById(R.id.pager);

        //pager.setAdapter(new FitGridPagerAdapter(this, getFragmentManager()));
        pager.setAdapter(new FitGridPagerAdapter(WearApplication.getAppContext(), getFragmentManager()));
        //mLoginClient = RestClient.getInstance(WearApplication.getAppContext());
        //mLoginClient.addRestServerEventListener(this);


/*
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        CardFragment cardFragment = CardFragment.create(getString(R.string.title),
                getString(R.string.des),
                R.drawable.heart_beat_icon);
        fragmentTransaction.add(R.id.frame_layout, cardFragment);
        fragmentTransaction.commit();
  */
    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onLoginDone(LoginData.ResponseData data) {

    }

    @Override
    public void onLoginFailed(int errCode) {

    }

    @Override
    public void onSensorRegDone(ArrayList<SensorRegData.ResponseData> reg_sensors) {
        //ignore
    }

    @Override
    public void onSensorRegFaild(int errCode) {
        //ignore
    }

    @Override
    public void onOccupyResult(boolean success, int errCode) {
        //ignore
    }

    @Override
    public void onReleaseResult(boolean success, int errCode) {
        //ignore
    }

    @Override
    public void onDataPostDone() {
        //ignore
    }

    @Override
    public void onDataPostFailed(int errCode) {
        //ignore
    }
}


