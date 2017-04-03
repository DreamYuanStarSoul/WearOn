package com.cisco.wear_on.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cisco.wear_on.R;
import com.cisco.wear_on.Utils;

/**
 * Created by cymszb on 16-1-17.
 */
public class CloudCareIntroFragment extends WearOnFragmentBase{
    final static String LOG_TAG = "com.cisco.wear_on";

    //private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        Utils.S_Log.d(LOG_TAG,"CloudCareIntroFragment created");
        super.onCreate(savedInstanceState);
        //mContext = this.getActivity();



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        return inflater.inflate(R.layout.cloud_care_intr, container, false);
    }
}
