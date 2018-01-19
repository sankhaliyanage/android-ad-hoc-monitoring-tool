package com.network.ad_hoc.fidelity_fi.MainActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.TrafficStats;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.network.ad_hoc.fidelity_fi.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    private Switch HotSpotOn_OffSwitch;
    private Switch Auto_Pilot_OnOff_Switch;
    private TextView NetworkStatusDisplaytextView;

    private long startRXBytes;
    private long startTXBytes;
    private long startTXPackets;
    private long startRXPackets;

    private long startWirelessRXBytes;
    private long startWirelessTXBytes;
    private long startWirelessRXPackets;
    private long startWirelessTXPackets;
    private long startMobileRXPackets;
    private long startMobileTXPackets;
    private long startMobileRXBytes;
    private long startMobileTXBytes;

    private long startTime, endTime, timeDifference;
    String Display = "";

    public String Source = "";
    public  String Status = "";
    public int Temperature = 0;
    public int Battery_level = 0;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(runnable_total_bytes);
                        runOnUiThread(runnable_total_packets);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        IntentFilter filter = new IntentFilter("android.net.wifi.WIFI_AP_STATE_CHANGED");
        registerReceiver(Receiver, filter);


        registerReceiver(Battery_Info_Status, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));


        //create the toolbar at the application startup
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //create the switch button for ad-hoc mode on/off
        HotSpotOn_OffSwitch = (Switch) findViewById(R.id.HotSpotOn_OffSwitch);
        Auto_Pilot_OnOff_Switch = (Switch) findViewById(R.id.Auto_Pilot_OnOff_Switch);
        NetworkStatusDisplaytextView = (TextView) findViewById(R.id.NetworkStatusDisplaytextView);

        //turning the switch ON
        HotSpotOn_OffSwitch.setChecked(false);
        Auto_Pilot_OnOff_Switch.setChecked(false);
        //attach a listener to check for state changes
        HotSpotOn_OffSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    createDialogPopUp_AdHocNetworkImplementationPrompt();

                } else {
                    createDialogPopUp_AdHocNetworkSwitchOffPropmpt();
                }
            }
        });
        //check the NetworkStatusDisplaytextView button

        Auto_Pilot_OnOff_Switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    AutoPilotMode();
                }
            }

        });


        TextView Upload_Rate_Display_Main_textView = (TextView) findViewById(R.id.Upload_Rate_Display_Main_textView);
        Upload_Rate_Display_Main_textView.setText(UploadDownloadRates_Main());

        TextView Download_Rate_Display_Main_textView = (TextView) findViewById(R.id.Download_Rate_Display_Main_textView);
        Download_Rate_Display_Main_textView.setText(UploadDownloadRates_Main());

       // TextView Operating_Mode_TextView = (TextView)findViewById(R.id.Operating_Mode_TextView);
      //  Operating_Mode_TextView.setText(AutoPilotMode());


        TextView Throughput_Total_Upload = (TextView) findViewById(R.id.Throughput_Total_Upload);
        if (Throughput_Total_Upload != null) {
            Throughput_Total_Upload.setText(totalUploadDownload());
        }

        TextView Throughput_Total_Download = (TextView) findViewById(R.id.Throughput_Total_Download);
        if (Throughput_Total_Download != null) {
            Throughput_Total_Download.setText(totalUploadDownload());
        }

        TextView Wireless_Upload_Packets = (TextView) findViewById(R.id.Wireless_Upload_Packets);
        if (Wireless_Upload_Packets != null) {
            Wireless_Upload_Packets.setText(UploadDownloadPackets());
        }

        TextView Wireless_Download_Packets = (TextView) findViewById(R.id.Wireless_Download_Packets);
        if (Wireless_Download_Packets != null) {
            Wireless_Download_Packets.setText(UploadDownloadPackets());
        }
    }



    /*
    public static int AP_STATE_DISABLING = 10;
    public static int AP_STATE_DISABLED = 11;
    public static int AP_STATE_ENABLING = 12;
    public static int AP_STATE_ENABLED = 13;
    public static int AP_STATE_FAILED = 14;
    */

    private final BroadcastReceiver Receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("android.net.wifi.WIFI_AP_STATE_CHANGED".equals(action)) {
                int state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
                if (WifiManager.WIFI_STATE_ENABLED == (state % 10)) {
                    if (state % 10 == 0) {
                        NetworkStatusDisplaytextView.setText("Disabling");
                    } else if (state % 10 == 1) {
                        NetworkStatusDisplaytextView.setText("Disabled");
                    } else if (state % 10 == 2) {
                        NetworkStatusDisplaytextView.setText("Enabling");
                    } else if (state % 10 == 3) {
                        NetworkStatusDisplaytextView.setText("Enabled");
                        Toast.makeText(getApplicationContext(), "HotSpot Started", Toast.LENGTH_LONG).show();
                    } else if (state % 10 == 4) {
                        NetworkStatusDisplaytextView.setText("Failed");
                    }
                } else {
                    NetworkStatusDisplaytextView.setText("Inactive");
                }
            }
        }
    };


    public Button Button_SignalStrength;

    public void SignalStrengthButtonInitiate() {
        Button_SignalStrength = (Button) findViewById(R.id.Button_SignalStrength);
        Button_SignalStrength.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent SignalStrength = new Intent(MainActivity.this, com.network.ad_hoc.fidelity_fi.MainActivity.SignalStrength.class);
                startActivity(SignalStrength);
            }
        });
    }

    public Button Button_NodeMonitoring;

    public void NodeMonitoringButtonInitiate() {
        Button_NodeMonitoring = (Button) findViewById(R.id.Button_NodeMonitoring);
        Button_NodeMonitoring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent NodeMonitoring = new Intent(MainActivity.this, com.network.ad_hoc.fidelity_fi.MainActivity.NodeMonitoring.NodeMonitoring.class);
                startActivity(NodeMonitoring);
            }
        });
    }

    public Button Button_ProtocolStatistics;


    public Button Button_PowerConsumption;

    public void PowerConsumptionButtonInitiate() {
        Button_PowerConsumption = (Button) findViewById(R.id.Button_PowerConsumption);
        Button_PowerConsumption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                Intent PowerConsumption = new Intent(MainActivity.this, com.network.ad_hoc.fidelity_fi.ResourceMonitoring.PowerConsumption.class);
                startActivity(PowerConsumption);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu_toolbar_widgets, menu);
        SignalStrengthButtonInitiate();
        NodeMonitoringButtonInitiate();
        PowerConsumptionButtonInitiate();

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int resource_ID = item.getItemId();
        if (resource_ID == R.id.action_settings) {
            Toast.makeText(getApplicationContext(), "You selected Settings option", Toast.LENGTH_LONG).show();
            startActivityForResult(new Intent(Settings.ACTION_WIRELESS_SETTINGS), 0);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "You selected Exit option", Toast.LENGTH_LONG).show();
        createDialogPopUp_ExitPrompt();
    }

    private void createDialogPopUp_ExitPrompt() {
        AlertDialog.Builder YesNoPrompt = new AlertDialog.Builder(this);
        YesNoPrompt.setMessage("Are you sure you want to exit?");
        YesNoPrompt.setCancelable(false);

        YesNoPrompt.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.super.onBackPressed();
            }
        });
        YesNoPrompt.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        YesNoPrompt.create().show();
    }

    private void createDialogPopUp_AdHocNetworkImplementationPrompt() {
        AlertDialog.Builder AdHocNetworkImplement = new AlertDialog.Builder(this);
        AdHocNetworkImplement.setMessage("Implementing HotSpot");
        AdHocNetworkImplement.setCancelable(false);
        AdHocNetworkImplement.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivityForResult(new Intent(Settings.ACTION_WIRELESS_SETTINGS), 0);

            }
        });
        AdHocNetworkImplement.create().show();
    }

    private void createDialogPopUp_AdHocNetworkSwitchOffPropmpt() {
        AlertDialog.Builder AdHocNetworkTurnOff = new AlertDialog.Builder(this);
        AdHocNetworkTurnOff.setMessage("Turning off the HotSpot");
        AdHocNetworkTurnOff.setCancelable(false);
        AdHocNetworkTurnOff.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivityForResult(new Intent(Settings.ACTION_WIRELESS_SETTINGS), 0);
            }
        });
        AdHocNetworkTurnOff.create().show();
    }

    public BroadcastReceiver Battery_Info_Status = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            TextView Battery_Status_Display_MaintextView = (TextView) findViewById(R.id.Battery_Status_Display_MaintextView);
            TextView Battery_Status_Source_textView = (TextView) findViewById(R.id.Battery_Status_Source_textView);
            TextView Battery_Temperature_Display_Main_textView = (TextView) findViewById(R.id.Battery_Temperature_Display_Main_textView);
            TextView Battery_Capacity_Display_Main_textView = (TextView) findViewById(R.id.Battery_Capacity_Display_Main_textView);
            if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) ;
            {
                int status = intent.getIntExtra("status", BatteryManager.BATTERY_STATUS_UNKNOWN);
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
                        Status = "";
                }
                Battery_Status_Display_MaintextView.setText(Status);

                int source = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
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
                Battery_Status_Source_textView.setText(Source);

                Temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0);
                Battery_Temperature_Display_Main_textView.setText(String.valueOf(intent.getIntExtra("Temperature", Temperature / 10) + " C"));

                Battery_Capacity_Display_Main_textView.setText(String.valueOf(intent.getIntExtra("voltage", 0) + "mV"));

                Battery_level = intent.getIntExtra("level", 0);
            }

        }
    };
    //throughput methods
    public String totalUploadDownload() {
        startRXBytes = TrafficStats.getTotalRxBytes();
        startMobileRXBytes = TrafficStats.getMobileRxBytes();
        startWirelessRXBytes = startRXBytes - startMobileRXBytes;

        startTXBytes = TrafficStats.getTotalTxBytes();
        startMobileTXBytes = TrafficStats.getMobileTxBytes();
        startWirelessTXBytes = startTXBytes - startMobileTXBytes;

        if (startRXBytes == TrafficStats.UNSUPPORTED || startTXBytes == TrafficStats.UNSUPPORTED) {
            Display = "SDK Unsupported";
        } else {
            handler.post(runnable_total_bytes);
        }
        return Display;
    }

    public Runnable runnable_total_bytes = new Runnable() {
        @Override
        public void run() {
            TextView Throughput_Total_Upload = (TextView) findViewById(R.id.Throughput_Total_Upload);
            long EndTXBytes = TrafficStats.getTotalTxBytes();
            long EndMobileTXBytes = TrafficStats.getMobileTxBytes();
            long EndWirelessTXBytes = EndTXBytes - EndMobileTXBytes;
            long EndTotalWirelessTXBytes = EndWirelessTXBytes - startWirelessTXBytes;
            double totalSentKBytes = (double) EndTotalWirelessTXBytes / 1000;

            if (totalSentKBytes < 1024) {
                String output = String.format("%.2f", totalSentKBytes);
                if (Throughput_Total_Upload != null) {
                    Throughput_Total_Upload.setText(output + " kB");
                }
            } else if (totalSentKBytes >= 1024 && totalSentKBytes < (1024 * 1024)) {
                double totalSentMB = totalSentKBytes / 1024;
                String output = String.format("%.2f", totalSentMB);
                if (Throughput_Total_Upload != null) {
                    Throughput_Total_Upload.setText(output + " MB");
                }
            } else if (totalSentKBytes >= 1048576) {
                double totalSentGB = totalSentKBytes / 1048576;
                String output = String.format("%.2f", totalSentGB);
                if (Throughput_Total_Upload != null) {
                    Throughput_Total_Upload.setText(output + " GB");
                }
            }


            TextView Throughput_Total_Download = (TextView) findViewById(R.id.Throughput_Total_Download);
            long EndRXBytes = TrafficStats.getTotalRxBytes();
            long EndMobileRXBytes = TrafficStats.getMobileRxBytes();
            long EndWirelessRXBytes = EndRXBytes - EndMobileRXBytes;
            long EndTotalWirelessRXBytes = EndWirelessRXBytes - startWirelessRXBytes;
            double totalReceivedKBytes = (double) EndTotalWirelessRXBytes / 1000;
            if (totalReceivedKBytes < 1024) {
                String output = String.format("%.2f", totalReceivedKBytes);
                if (Throughput_Total_Download != null) {
                    Throughput_Total_Download.setText(output + " kB");
                }
            } else if (totalReceivedKBytes >= 1024 && totalReceivedKBytes < (1024 * 1024)) {
                double totalReceivedMB = totalReceivedKBytes / 1024;
                String output = String.format("%.2f", totalReceivedMB);
                if (Throughput_Total_Download != null) {
                    Throughput_Total_Download.setText(output + " MB");
                }
            } else if (totalReceivedKBytes >= (1024 * 1024)) {
                double totalReceivedGB = (totalReceivedKBytes) / 1048576;
                String output = String.format("%.2f", totalReceivedGB);
                if (Throughput_Total_Download != null) {
                    Throughput_Total_Download.setText(output + " GB");
                }
            }
            handler.post(runnable_total_bytes);
        }
    };

    public String UploadDownloadRates_Main() {
        startTime = System.currentTimeMillis();
        startRXBytes = TrafficStats.getTotalRxBytes();
        startMobileRXBytes = TrafficStats.getMobileRxBytes();
        startWirelessRXBytes = startRXBytes - startMobileRXBytes;

        startTXBytes = TrafficStats.getTotalTxBytes();
        startMobileTXBytes = TrafficStats.getMobileTxBytes();
        startWirelessTXBytes = startTXBytes - startMobileTXBytes;

        if (startRXBytes == TrafficStats.UNSUPPORTED || startTXBytes == TrafficStats.UNSUPPORTED) {
            Display = "SDK Unsupported";
        } else {
            handler.post(runnable_wireless_Speed);
        }
        return Display;
    }


    Runnable runnable_wireless_Speed = new Runnable() {
        @Override
        public void run() {

            TextView Upload_Rate_Display_Main_textView = (TextView) findViewById(R.id.Upload_Rate_Display_Main_textView);
            long EndTXBytes = TrafficStats.getTotalTxBytes();
            long EndMobileTXBytes = TrafficStats.getMobileTxBytes();
            long EndWirelessTXBytes = EndTXBytes - EndMobileTXBytes;
            //the code below added the infinity loop; mention it in documentation
            double EndTotalWirelessTXBytes = EndWirelessTXBytes - startWirelessTXBytes;
            endTime = System.currentTimeMillis();
            timeDifference = (endTime - startTime) / 1000;
            double SentKBytes = ((EndTotalWirelessTXBytes / 1024) / (timeDifference));
            if (SentKBytes < 1024) {
                String output = String.format("%.2f", SentKBytes);
                Upload_Rate_Display_Main_textView.setText(output + " kB/s");
            } else if (SentKBytes >= 1024) {
                double SentMBytes = SentKBytes / 1024;
                String output = String.format("%.2f", SentMBytes);
                Upload_Rate_Display_Main_textView.setText(output + " MB/s");
            }

            TextView Download_Rate_Display_Main_textView = (TextView) findViewById(R.id.Download_Rate_Display_Main_textView);
            long EndRXBytes = TrafficStats.getTotalRxBytes();
            long EndMobileRXBytes = TrafficStats.getMobileRxBytes();
            long EndWirelessRXBytes = EndRXBytes - EndMobileRXBytes;
            double EndTotalWirelessRXBytes = EndWirelessRXBytes - startWirelessRXBytes;
            endTime = System.currentTimeMillis();
            timeDifference = (endTime - startTime) / 1000;
            double ReceivedKBytes = ((EndTotalWirelessRXBytes / 1024) / timeDifference);
            if (ReceivedKBytes < 1024) {
                String output = String.format("%.2f", ReceivedKBytes);
                Download_Rate_Display_Main_textView.setText(output + " kB/s");
            } else if (ReceivedKBytes >= 1024) {
                double ReceivedMBytes = ReceivedKBytes / 1024;
                String output = String.format("%.2f", ReceivedMBytes);
                Download_Rate_Display_Main_textView.setText(output + " MB/s");
            }
            handler.post(runnable_wireless_Speed);
        }
    };


    public String UploadDownloadPackets() {

        startRXPackets = TrafficStats.getTotalRxPackets();
        startMobileRXPackets = TrafficStats.getMobileRxPackets();
        startWirelessRXPackets = startRXPackets - startMobileRXPackets;

        startTXPackets = TrafficStats.getTotalTxPackets();
        startMobileTXPackets = TrafficStats.getMobileTxPackets();
        startWirelessTXPackets = startTXPackets - startMobileTXPackets;

        if (startTXPackets == TrafficStats.UNSUPPORTED || startRXPackets == TrafficStats.UNSUPPORTED) {
            Display = "SDK Unsupported";
        } else {
            handler.post(runnable_total_packets);
        }
        return Display;
    }

    private Runnable runnable_total_packets = new Runnable() {
        @Override
        public void run() {
            TextView Wireless_Upload_Packets = (TextView) findViewById(R.id.Wireless_Upload_Packets);

            long EndTXPackets = TrafficStats.getTotalTxPackets();
            long EndMobileTXPackets = TrafficStats.getMobileTxPackets();
            long EndWirelessTXPackets = EndTXPackets - EndMobileTXPackets;

            long EndTotalTXPackets = EndWirelessTXPackets - startWirelessTXPackets;
            double sentTXPackets = (double) EndTotalTXPackets;
            if (Wireless_Upload_Packets != null) {
                Wireless_Upload_Packets.setText(Double.toString(sentTXPackets));
            }

            TextView Wireless_Download_Packets = (TextView) findViewById(R.id.Wireless_Download_Packets);

            long EndRXPackets = TrafficStats.getTotalRxPackets();
            long EndMobileRXPackets = TrafficStats.getMobileRxPackets();
            long EndWirelessRXPackets = EndRXPackets - EndMobileRXPackets;

            long EndTotalRXPackets = EndWirelessRXPackets - startWirelessRXPackets;
            double sentRXPackets = (double) EndTotalRXPackets;
            if (Wireless_Download_Packets != null) {
                Wireless_Download_Packets.setText(Double.toString(sentRXPackets));
            }
            handler.post(runnable_total_packets);
        }
    };

    public void SetMobileDataOn() {
        Auto_Pilot_OnOff_Switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(MainActivity.this,"Switching on mobile data",Toast.LENGTH_SHORT).show();

                (new Thread(new Runnable() {
                    @Override
                    public void run() {

                        setMobileDataEnabled(MainActivity.this,true);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "Mobile data switched on", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })).start();
            }
        });
    }

    public void SetMobileDataOff() {
        Auto_Pilot_OnOff_Switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,"Switching off mobile data",Toast.LENGTH_SHORT).show();

                (new Thread(new Runnable() {
                    @Override
                    public void run() {

                        setMobileDataEnabled(MainActivity.this,false);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "Mobile data turned off", Toast.LENGTH_SHORT).show();
                            }
                        });


                    }
                })).start();
            }
        });
    }



    public String AutoPilotMode() {
        TextView Operating_Mode = (TextView) findViewById(R.id.Operating_Mode);
        TextView Device_Activity_Display_MaintextView = (TextView) findViewById(R.id.Device_Activity_Display_MaintextView);
        if (isRooted() == true) {
            if ((Temperature >= 45) && (Source == "AC") && (Status == "Charging")) {
                SetMobileDataOff();
                Operating_Mode.setText("Suspended");
                Device_Activity_Display_MaintextView.setText("High");
                Toast.makeText(getApplicationContext(), "Enable Power Saving mode", Toast.LENGTH_LONG).show();
            } else if ((Temperature < 45) && (Temperature >= 40) && (Source == "USB") && (Status == "Charging")) {
                SetMobileDataOff();
                Operating_Mode.setText("Suspended");
                Toast.makeText(getApplicationContext(), "Enable Power Saving mode", Toast.LENGTH_LONG).show();
                Device_Activity_Display_MaintextView.setText("High");
            } else if ((Temperature > 40) && (Temperature >= 35) && (Battery_level < 20)) {
                SetMobileDataOff();
                Operating_Mode.setText("Suspended");
                Toast.makeText(getApplicationContext(), "Enable Power Saving mode", Toast.LENGTH_LONG).show();
                Device_Activity_Display_MaintextView.setText("Inactive");
            } else {
                SetMobileDataOn();
                Operating_Mode.setText("Active");
                Device_Activity_Display_MaintextView.setText("Low");
                Toast.makeText(getApplicationContext(), "Disable Power Saving mode", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Your device is not Rooted", Toast.LENGTH_LONG).show();
            Operating_Mode.setText("Unsupported");
            if ((Temperature >=45) && (Source =="AC") && (Status == "Charging")){
                Device_Activity_Display_MaintextView.setText("High");
                Toast.makeText(getApplicationContext(), "High temperature. Please turn on the power saving mode", Toast.LENGTH_LONG).show();
            }
            else if ((Temperature < 45) && (Temperature >= 40) && (Source == "USB") && (Status == "Charging")){
                Device_Activity_Display_MaintextView.setText("High");
                Toast.makeText(getApplicationContext(), "High temperature. Please turn on the power saving mode", Toast.LENGTH_LONG).show();
            }
            else if ((Temperature > 40) && (Temperature >= 35) && (Battery_level < 20)){
                Device_Activity_Display_MaintextView.setText("Inactive");
                Toast.makeText(getApplicationContext(), "Battery low. Turn off the hotspot", Toast.LENGTH_LONG).show ();
            }
            else {
                Device_Activity_Display_MaintextView.setText("Low");
            }
        }
        return "";
    }

        private void setMobileDataEnabled(Context context, boolean enabled) {
        try {
            final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            final Class connectivityManagerClass = Class.forName(connectivityManager.getClass().getName());
            final Field ConnectivityManagerField = connectivityManagerClass.getDeclaredField("Service");
            ConnectivityManagerField.setAccessible(true);
            final Object ConnectivityManager = ConnectivityManagerField.get(connectivityManager);
            final Class ConnectivityManagerClass = Class.forName(ConnectivityManager.getClass().getName());
            final Method setMobileDataEnabledMethod = ConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
            setMobileDataEnabledMethod.setAccessible(true);
            setMobileDataEnabledMethod.invoke(ConnectivityManager, enabled);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static boolean isRooted() {
        return checkRootMethod_buildTags() || checkRootMethod_SU_Path() || checkRootMethod_SU_xbin();
    }

    private static boolean checkRootMethod_buildTags() {
        String buildTags = android.os.Build.TAGS;
        return buildTags != null && buildTags.contains("test-keys");
    }

    private static boolean checkRootMethod_SU_Path() {
        String[] pathChecker = { "/system/app/Superuser.apk",
                "/sbin/su",
                "/system/bin/su",
                "/system/xbin/su",
                "/data/local/xbin/su",
                "/data/local/bin/su",
                "/system/sd/xbin/su",
                "/system/bin/failsafe/su",
                "/data/local/su" };
        for (String pathExisting : pathChecker) {
            if (new File(pathExisting).exists()) return true;
        }
        return false;
    }

    private static boolean checkRootMethod_SU_xbin() {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(new String[] { "/system/xbin/which", "su" });
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            if (bufferedReader.readLine() != null) return true;
            return false;
        } catch (Throwable throwable) {
            return false;
        } finally {
            if (process != null) process.destroy();
        }
    }
}








