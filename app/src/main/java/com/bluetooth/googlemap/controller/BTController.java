package com.bluetooth.googlemap.controller;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.bluetooth.googlemap.modals.Device;

import java.util.ArrayList;
import java.util.Observable;


public class BTController extends Observable {
    private static final String TAG = "BTC";
    private static final int REQUEST_ENABLE_BT = 1;
    private BluetoothAdapter bluetoothAdapter;


    private ArrayList<Device> devices = null;

    private BroadcastReceiver bluetoothReceiver = new BroadcastReceiver() {
        @SuppressLint("WrongConstant")
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive: Execute");
            String action = intent.getAction();
            switch (action) {
                case BluetoothDevice.ACTION_FOUND:
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    String deviceName = device.getName();
                    boolean paired = device.getBondState() == BluetoothDevice.BOND_BONDED;
                    String deviceAddress = device.getAddress();
                    short deviceRSSI = intent.getExtras().getShort(BluetoothDevice.EXTRA_RSSI, (short) 0);

                    // TODO fournir les autres informations
                    Device d = new Device(deviceAddress);
                    d.setName(deviceName);
                    d.setBtClass(device.getBluetoothClass());
                    d.setSignal(deviceRSSI);
                    Log.d(TAG, deviceAddress);

                    devices.add(d);
                    break;
                case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                    Log.d(TAG,"Discovery finished");
                    setChanged();
                    notifyObservers(devices);
                    break;
                case BluetoothAdapter.ACTION_DISCOVERY_STARTED:
                    Log.d(TAG,"Discovery started");
                    devices = new ArrayList<>();
                    break;
                case BluetoothAdapter.ACTION_STATE_CHANGED:
                    Log.d(TAG,"State changed: "+ bluetoothAdapter.getState());
                    break;
                default:
                    break;
            }
        }
    };
    private boolean hasBT = false;


    public void registerReceiver(Activity activity){
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        activity.registerReceiver(bluetoothReceiver, filter);
    }

    public void unregisterReceiver(Activity activity){
        activity.unregisterReceiver(bluetoothReceiver);
    }

    public boolean enable(Activity activity) {
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            this.hasBT = false;
            return false;
        }

        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        this.hasBT = true;
        this.registerReceiver(activity);
        return this.isEnabled();
    }

    private boolean isEnabled(){
        if (!this.hasBT) return false;
        return (this.bluetoothAdapter.getState() == BluetoothAdapter.STATE_ON);
    }

    public boolean performScan() {
        if (!this.isEnabled()) return false;
        if (this.bluetoothAdapter.isDiscovering())
            this.bluetoothAdapter.cancelDiscovery();
        return this.bluetoothAdapter.startDiscovery();
    }
}
