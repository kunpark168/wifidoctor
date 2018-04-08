package com.doanjava.nhantam.wifidoctor.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.doanjava.nhantam.wifidoctor.network.ClientScanResult;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Anh on 08/04/2018.
 */

public class PreferenceManager {

    public final static String APP_NAME = "WIFI_DOCTOR";
    public static PreferenceManager instance;
    private static Context mContext;
    private SharedPreferences mPref;
    private SharedPreferences.Editor editor;

    private final static String REAL_TIME_PROTECTION = "REAL_TIME_PROTECTION";
    private final static String WIFI_DETECTION = "WIFI_DETECTION";
    private final static String WIFI_BOOST = "WIFI_BOOST";
    private final static String WEATHER = "WEATHER";
    private final static String SUPER_BOOST = "SUPER_BOOST";
    private final static String LIST_DEVICE = "LIST_DEVICE";


    public static PreferenceManager getInstance(Context context) {
        mContext = context;
        if (instance == null){
            instance = new PreferenceManager(context);
        }
        return instance;
    }

    public PreferenceManager(Context mContext) {
        mPref = mContext.getSharedPreferences(APP_NAME, Context.MODE_PRIVATE);
        editor = mPref.edit();
    }

    public void setRealTimeProtection (boolean b){
        editor.putBoolean(REAL_TIME_PROTECTION, b);
        editor.commit();
    }

    public boolean getRealTimeProtection (){
        return mPref.getBoolean(REAL_TIME_PROTECTION, true);
    }

    public void setWifiDetection (boolean b){
        editor.putBoolean(WIFI_DETECTION, b);
        editor.commit();
    }

    public boolean getWifiDetection (){
        return mPref.getBoolean(WIFI_DETECTION, true);
    }

    public void setWifiBoost (boolean b){
        editor.putBoolean(WIFI_BOOST, b);
        editor.commit();
    }

    public boolean getWifiBoost(){
        return mPref.getBoolean(WIFI_BOOST, true);
    }

    public void setWeather (boolean b){
        editor.putBoolean(WEATHER, b);
        editor.commit();
    }

    public boolean getWeather (){
        return mPref.getBoolean(WEATHER, true);
    }

    public void setSuperBoost (boolean b){
        editor.putBoolean(SUPER_BOOST, b);
        editor.commit();
    }

    public boolean getSuperBoost (){
        return mPref.getBoolean(SUPER_BOOST, true);
    }

    public void setDeviceList (ArrayList<ClientScanResult> arrCountry){
        Gson gson = new Gson();
        String json = gson.toJson(arrCountry);
        editor.putString(LIST_DEVICE, json);
        editor.commit();
    }

    public ArrayList<ClientScanResult> getDeviceList (){
        Gson gson = new Gson();
        String json = mPref.getString(LIST_DEVICE, null);
        Type type = new TypeToken<ArrayList<ClientScanResult>>() {}.getType();
        return gson.fromJson(json, type);
    }
}
