package com.matwoosh.heboy;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;
import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ScanningActivity extends BaseActivity {

    private static final String TAG = ScanningActivity.class.getSimpleName();

    public static final String EXTRAS_TARGET_ACTIVITY = "extrasTargetActivity";
    public static final String EXTRAS_BEACON = "extrasBeacon";

    private static final int REQUEST_ENABLE_BT = 1234;
    private static final Region ALL_ESTIMOTE_BEACONS_REGION = new Region("rid", null, null, null);

    private MyBeacons myBeacons = new MyBeacons();
    private BeaconManager beaconManager;
    private BeaconListAdapter adapter;
    private int beaconFound;

    public enum beaconColors {
        VIOLET1, VIOLET2, MINTY, AZURE, NOPE
    }

    public beaconColors beaconVal = beaconColors.NOPE;
    @Override protected int getLayoutResId() {
        return R.layout.activity_scanning;
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(this.getLayoutResId());
        beaconFound=0;
        // Configure device list.
        adapter = new BeaconListAdapter(this);
        final TextView textView4 = (TextView)findViewById(R.id.textView4);
        textView4.setText("My Awesome Text");
        // Configure BeaconManager.
        final List<Region> list = myBeacons.getKeyList();
        beaconManager = new BeaconManager(this);
        beaconManager.setBackgroundScanPeriod(TimeUnit.MILLISECONDS.toMillis(100), TimeUnit.MILLISECONDS.toMillis(100));
        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override public void onBeaconsDiscovered(Region region, final List<Beacon> beacons) {
                // Note that results are not delivered on UI thread.
                runOnUiThread(new Runnable() {
                    @Override public void run() {
                        // Note that beacons reported here are already sorted by estimated
                        // distance between device and beacon.
//                        toolbar.setSubtitle("Found beacons: " + beacons.size());
                        adapter.replaceWith(beacons);

                        for(int i=0; i<adapter.getCount(); i++) {
                            Region beacon = new Region("", adapter.getItem(i).getProximityUUID(),
                                    adapter.getItem(i).getMajor(), adapter.getItem(i).getMinor());


                            if (list.get(0).equals(beacon) && beaconFound==0) {
                                textView4.setText("Fiolet1");
                                beaconFound++;
                            } else if (list.get(1).equals(beacon) && beaconFound==1) {
                                textView4.setText("blue");
                                beaconFound++;
                            } else if (list.get(2).equals(beacon) && beaconFound==2) {
                                textView4.setText("Fiolet2");
                                beaconFound++;
                            } else if (list.get(3).equals(beacon) && beaconFound==3) {
                                textView4.setText("green");
                                beaconFound++;
                            }
                        }

                    }
                });
            }
        });
    }


    @Override protected void onDestroy() {
        beaconManager.disconnect();

        super.onDestroy();
    }

    @Override protected void onStart() {
        super.onStart();

        // Check if device supports Bluetooth Low Energy.
        if (!beaconManager.hasBluetooth()) {
            Toast.makeText(this, "Device does not have Bluetooth Low Energy", Toast.LENGTH_LONG).show();
            return;
        }

        // If Bluetooth is not enabled, let user enable it.
        if (!beaconManager.isBluetoothEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            connectToService();
        }
    }

    @Override protected void onStop() {
        beaconManager.stopRanging(ALL_ESTIMOTE_BEACONS_REGION);

        super.onStop();
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                connectToService();
            } else {
                Toast.makeText(this, "Bluetooth not enabled", Toast.LENGTH_LONG).show();
                toolbar.setSubtitle("Bluetooth not enabled");
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void connectToService() {
//        toolbar.setSubtitle("Scanning...");
        adapter.replaceWith(Collections.<Beacon>emptyList());
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override public void onServiceReady() {
                beaconManager.startRanging(ALL_ESTIMOTE_BEACONS_REGION);
            }
        });
    }

    private AdapterView.OnItemClickListener createOnItemClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (getIntent().getStringExtra(EXTRAS_TARGET_ACTIVITY) != null) {
                    try {
                        Class<?> clazz = Class.forName(getIntent().getStringExtra(EXTRAS_TARGET_ACTIVITY));
                        Intent intent = new Intent(ScanningActivity.this, clazz);
                        intent.putExtra(EXTRAS_BEACON, adapter.getItem(position));
                        startActivity(intent);
                    } catch (ClassNotFoundException e) {
                        Log.e(TAG, "Finding class by name failed", e);
                    }
                }
            }
        };
    }
}
