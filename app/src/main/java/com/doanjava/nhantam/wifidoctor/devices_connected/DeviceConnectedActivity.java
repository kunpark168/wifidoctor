package com.doanjava.nhantam.wifidoctor.devices_connected;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.doanjava.nhantam.wifidoctor.R;
import com.doanjava.nhantam.wifidoctor.Utils.Constans;
import com.doanjava.nhantam.wifidoctor.Utils.PreferenceManager;
import com.doanjava.nhantam.wifidoctor.more.MoreActivity;
import com.doanjava.nhantam.wifidoctor.network.ClientScanResult;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.NativeAppInstallAd;
import com.google.android.gms.ads.formats.NativeAppInstallAdView;

import java.util.ArrayList;
import java.util.List;

public class DeviceConnectedActivity extends AppCompatActivity {

    private TextView txtCountDevice, txtwifName;
    private FrameLayout layoutAdsDetail;
    private ImageView imgBackDetailWifi;
    private RecyclerView recyclerViewDeviceConnected;
    private ArrayList<ClientScanResult> arrDevice;
    private DevicesConnectedAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_connected);
        initView ();
        initData ();
    }

    private void initData() {
        String wifiName = getIntent().getStringExtra(Constans.WIFI_NAME);
        arrDevice = PreferenceManager.getInstance(this).getDeviceList();
        if (wifiName != null && !wifiName.equals(""))
           txtwifName.setText(wifiName);
        if (arrDevice != null && arrDevice.size() > 0){
            String text = arrDevice.size() + " devices are sharing network";
            txtCountDevice.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
        }

        adapter = new DevicesConnectedAdapter(this, arrDevice);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewDeviceConnected.setLayoutManager(layoutManager);
        recyclerViewDeviceConnected.setAdapter(adapter);



    }

    private void initView() {
        txtCountDevice = findViewById(R.id.txtCountAppsConnectedDetail);
        txtwifName = findViewById(R.id.txtWifiName);
        layoutAdsDetail = findViewById(R.id.layoutAdsDetailWifi);
        imgBackDetailWifi= findViewById(R.id.imgBackDetailWifi);
        recyclerViewDeviceConnected = findViewById(R.id.recyclerViewDeviceConnected);

        imgBackDetailWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadNativeAd();
    }

    private void loadNativeAd() {


        AdLoader.Builder builder = new AdLoader.Builder(this, getResources().getString(R.string.ADMOB_AD_UNIT_ID));
        AdLoader adLoader = builder.forAppInstallAd(new NativeAppInstallAd.OnAppInstallAdLoadedListener() {
            @Override
            public void onAppInstallAdLoaded(NativeAppInstallAd ad) {
                // An app install ad loaded successfully, call this method again to
                // load the next ad.
                NativeAppInstallAdView adView = (NativeAppInstallAdView) LayoutInflater.from(DeviceConnectedActivity.this)
                        .inflate(R.layout.ad_app_install_2, null);
                populateAppInstallAdView(ad, adView);
                layoutAdsDetail.removeAllViews();
                layoutAdsDetail.addView(adView);


            }
        }).withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {
                // A native ad failed to load. Call this method again to load
                // the next ad in the items list.
                android.util.Log.e("MainActivity", "The previous native ad failed to load. Attempting to"
                        + " load another.");
                loadNativeAd();
            }
        }).build();
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        adLoader.loadAd(adRequest);
    }
    private void populateAppInstallAdView(NativeAppInstallAd nativeAppInstallAd,
                                          NativeAppInstallAdView adView) {

        adView.setHeadlineView(adView.findViewById(R.id.appinstall_headline2));
        adView.setBodyView(adView.findViewById(R.id.appinstall_body2));
        adView.setCallToActionView(adView.findViewById(R.id.appinstall_call_to_action2));
        adView.setIconView(adView.findViewById(R.id.appinstall_app_icon2));

        // Some assets are guaranteed to be in every NativeAppInstallAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAppInstallAd.getHeadline());
        ((TextView) adView.getBodyView()).setText(nativeAppInstallAd.getBody());
        ((Button) adView.getCallToActionView()).setText(nativeAppInstallAd.getCallToAction());
        ((ImageView) adView.getIconView()).setImageDrawable(
                nativeAppInstallAd.getIcon().getDrawable());

        ((Button) adView.getCallToActionView()).setText(nativeAppInstallAd.getCallToAction().toString());

        // Assign native ad object to the native view.
        adView.setNativeAd(nativeAppInstallAd);
    }
}
