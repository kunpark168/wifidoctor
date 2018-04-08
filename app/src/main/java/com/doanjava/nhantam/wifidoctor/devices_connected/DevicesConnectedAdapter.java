package com.doanjava.nhantam.wifidoctor.devices_connected;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.doanjava.nhantam.wifidoctor.R;
import com.doanjava.nhantam.wifidoctor.network.ClientScanResult;

import java.sql.ClientInfoStatus;
import java.util.ArrayList;

/**
 * Created by Anh on 09/04/2018.
 */

public class DevicesConnectedAdapter extends RecyclerView.Adapter<DevicesConnectedViewHolder> {

    private Context mContext;
    private ArrayList<ClientScanResult> arrDevice;

    public DevicesConnectedAdapter(Context mContext, ArrayList<ClientScanResult> arrDevice) {
        this.mContext = mContext;
        this.arrDevice = arrDevice;
    }

    @Override
    public DevicesConnectedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.item_client, parent, false);
        return new DevicesConnectedViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(DevicesConnectedViewHolder holder, int position) {
        ClientScanResult device = arrDevice.get(position);
        holder.txtDeviceName.setText(device.getNameDevice());
        holder.txtDeviceIp.setText(device.getIpAddr());
    }

    @Override
    public int getItemCount() {
        return arrDevice.size();
    }
}
