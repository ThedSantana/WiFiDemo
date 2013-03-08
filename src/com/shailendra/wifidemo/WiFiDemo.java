package com.shailendra.wifidemo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class WiFiDemo extends Activity implements OnClickListener {
    private WifiManager wifi;
    private ListView lv;
    private TextView textStatus;
    private Button buttonScan;
    private List<ScanResult> results;

    private String ITEM_KEY = "key";
    private List<HashMap<String, String>> networkList = new ArrayList<HashMap<String, String>>();
    private SimpleAdapter adapter;

    /* Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wi_fi_demo);

        textStatus = (TextView) findViewById(R.id.textStatus);
        buttonScan = (Button) findViewById(R.id.buttonScan);
        buttonScan.setOnClickListener(this);
        lv = (ListView) findViewById(R.id.list);

        wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        if (wifi.isWifiEnabled() == false) {
            Toast.makeText(getApplicationContext(), "wifi is disabled..making it enabled",
                    Toast.LENGTH_LONG).show();
            wifi.setWifiEnabled(true);
        }
        this.adapter = new SimpleAdapter(WiFiDemo.this, networkList, R.layout.row,
                new String[] { ITEM_KEY }, new int[] { R.id.list_value });
        lv.setAdapter(this.adapter);

        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context c, Intent intent) {
                results = wifi.getScanResults();
                textStatus.setText(results.size() + " networks found");
                for (ScanResult result : results) {
                    HashMap<String, String> items = new HashMap<String, String>();
                    String itenName = "BSSID=" + result.BSSID + ", SSID=" + result.SSID
                            + ", capabilities=" + result.capabilities;
                    items.put(ITEM_KEY, itenName);
                    networkList.add(items);
                    adapter.notifyDataSetChanged();
                }

            }
        }, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
    }

    public void onClick(View view) {
        networkList.clear();
        wifi.startScan();
        textStatus.setText("Scanning....");
    }
}
