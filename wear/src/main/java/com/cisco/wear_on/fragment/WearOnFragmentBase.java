package com.cisco.wear_on.fragment;

import android.app.Fragment;
import android.support.wearable.view.CardFragment;

import com.cisco.wear_on.networking.LoginData;
import com.cisco.wear_on.networking.RestServerEventListener;
import com.cisco.wear_on.networking.SensorRegData;

import java.util.ArrayList;

public class WearOnFragmentBase extends Fragment implements RestServerEventListener{

    @Override
    public void onLoginDone(LoginData.ResponseData data){
        //ignore
    }
    @Override
    public void onLoginFailed(int errCode){
        //ignore
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
     public void onOccupyResult(boolean success,int errCode){
        //ignore
    }
    @Override
     public void onReleaseResult(boolean success, int errCode) {
        //ignore
    }
    @Override
    public void onDataPostDone(){
        //ignore
    }
    @Override
    public void onDataPostFailed(int errCode){
        //ignore
    }
}
