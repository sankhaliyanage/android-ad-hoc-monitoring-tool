package com.network.ad_hoc.fidelity_fi.ResourceMonitoring;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.network.ad_hoc.fidelity_fi.R;

public class Device_Info extends AppCompatActivity {
    String Display = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device__info);

        TextView Device_info_View = (TextView)findViewById(R.id.Device_info_View);
        Device_info_View.setText(getDeviceInfo());
    }

    private String getDeviceInfo() {
        try {
            Display += "\n OS Version:"+ System.getProperty("os.version") +"\n";
            Display += "\n OS API Level:"+ android.os.Build.VERSION.SDK_INT +"\n";
            Display += "\n Device:"+ android.os.Build.DEVICE +"\n";
            Display += "\n Model:" + android.os.Build.MODEL +"\n";
            Display += "\n Product:"+ android.os.Build.PRODUCT +"\n";
            Display += "\n Release:"+ android.os.Build.VERSION.RELEASE +"\n";
            Display += "\n Brand:"+ android.os.Build.BRAND +"\n";
            Display += "\n Build ID:"+ android.os.Build.ID +"\n";
            Display += "\n Manufacturer:"+ android.os.Build.MANUFACTURER +"\n";
            Display += "\n Serial:"+ android.os.Build.SERIAL +"\n";
        } catch (Exception e) {
         Display = "Error getting device information";
        }
return Display;
    }
}
