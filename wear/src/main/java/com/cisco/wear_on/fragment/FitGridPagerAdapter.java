package com.cisco.wear_on.fragment;

import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.support.wearable.view.FragmentGridPagerAdapter;

import android.content.Context;
import android.app.FragmentManager;


import com.cisco.wear_on.R;
import com.cisco.wear_on.Utils;


/**
 * Created by cymszb on 16-1-14.
 */
public class FitGridPagerAdapter extends FragmentGridPagerAdapter {
    final static String LOG_TAG = "com.cisco.wear_on";

    private Context mContext;

    private Fragment mFragmentPages[][];

    public FitGridPagerAdapter(Context ctx, FragmentManager fm) {
        super(fm);
        mContext = ctx;
        init();
    }

    private void init(){
        Utils.S_Log.d(LOG_TAG,"FitGridPagerAdapter init().");

        mFragmentPages = new Fragment[4][];
        mFragmentPages[0] = new Fragment[1];
        mFragmentPages[0][0] = new WelcomeFragment();
        mFragmentPages[1] = new Fragment[2];
        mFragmentPages[1][0] = new HeartRateIntrFragment();
        mFragmentPages[1][1] = new HeartRateMeasurementFragment();
        mFragmentPages[2] = new Fragment[2];
        mFragmentPages[2][0] = new PedometerIntrFragment();
        mFragmentPages[2][1] = new PedometerMeasurementFragment();
        mFragmentPages[3] = new Fragment[2];
        mFragmentPages[3][0] = new CloudCareIntroFragment();
        mFragmentPages[3][1] = new BodyWeightMeasurementFragment();


    }


    @Override
    public Fragment getFragment(int row, int col) {

        return  mFragmentPages[row][col];
        //return new WelcomeFragment();
    }


    @Override
    public int getRowCount() {
        //Utils.S_Log.d(LOG_TAG,"Row:" + mFragmentPages.length);
        return  mFragmentPages.length;

    }

    @Override
    public int getColumnCount(int i) {
        //Utils.S_Log.d(LOG_TAG,"col of row " + i + " :" + mFragmentPages[i].length);
        return mFragmentPages[i].length;
        //return 1;
    }

    @Override
    public Drawable getBackgroundForRow(int row){
        if(row == 0) return mContext.getDrawable(R.drawable.bk_welcome);
        if(row == 1) return mContext.getDrawable(R.drawable.bk_heart);
        if(row == 2) return mContext.getDrawable(R.drawable.bk_sports);
        if(row == 3) return mContext.getDrawable(R.drawable.bk_iot);
        else return mContext.getDrawable(R.drawable.bk_welcome);
    }
}
