package com.network.ad_hoc.fidelity_fi.MainActivity.NodeMonitoring;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.network.ad_hoc.fidelity_fi.R;

import java.util.ArrayList;


public class NodeMonitoring extends AppCompatActivity {

    WifiApManager wifiApManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_node_monitoring);


        wifiApManager = new WifiApManager(this);



        display_active_clients();

    }

    private void display_active_clients() {
        wifiApManager.getClientList(false, new FinishScanListener() {

            @Override
            public void onFinishScan(final ArrayList<ClientScanResult> clients) {
                TextView Node_Monitoring_TextView = (TextView) findViewById(R.id.Node_Monitoring_TextView);
                Node_Monitoring_TextView.append("Connected clients: \n");
                for (ClientScanResult clientScanResult : clients) {
                    Node_Monitoring_TextView.append("------------------------------------\n");
                    Node_Monitoring_TextView.append("IP address : " + clientScanResult.getIP_Address() + "\n");
                    Node_Monitoring_TextView.append("Hardware type : " + clientScanResult.getDevice() + "\n");
                    Node_Monitoring_TextView.append("MAC Address : " + clientScanResult.getHardwareAddress() + "\n");
                    Node_Monitoring_TextView.append("Reachability Status : " + clientScanResult.isReachable() + "\n");
                    }
            }
        });
    }
}



