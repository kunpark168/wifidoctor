package com.doanjava.nhantam.wifidoctor.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.doanjava.nhantam.wifidoctor.main.MainActivity;

/**
 * Created by Anh on 07/04/2018.
 */

public class NetworkReceiver extends BroadcastReceiver {

    private MainActivity mContext;

    public NetworkReceiver(MainActivity mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] info = connectivityManager.getAllNetworkInfo();//All network info

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();//Active network info

        //Active network info
        if (networkInfo != null && networkInfo.isConnected()) {
            mContext.onNetworkchange(true);
        }else if (networkInfo != null && !networkInfo.isConnected()) {
            mContext.onNetworkchange(false);
        }
    }
}
