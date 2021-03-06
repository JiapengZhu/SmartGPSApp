package com.jp.smartgpsapp.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by jiapeng-zhu on 11/12/15.
 */
public class SessionManager {
    // LogCat tag
    private static String TAG = SessionManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    SharedPreferences.Editor editor;
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "AndroidPref";

    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";
    private static final String KEY_IS_CONNECTED = "isBtOn";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setLogin(boolean isLoggedIn) {

        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);

        // commit changes
        editor.commit();

        Log.d(TAG, "User login session modified!");
    }

    public void setConnected(boolean isConnected){
        editor.putBoolean(KEY_IS_CONNECTED, isConnected);
        editor.commit();
        Log.d(TAG, "Device is connected");
    }

    public boolean isConnected(){
        System.out.println("isConnected: "+pref.getBoolean(KEY_IS_CONNECTED,false));
        return pref.getBoolean(KEY_IS_CONNECTED, false);
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }
}