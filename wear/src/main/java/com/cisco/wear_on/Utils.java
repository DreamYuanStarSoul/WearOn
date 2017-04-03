package com.cisco.wear_on;


import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;


import android.os.Build;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import  android.net.wifi.*;
import android.util.Log;

/**
 * Created by cymszb on 15-11-26.
 */
public class Utils {
    public static final String LOG_TAG = Utils.class.getName();

    public static final boolean IS_DEBUG = true;


    public static class S_Log{


        public static void d(String tag, String log){
            if(IS_DEBUG) Log.d(tag, log);
        }
        public static void e(String tag, String log){
            if(IS_DEBUG) Log.e(tag, log);
        }
        public static void i(String tag, String log){
            if(IS_DEBUG) Log.i(tag, log);
        }
    }

    public static boolean isEmulator(){
        //S_Log.d(LOG_TAG,Build.MANUFACTURER+ "," + Build.DEVICE + "," + Build.HOST +  "," + Build.MODEL +  "," + Build.PRODUCT);

        if( Build.MODEL.equals(new String("sdk_google_aw_armv7"))) return true;
        return false;
    }


    public static class Network{
        public static boolean isConnect(Context context) {

            try {
                ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                if (connectivity != null) {
                    NetworkInfo info = connectivity.getActiveNetworkInfo();

                    if (info != null&& info.isConnected()) {
                        if (info.getState() == NetworkInfo.State.CONNECTED) {
                            return true;
                        }
                    }
                }
            }catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        public static boolean isWifiConnected(Context conext){

            try{
                WifiManager wificonnectivity = (WifiManager)conext.getSystemService(Context.WIFI_SERVICE);
                if(wificonnectivity != null && wificonnectivity.isWifiEnabled()){
                    return true;
                }

            }catch (Exception e){
                e.printStackTrace();
            }
            return false;
        }

        public static String Wifi_getSSID(Context context){

            WifiManager wificonnectivity = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
            if(wificonnectivity != null && wificonnectivity.isWifiEnabled()){
                WifiInfo wifiInfo = wificonnectivity.getConnectionInfo();
                return wifiInfo.getSSID();
            }

            return new String("None");
        }

        public static String Wifi_ipAddress(Context context){

            WifiManager wificonnectivity = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
            if(wificonnectivity != null && wificonnectivity.isWifiEnabled()){
                WifiInfo wifiInfo = wificonnectivity.getConnectionInfo();

                int ip = wifiInfo.getIpAddress();


                return String.format("%d.%d.%d.%d",
                        (ip & 0xff),
                        (ip >> 8 & 0xff),
                        (ip >> 16 & 0xff),
                        (ip >> 24 & 0xff));
            }

            return new String("None");
        }

        public static String Wifi_getMac(Context context){
            WifiManager wificonnectivity = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
            if(wificonnectivity != null){
                WifiInfo wifiInfo = wificonnectivity.getConnectionInfo();
                return wifiInfo.getMacAddress();
            }

            return new String("None");
        }



        public static ArrayList<String> getIpv4Addresses() {
            ArrayList<String> ips = new ArrayList<String>();
            try {
                for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                    NetworkInterface intf = en.nextElement();
                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();  ){
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()&& inetAddress instanceof Inet4Address) {
                            ips.add( inetAddress.getHostAddress());
                        }
                    }
                }
            } catch (SocketException ex) {
                Log.e(LOG_TAG, ex.toString());
            }
            return ips;
        }

        public static ArrayList<String> getIpv6Addresses() {
            ArrayList<String> ips = new ArrayList<String>();
            try {
                for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                    NetworkInterface intf = en.nextElement();
                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();  ){
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()&& inetAddress instanceof Inet6Address) {
                            ips.add( inetAddress.getHostAddress());
                        }
                    }
                }
            } catch (SocketException ex) {
                Log.e(LOG_TAG, ex.toString());
            }
            return ips;
        }

    }
}
