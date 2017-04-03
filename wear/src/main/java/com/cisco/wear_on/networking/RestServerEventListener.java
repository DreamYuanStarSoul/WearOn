package com.cisco.wear_on.networking;

import java.util.ArrayList;

/**
 * Created by cymszb on 16-1-15.
 */
public interface RestServerEventListener {
    abstract public void onLoginDone(LoginData.ResponseData data);
    abstract public void onLoginFailed(int errCode);

    abstract public void onSensorRegDone(ArrayList<SensorRegData.ResponseData> reg_sensors);
    abstract public void onSensorRegFaild(int errCode);

    abstract public void onOccupyResult(boolean success, int errCode);
    abstract public void onReleaseResult(boolean success, int errCode);


    abstract public void onDataPostDone();
    abstract public void onDataPostFailed(int errCode);
}
