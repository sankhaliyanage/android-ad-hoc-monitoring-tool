package com.network.ad_hoc.fidelity_fi.ResourceMonitoring;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.network.ad_hoc.fidelity_fi.R;

public class Battery_Info extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battery__info);

        registerReceiver(BatteryInfo, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    private BroadcastReceiver BatteryInfo = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // beginning of remaining capacity calculation
            int level = intent.getIntExtra("level", 0);
            ProgressBar progressBar_Remaining_Capacity = (ProgressBar) findViewById(R.id.progressBar_Remaining_Capacity);
            progressBar_Remaining_Capacity.setProgress(level);
            TextView Battery_Percentage_textView = (TextView) findViewById(R.id.Battery_Percentage_textView);
            Battery_Percentage_textView.setText(Integer.toString(level) + "%");
            // end of remaining capacity calculation

            // start of remaining voltage calculation
            TextView Voltage_Display = (TextView) findViewById(R.id.Voltage_Display);
            if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {
                Voltage_Display.setText(String.valueOf(intent.getIntExtra("voltage", 0) + "mV"));
            }
            // end of remaining voltage calculation

            // start of power source calculation
            TextView Plugged_in_Source = (TextView) findViewById(R.id.Plugged_in_Source);
            if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {
                int source = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
                String Source;
                switch (source) {
                    case BatteryManager.BATTERY_PLUGGED_USB:
                        Source = "USB";
                        break;
                    case BatteryManager.BATTERY_PLUGGED_AC:
                        Source = "AC";
                        break;
                    case BatteryManager.BATTERY_PLUGGED_WIRELESS:
                        Source = "Wireless";
                        break;
                    default:
                        Source = "Unknown";
                }
                Plugged_in_Source.setText(Source);
            }
            // end of power source calculation

            //start of battery status calculation
            TextView Battery_Charging_Discharging_Status_Indicator = (TextView) findViewById(R.id.Battery_Charging_Discharging_Status_Indicator);
            if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) ;
            {
                int status = intent.getIntExtra("status", BatteryManager.BATTERY_STATUS_UNKNOWN);
                String Status;
                switch (status) {
                    case BatteryManager.BATTERY_STATUS_CHARGING:
                        Status = "Charging";
                        break;
                    case BatteryManager.BATTERY_STATUS_DISCHARGING:
                        Status = "Discharging";
                        break;
                    case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                        Status = "Not charging";
                        break;
                    case BatteryManager.BATTERY_STATUS_FULL:
                        Status = "Full";
                        break;
                    default:
                        Status = "Unknown";
                }
                Battery_Charging_Discharging_Status_Indicator.setText(Status);
            }
            //end of battery status calculation

            //start of battery temperature calculation
            TextView Operating_Temperature = (TextView) findViewById(R.id.Operating_Temperature);
            if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) ;
            {
                int Temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0);
                Operating_Temperature.setText(String.valueOf(intent.getIntExtra("Temperature", Temperature / 10) + "C"));
            }
            //end of battery temperature calculation

            //start of battery health calculation
            TextView Battery_Health = (TextView) findViewById(R.id.Battery_Health);
            if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) ;
            {
                int health = intent.getIntExtra("health", BatteryManager.BATTERY_HEALTH_UNKNOWN);
                String Health;
                if (health == BatteryManager.BATTERY_HEALTH_GOOD) {
                    Health = "Good";
                } else if (health == BatteryManager.BATTERY_HEALTH_DEAD) {
                    Health = "Dead";
                } else if (health == BatteryManager.BATTERY_HEALTH_COLD) {
                    Health = "Cold";
                } else if (health == BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE) {
                    Health = "Over Voltage";
                } else if (health == BatteryManager.BATTERY_HEALTH_OVERHEAT) {
                    Health = "Over Heat";
                } else {
                    Health = "Unknown";
                }
                Battery_Health.setText(Health);
            }
            //end of battery health calculation

            //start of battery technology calculation
            TextView Battery_Technology = (TextView) findViewById(R.id.Battery_Technology);
            if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) ;
            {
                String technology = intent.getExtras().getString(BatteryManager.EXTRA_TECHNOLOGY);
                Battery_Technology.setText(technology);
            }
            //end of battery technology calculation

            //start of power saving mode checker
            PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            TextView Power_Saving_Mode_TextView = (TextView)findViewById(R.id.Power_Saving_Mode_TextView);
            if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
                if (powerManager.isPowerSaveMode() == true) {
                    Power_Saving_Mode_TextView.setText("Enabled");
                } else {
                    Power_Saving_Mode_TextView.setText("Disabled");
                }
            }

            else {
                Power_Saving_Mode_TextView.setText("SDK Unsupported");
            }
            //end of power saving mode checker
        }
    };
}
