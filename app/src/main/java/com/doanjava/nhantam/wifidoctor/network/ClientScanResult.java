/*
 * Copyright 2013 WhiteByte (Nick Russler, Ahmet Yueksektepe).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.doanjava.nhantam.wifidoctor.network;

public class ClientScanResult {
    private String IpAddr;
    private String nameDevice;
    private boolean isUnknowDevice;

    public ClientScanResult(String ipAddr, String nameDevice, boolean isUnknowDevice) {
        this.IpAddr = ipAddr;
        this.nameDevice = nameDevice;
        this.isUnknowDevice = isUnknowDevice;
    }

    public String getIpAddr() {
        return IpAddr;
    }

    public void setIpAddr(String ipAddr) {
        IpAddr = ipAddr;
    }

    public String getNameDevice() {
        return nameDevice;
    }

    public void setNameDevice(String nameDevice) {
        this.nameDevice = nameDevice;
    }

    public void setUnknowDevice(boolean unknowDevice) {
        isUnknowDevice = unknowDevice;
    }
}
