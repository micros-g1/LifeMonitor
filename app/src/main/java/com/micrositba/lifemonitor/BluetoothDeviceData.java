package com.micrositba.lifemonitor;

class BluetoothDeviceData {
    private String deviceName;
    private String deviceAddress;

    BluetoothDeviceData(String deviceName, String deviceAddress){
        this.deviceName = deviceName;
        this.deviceAddress = deviceAddress;
    }

    String getName(){
        return deviceName;
    }

    String getAddress(){
        return deviceAddress;
    }
}
