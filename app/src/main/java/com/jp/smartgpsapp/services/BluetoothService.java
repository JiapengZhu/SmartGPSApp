package com.jp.smartgpsapp.services;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.widget.ArrayAdapter;

import java.util.Set;

/**
 * Created by Zhu on 2015-12-28.
 */
public class BluetoothService {
    private static final int REQUEST_ENABLE_BT=1;
    private BluetoothAdapter btAdapter;
    private Set<BluetoothDevice> pairedDevice; // paired devices is stored in set

    public BluetoothService(){
        btAdapter = BluetoothAdapter.getDefaultAdapter();

    }





}
