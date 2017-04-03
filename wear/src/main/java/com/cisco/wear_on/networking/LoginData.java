package com.cisco.wear_on.networking;

/**
 * Created by cymszb on 16-1-18.
 */
public class LoginData {

    public static class RequestData {
        public RequestData(){
            Username = null;
            Password = null;
        }

        public RequestData(String user, String passwd){
            Username = user;
            Password = passwd;
        }
        public String Username;
        public String Password;
    }

    public  static class ResponseData {

        public ResponseData() {
            LonginId = null;
            Ttl = -1;
            CreatedTime = null;
            UserId = -1;
        }

        public ResponseData(String loginId, long ttl, String createdTime, int userId) {
            LonginId = loginId;
            Ttl = ttl;
            CreatedTime = createdTime;
            UserId = userId;
        }

        public String LonginId;
        public long Ttl;
        public String CreatedTime;
        public int UserId;
    }
}
