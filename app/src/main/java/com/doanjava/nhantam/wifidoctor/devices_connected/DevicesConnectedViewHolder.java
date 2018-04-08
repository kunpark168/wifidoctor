package com.doanjava.nhantam.wifidoctor.devices_connected;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.doanjava.nhantam.wifidoctor.R;

/**
 * Created by Anh on 09/04/2018.
 */

public class DevicesConnectedViewHolder extends RecyclerView.ViewHolder {

    public TextView txtDeviceName, txtDeviceIp;
    public ImageView imgType;
    public DevicesConnectedViewHolder(View itemView) {
        super(itemView);
        txtDeviceIp = itemView.findViewById(R.id.txtDeviceIp);
        txtDeviceName = itemView.findViewById(R.id.txtDeviceName);
        imgType = itemView.findViewById(R.id.imgTypeDevice);
    }
}
