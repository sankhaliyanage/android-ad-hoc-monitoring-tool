package com.network.ad_hoc.fidelity_fi.MainActivity.NodeMonitoring;


public class ClientScanResult {
    private String IPAddress;
    private String HardwareAddress;
    private String Device;
    private boolean isReachable;

    public ClientScanResult(String IP_Address, String HW_Address, String device, boolean isReachable) {
        super();
        this.IPAddress = IP_Address;
        this.HardwareAddress = HW_Address;
        this.Device = device;
        this.isReachable = isReachable;
    }

    public String getIP_Address() {
        return IPAddress;
    }

    public String getHardwareAddress() {

        return HardwareAddress;
    }

    public String getDevice() {

        return Device;
    }

    public boolean isReachable()
    {
        return isReachable;
    }


}
