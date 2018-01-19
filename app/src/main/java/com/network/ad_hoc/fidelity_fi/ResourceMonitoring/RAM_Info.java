package com.network.ad_hoc.fidelity_fi.ResourceMonitoring;

import android.app.ActivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.network.ad_hoc.fidelity_fi.R;

import java.text.DecimalFormat;


public class RAM_Info extends AppCompatActivity {

    String Display = "";
    DecimalFormat twoDecimalForm = new DecimalFormat("#.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ram__info);

        TextView Total_RAM_Available = (TextView)findViewById(R.id.Total_RAM_Available);
        Total_RAM_Available.setText(getTotalRAM());

        TextView Application_Used_Memory = (TextView)findViewById(R.id.Application_Used_Memory);
        Application_Used_Memory.setText(applicationUsedMemory());

        TextView Application_Free_Memory = (TextView)findViewById(R.id.Application_Free_Memory);
        Application_Free_Memory.setText(availableMemory());
    }

    public String getTotalRAM () {
        ActivityManager activityManager = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        long totalMemory = 0;

        try {
            totalMemory= memoryInfo.totalMem;
        } catch (Exception e) {
        }
        double kB = totalMemory/1000;
        double MB = kB/ (1024.0);
        String output;
        output = String.format("%.2f",MB);
        Display = output + " MB";
        return Display;
    }

    public String applicationUsedMemory() {

        ActivityManager activityManager = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);

        long usedMemory = 0;
        try {
            long freeMemory = memoryInfo.availMem;
            long totalMemory = memoryInfo.totalMem;
            usedMemory = totalMemory-freeMemory;
        } catch (Exception e) {
        }
        double kB = usedMemory/1000.0;
        double MB = kB / (1024.0);
        String output;
        output = String.format("%.2f",MB);
        Display = output + " MB";
        return Display;
    }

    public String availableMemory() {
        ActivityManager activityManager = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        long freeMemory = 0;

        try {
             freeMemory = memoryInfo.availMem;
        } catch (Exception e) {
        }
        double kB = freeMemory/1000;
        double MB = kB/1024;
        String output;
        output = String.format("%.2f",MB);
        Display = output + " MB";
        return Display;
    }
}
