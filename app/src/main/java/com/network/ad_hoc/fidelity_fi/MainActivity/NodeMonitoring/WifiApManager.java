package com.network.ad_hoc.fidelity_fi.MainActivity.NodeMonitoring;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;


public class WifiApManager {
    private final WifiManager WifiManager;
    private Context context;

    public void getClientList(boolean onlyReachables, FinishScanListener finishListener) {
        getClientList(onlyReachables, 300, finishListener );
    }
    public WifiApManager(Context context) {
        this.context = context;
        WifiManager = (WifiManager) this.context.getSystemService(Context.WIFI_SERVICE);
    }

    public void getClientList(final boolean onlyReachables, final int reachableTimeout, final FinishScanListener finishListener) {


        Runnable runnable = new Runnable() {
            public void run() {

                BufferedReader bufferedReader = null;
                final ArrayList<ClientScanResult> clientScanResult = new ArrayList<>();

                try {
                    bufferedReader = new BufferedReader(new FileReader("/proc/net/arp"));
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        String[] split_point = line.split(" + ");
                        if ((split_point != null) && (split_point.length >= 4)) {
                            String MAC_Address = split_point[3];
                            if (MAC_Address.matches("..:..:..:..:..:..")) {
                                boolean isReachable = InetAddress.getByName(split_point[0]).isReachable(reachableTimeout);
                                if (!onlyReachables || isReachable) {
                                    clientScanResult.add(new ClientScanResult(split_point[0], split_point[3], split_point[5], isReachable));
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    Log.e(this.getClass().toString(), e.toString());
                } finally {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        Log.e(this.getClass().toString(), e.getMessage());
                    }
                }

                // Get a handler that can be used to post to the main thread
                Handler mainHandler = new Handler(context.getMainLooper());
                Runnable myRunnable = new Runnable() {
                    @Override
                    public void run() {
                        finishListener.onFinishScan(clientScanResult);
                    }
                };
                mainHandler.post(myRunnable);
            }
        };

        Thread mythread = new Thread(runnable);
        mythread.start();
    }
}
