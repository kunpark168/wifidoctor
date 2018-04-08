package com.doanjava.nhantam.wifidoctor.main;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.doanjava.nhantam.wifidoctor.R;
import com.doanjava.nhantam.wifidoctor.Utils.Constans;
import com.doanjava.nhantam.wifidoctor.Utils.PreferenceManager;
import com.doanjava.nhantam.wifidoctor.broadcast.NetworkReceiver;
import com.doanjava.nhantam.wifidoctor.devices_connected.DeviceConnectedActivity;
import com.doanjava.nhantam.wifidoctor.more.MoreActivity;
import com.doanjava.nhantam.wifidoctor.network.ClientScanResult;
import com.skyfishjy.library.RippleBackground;
import java.lang.ref.WeakReference;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RippleBackground ripple;
    private ImageView imgCircleGrandient, imgBoost, imgDetect;
    private TextView txtNetwork, txtOpenWifi, txtCountAppsConnected, txtLastDetect, txtDetect, txtBoost, txtCountDeviceConnected;
    private FrameLayout layoutDeviceConnected;
    private NetworkReceiver receiver;
    private ImageView rippleMore;
    private ArrayList<ClientScanResult> arrDevice;
    private String nameWifi, deviceIp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        getApplicationsRunningOnBackground();
    }

    private void getApplicationsRunningOnBackground() {
        /*ActivityManager activityManager = (ActivityManager) getApplicationContext()
                .getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager
                .getRunningAppProcesses();
        List<ActivityManager.RunningTaskInfo> runningTaskInfo = activityManager.getRunningTasks(10);

        ComponentName componentInfo = runningTaskInfo.get(0).topActivity;
        for (int idx = 0; idx < procInfos.size(); idx++) {
            Toast.makeText(this, procInfos.get(idx).processName + "\n", Toast.LENGTH_SHORT).show();
        }*/
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> pkgAppsList = getApplicationContext().getPackageManager().queryIntentActivities(mainIntent, 0);


        List<ApplicationInfo> appss = getPackageManager().getInstalledApplications(0);
        List<ApplicationInfo> appssa = new ArrayList<>();
        for(ApplicationInfo app : appss) {
            if((app.flags & (ApplicationInfo.FLAG_UPDATED_SYSTEM_APP | ApplicationInfo.FLAG_SYSTEM)) > 0) {
                // It is a system app
            } else {
                // It is installed by the user
                appssa.add(app);
            }
        }
        Log.d("aa", appssa + "");

        isAppIsInBackground(this);
        ArrayList<String> packageNames = new ArrayList<String>();
        PackageManager p = getPackageManager();
        final List<PackageInfo> apps = p.getInstalledPackages(PackageManager.GET_PERMISSIONS);
        for (PackageInfo packageInfo : apps) {
            if (packageInfo.requestedPermissions == null || isSystemPackage(packageInfo))
                continue;
            for (String permission : packageInfo.requestedPermissions) {
                if (TextUtils.equals(permission, android.Manifest.permission.INTERNET)) {
                    packageNames.add(packageInfo.packageName);
                    break;
                }
            }
        }

    }

    private boolean isSystemPackage(PackageInfo pkgInfo) {
        return ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) ? true
                : false;
    }

    private boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
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
        rippleMore = findViewById(R.id.rippleMore);
        layoutDeviceConnected = findViewById(R.id.layoutDeviceConnected);
        txtCountDeviceConnected= findViewById(R.id.txtCountDeviceConnected);

        arrDevice = new ArrayList<>();

        receiver = new NetworkReceiver(this);
        rippleMore.setOnClickListener(this);
        txtCountDeviceConnected.setOnClickListener(this);
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
        new NetworkSniffTask(MainActivity.this).execute();

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
            arrDevice.clear();
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
            nameWifi = myWifi.getSSID().replace("\"", "");
            txtNetwork.setText(nameWifi);
            deviceIp = Formatter.formatIpAddress(myWifi.getIpAddress());
        }

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.txtOpenWifi:
                enableMyWifi();
                break;
            case R.id.rippleMore:
                ripple.stopRippleAnimation();
                startActivity(new Intent(MainActivity.this, MoreActivity.class));
                break;
            case R.id.txtCountDeviceConnected :
                Intent intent = new Intent(MainActivity.this, DeviceConnectedActivity.class);
                intent.putExtra(Constans.WIFI_NAME, nameWifi);
                startActivity(intent);
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

    private class NetworkSniffTask extends AsyncTask<Void, Void, ArrayList<ClientScanResult>> {

        private static final String TAG = "nstask";

        private WeakReference<Context> mContextRef;

        public NetworkSniffTask(Context context) {
            mContextRef = new WeakReference<Context>(context);
        }

        @Override
        protected ArrayList<ClientScanResult> doInBackground(Void... voids) {
            Log.d(TAG, "Let's sniff the network");
            try {
                Context context = mContextRef.get();
                arrDevice.clear();
                if (context != null) {
                    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                    WifiManager wm = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    WifiInfo connectionInfo = wm.getConnectionInfo();
                    int ipAddress = connectionInfo.getIpAddress();
                    String ipString = Formatter.formatIpAddress(ipAddress);
                    Log.d(TAG, "activeNetwork: " + String.valueOf(activeNetwork));
                    Log.d(TAG, "ipString: " + String.valueOf(ipString));

                    String prefix = ipString.substring(0, ipString.lastIndexOf(".") + 1);
                    Log.d(TAG, "prefix: " + prefix);

                    for (int i = 0; i < 255; i++) {
                        String testIp = prefix + String.valueOf(i);
                        InetAddress address = InetAddress.getByName(testIp);
                        String hostName = address.getCanonicalHostName();
                        if (!hostName.equals(testIp)){
                            boolean reachable = address.isReachable(1000);
                            if (reachable) {
                                if (testIp.equals(deviceIp))
                                    arrDevice.add(new ClientScanResult(testIp, "My Device", false));
                                else
                                    arrDevice.add(new ClientScanResult(testIp, hostName, false));
                            }
                        }
                    }
                }
            } catch (Throwable t) {
                Log.e(TAG, "Well that's not good.", t);
            }
            return arrDevice;

        }

        @Override
        protected void onPostExecute(ArrayList<ClientScanResult> clientScanResults) {
            super.onPostExecute(clientScanResults);
            String text = arrDevice.size() + " devices are sharing network";
            txtCountDeviceConnected.setText(text);
            PreferenceManager.getInstance(MainActivity.this).setDeviceList(clientScanResults);
        }
    }
}


