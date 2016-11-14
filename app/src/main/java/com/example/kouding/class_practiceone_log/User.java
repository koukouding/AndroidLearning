package com.example.kouding.class_practiceone_log;

/**
 * Created by kouding on 2016/11/3.
 */

import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;

public class User {
    private String mId;
    private String mPwd;
    private static final String JSON_ID = "user_id";
    private static final String JSON_PWD = "user_pwd";
    private static final String TAG = "User";


public void setName(String name)
{
    this.mId=name;
}
    public void setPwd(String pwd)
    {
        this.mPwd=pwd;
    }
    public String getName() {
        return mId;
    }

    public String getPwd() {
        return mPwd;
    }
}