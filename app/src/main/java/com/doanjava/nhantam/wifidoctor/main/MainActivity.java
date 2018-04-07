package com.doanjava.nhantam.wifidoctor.main;

import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.doanjava.nhantam.wifidoctor.R;
import com.doanjava.nhantam.wifidoctor.broadcast.NetworkReceiver;
import com.skyfishjy.library.RippleBackground;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RippleBackground ripple;
    private ImageView imgCircleGrandient, imgBoost, imgDetect;
    private TextView txtNetwork, txtOpenWifi, txtCountAppsConnected, txtLastDetect, txtDetect, txtBoost;
    private FrameLayout layoutDeviceConnected;
    private NetworkReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        ripple = findViewById(R.id.content);
        imgCircleGrandient = findViewById(R.id.imgCircleGrandient);
        txtNetwork = findViewById(R.id.txtNetwork);
        txtOpenWifi = findViewById(R.id.txtOpenWifi);
        txtCountAppsConnected = findViewById(R.id.txtCountAppsConnected);
        txtLastDetect = findViewById(R.id.txtLastDetect);
        imgBoost = findViewById(R.id.imgBoost);
        imgDetect = findViewById(R.id.imgDetect);
        txtDetect = findViewById(R.id.txtDetect);
        txtBoost = findViewById(R.id.txtBoost);
        layoutDeviceConnected = findViewById(R.id.layoutDeviceConnected);

        receiver = new NetworkReceiver(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        final IntentFilter filters = new IntentFilter();
        filters.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        filters.addAction("android.net.wifi.STATE_CHANGE");
        registerReceiver(receiver, filters);
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager.isWifiEnabled())
            onNetworkchange(true);
        else onNetworkchange(false);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (receiver.isOrderedBroadcast())
            unregisterReceiver(receiver);
    }

    public void onNetworkchange(boolean isConnected) {
        if (isConnected) {
            ripple.startRippleAnimation();
            imgCircleGrandient.setImageResource(R.drawable.circle_connected);
            txtOpenWifi.setVisibility(View.GONE);
            layoutDeviceConnected.setVisibility(View.VISIBLE);
            txtLastDetect.setVisibility(View.VISIBLE);
            txtCountAppsConnected.setVisibility(View.VISIBLE);
            txtDetect.setTextColor(getResources().getColor(R.color.color_white));
            txtBoost.setTextColor(getResources().getColor(R.color.color_white));
            imgDetect.setImageResource(R.drawable.ic_shield_active);
            imgBoost.setImageResource(R.drawable.ic_rocket_active);
            imgBoost.setOnClickListener(this);
            imgDetect.setOnClickListener(this);
            txtOpenWifi.setOnClickListener(null);
            getWifiInfo();
        } else {
            ripple.stopRippleAnimation();
            imgCircleGrandient.setImageResource(R.drawable.circle_disconnect);
            txtNetwork.setText(getResources().getString(R.string.no_network));
            txtOpenWifi.setVisibility(View.VISIBLE);
            layoutDeviceConnected.setVisibility(View.GONE);
            txtLastDetect.setVisibility(View.INVISIBLE);
            txtCountAppsConnected.setVisibility(View.INVISIBLE);
            txtDetect.setTextColor(getResources().getColor(R.color.color_unactive));
            txtBoost.setTextColor(getResources().getColor(R.color.color_unactive));
            imgDetect.setImageResource(R.drawable.ic_shield_unactive);
            imgBoost.setImageResource(R.drawable.ic_rocket_unactive);
            imgBoost.setOnClickListener(null);
            imgDetect.setOnClickListener(null);
            txtOpenWifi.setOnClickListener(this);
        }
    }

    private void getWifiInfo() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo myWifi = wifiManager.getConnectionInfo();
        if (myWifi != null && !myWifi.getSSID().equals("")) {
            String nameWifi = myWifi.getSSID().replace("\"", "");
            txtNetwork.setText(nameWifi);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.txtOpenWifi:
                enableMyWifi();
                break;
        }
    }

    private void enableMyWifi() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
            txtOpenWifi.setVisibility(View.GONE);
            txtOpenWifi.setOnClickListener(null);
        }
    }
}
