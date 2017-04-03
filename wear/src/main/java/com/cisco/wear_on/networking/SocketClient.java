package com.cisco.wear_on.networking;

import com.cisco.wear_on.Utils;

import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.DataOutputStream;

/**
 * Created by cymszb on 15-11-17.
 */
public class SocketClient {

    //Socket mSocket = null;


    private int mCurHeartRate;

    private Thread mNetworkThread = new Thread(){
      public void run(){

          String sentence = "Current HeartRate:" + mCurHeartRate + "\n";//new String("Hello CLG");
          String modifiedSentence; //= new String("I am google wear");
          try {
              BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
              Socket clientSocket = new Socket("ubuntu", 7272);
              //if(mSocket == null){
              //    mSocket = new Socket("ubuntu", 7272);
              //}


              DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
              BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

              //sentence = inFromUser.readLine();

              outToServer.writeBytes(sentence + '\n');

              modifiedSentence = inFromServer.readLine();
              //System.out.println("FROM SERVER: " + modifiedSentence);
              Utils.S_Log.d("SocketClient",modifiedSentence);
              outToServer.writeBytes("bye");

              clientSocket.close();
          }catch(Exception ex){
              ex.printStackTrace();
              Utils.S_Log.d("SocketClient", ex.getMessage());
          }
      }
    };

    public void connect(int rate){
        mCurHeartRate = rate;
        mNetworkThread.start();
    }

    public void disconnect(){

    }
}
