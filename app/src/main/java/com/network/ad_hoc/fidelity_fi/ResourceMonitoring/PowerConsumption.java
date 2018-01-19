package com.network.ad_hoc.fidelity_fi.ResourceMonitoring;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.network.ad_hoc.fidelity_fi.R;


public class PowerConsumption extends AppCompatActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_power_consumption);


        Battery_Info_Button_Initiate();
        Button_RAM_Utilization_Initiate();
        Button_CPU_Utilization_Initiate();
        Button_Device_Info_Initiate();

    }

    public  Button Button_CPU_Utilization;
    public  Button Button_RAM_Utilization;
    public Button Button_Battery_Info;
    public Button Button_Device_Info;

    public void Button_Device_Info_Initiate() {
        Button_Device_Info = (Button)findViewById(R.id.Button_Device_Info);
        Button_Device_Info.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Device_Info = new Intent(PowerConsumption.this, com.network.ad_hoc.fidelity_fi.ResourceMonitoring.Device_Info.class);
                startActivity(Device_Info);
            }
        });
    }

    public void Button_CPU_Utilization_Initiate() {
        Button_CPU_Utilization = (Button)findViewById(R.id.Button_CPU_Utilization);
        Button_CPU_Utilization.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent CPU_Info = new Intent(PowerConsumption.this, com.network.ad_hoc.fidelity_fi.ResourceMonitoring.CPU_Info.class);
                startActivity(CPU_Info);
            }
        });
    }

    public void Button_RAM_Utilization_Initiate() {
        Button_RAM_Utilization = (Button)findViewById(R.id.Button_RAM_Utilization);
        Button_RAM_Utilization.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent RAM_Info = new Intent(PowerConsumption.this, com.network.ad_hoc.fidelity_fi.ResourceMonitoring.RAM_Info.class);
                startActivity(RAM_Info);
            }
        });

    }

    public void Battery_Info_Button_Initiate() {
        Button_Battery_Info = (Button)findViewById(R.id.Button_Battery_Info);
        Button_Battery_Info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Battery_Info = new Intent(PowerConsumption.this, com.network.ad_hoc.fidelity_fi.ResourceMonitoring.Battery_Info.class);
                startActivity(Battery_Info);
            }
        });
    }
}

