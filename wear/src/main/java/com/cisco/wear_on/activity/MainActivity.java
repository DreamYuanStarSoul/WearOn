package com.cisco.wear_on.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cisco.wear_on.sensor.Measurement;
import com.cisco.wear_on.sensor.MeasurementHeartRate;
import com.cisco.wear_on.sensor.MeasurementPedometer;
import com.cisco.wear_on.R;
import com.cisco.wear_on.networking.RestClient;
import com.cisco.wear_on.sensor.WearSensorListener;
import com.cisco.wear_on.sensor.WearSensorPedometer;


public class MainActivity extends Activity{

    private final static String LOG_TAG = "MainActivity";

    private TextView mTextView;
    private TextView mTextSSID;
    private TextView mTextIP;

    private TextView mTextHeart;
    private ImageButton mRenew;
    private ImageButton mUpload;

    boolean isConnectedSensor = false;

    private RestClient mRstClt;


    private Measurement mHeartRate ;
    private Measurement mPedometer;

    private WearSensorListener mHeartRateListener = new WearSensorListener(){
        @Override
        public void onStatusChanged(int value, int accuracy){

            mHeartRate = new MeasurementHeartRate(value);
            mTextHeart.setText(String.valueOf(mHeartRate.getValue()));
            mRstClt.AsyncPostHeartRate(mHeartRate);
        }
    };

    private WearSensorListener mPedometerListener = new WearSensorListener() {
        @Override
        public void onStatusChanged(int value, int accuracy) {
            mPedometer = new MeasurementPedometer(value);
            mTextHeart.setText(String.valueOf(mPedometer.getValue()));
            mRstClt.AsyncPostPedometer(mPedometer);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextHeart = (TextView) findViewById(R.id.text_blank);
        mRenew = (ImageButton) findViewById(R.id.renew);
        mUpload = (ImageButton) findViewById(R.id.upload);
        //mRstClt = new RestClient(this);

        //BodySensor.getInstance(MainActivity.this);

        mRenew .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnectedSensor == false) {
                    //WearSensorHeartrate.getInstance(MainActivity.this).connectSensor();
                    //WearSensorHeartrate.getInstance(MainActivity.this).addSensorListener(mHeartRateListener);
                    WearSensorPedometer.getInstance(MainActivity.this).connectSensor();
                    WearSensorPedometer.getInstance(MainActivity.this).addSensorListener(mPedometerListener);
                    mRenew.setImageDrawable(MainActivity.this.getResources().getDrawable(R.drawable.ic_cached_white_48dp));
                    isConnectedSensor = true;
                } else {
                    //WearSensorHeartrate.getInstance(MainActivity.this).removeSensorListener(mHeartRateListener);
                    //WearSensorHeartrate.getInstance(MainActivity.this).disconnectSensor();
                    WearSensorPedometer.getInstance(MainActivity.this).removeSensorListener(mPedometerListener);
                    WearSensorPedometer.getInstance(MainActivity.this).disconnectSensor();
                    mRenew.setImageDrawable(MainActivity.this.getResources().getDrawable(R.drawable.ic_autorenew_white_48dp));
                    isConnectedSensor = false;
                }
            }
        });


        mUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //SocketClient client = new SocketClient();
                //client.connect(mHeartRate);

                mRstClt.AsyncRegisterDevice();
            }
        });

        /*
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = (TextView) stub.findViewById(R.id.text);
                mTextSSID = (TextView) stub.findViewById(R.id.text_ssid);
                mTextIP = (TextView) stub.findViewById(R.id.text_ip);
                //get connect status.

                if(Utils.Network.isWifiConnected(MainActivity.this)){
                    mTextView.setText(new String("Wifi Connected."));
                    mTextSSID.setText(Utils.Network.Wifi_getSSID(MainActivity.this));
                    mTextIP.setText(String.valueOf(Utils.Network.Wifi_ipAddress(MainActivity.this)));
                }else{
                    mTextView.setText(new String("Wifi Disconnected!"));
                    mTextSSID.setText(new String("None"));
                    mTextIP.setText(new String("None"));
                }

            }
        });
        */
        /*

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        CardFragment cardFragment = CardFragment.create(getString(R.string.title),
                getString(R.string.des),
                R.drawable.heart_beat_icon);
        fragmentTransaction.add(R.id.frame_layout, cardFragment);
        fragmentTransaction.commit();
        */
       // BodySensor.getInstance(MainActivity.this).initSensor();
    }

    @Override
    protected void onResume(){
        super.onResume();
        //WearSensorHeartrate.getInstance(MainActivity.this).connectSensor();
        //WearSensorHeartrate.getInstance(MainActivity.this).addSensorListener(mHeartRateListener);
        //WearSensorPedometer.getInstance(MainActivity.this).connectSensor();
        //WearSensorPedometer.getInstance(MainActivity.this).addSensorListener(mPedometerListener);
        //mRenew.setImageDrawable(MainActivity.this.getResources().getDrawable(R.drawable.ic_cached_white_48dp));
        //isConnectedSensor = true;
    }

    @Override
    protected void onPause(){
        super.onPause();
        //WearSensorHeartrate.getInstance(MainActivity.this).removeSensorListener(mHeartRateListener);
        //WearSensorHeartrate.getInstance(MainActivity.this).disconnectSensor();
        //WearSensorPedometer.getInstance(MainActivity.this).removeSensorListener(mPedometerListener);
        //WearSensorPedometer.getInstance(MainActivity.this).disconnectSensor();
        //mRenew.setImageDrawable(MainActivity.this.getResources().getDrawable(R.drawable.ic_autorenew_white_48dp));
        //isConnectedSensor = false;
    }



}
