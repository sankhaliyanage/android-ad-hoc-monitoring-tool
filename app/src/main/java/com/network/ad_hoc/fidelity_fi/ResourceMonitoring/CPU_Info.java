package com.network.ad_hoc.fidelity_fi.ResourceMonitoring;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.network.ad_hoc.fidelity_fi.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class CPU_Info extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cpu__info);

        TextView CPU_Info_View = (TextView)findViewById(R.id.CPU_Info_View);
        CPU_Info_View.setText(getCPU_Info());
    }

    private String getCPU_Info() {
        StringBuffer stringBuffer = new StringBuffer();
        if (new File("/proc/cpuinfo").exists()) {
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("/proc/cpuinfo")));
                String ReadLine;
                while ((ReadLine = bufferedReader.readLine()) != null) {
                    stringBuffer.append(ReadLine + "\n");
                }
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
            }
        }
        return stringBuffer.toString();
    }
}
