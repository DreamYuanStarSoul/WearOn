package com.cisco.wear_on;

import android.app.Application;
import android.content.Context;

/**
 * Created by cymszb on 15-12-16.
 */
public class WearApplication extends Application {

    private final static String TAG = "com.cisco.wear_on";

    private static Context mContext;


    @Override
    public void onCreate(){
        super.onCreate();
        Utils.S_Log.d(TAG,"WearApplication Created.");
        mContext = getApplicationContext();
    }

    public static Context getAppContext(){
        return mContext;
    }
}
