package com.jp.smartgpsapp.fragments;


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.jp.smartgpsapp.R;
import com.jp.smartgpsapp.helpers.Constants;
import com.jp.smartgpsapp.helpers.HandleProgressDialog;
import com.jp.smartgpsapp.helpers.SessionManager;
import com.jp.smartgpsapp.services.BluetoothService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 */
public class BtSettingFragment extends Fragment {
    private static final int REQUEST_ENABLE_BT=1;
    public static String EXTRA_DEVICE_ADDRESS = "device_address";
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 2;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 3;
    private static final String TAG = "BtSettingFragment";
    private boolean isDenied = true;
    private Button searchBtn, listDeviceBtn;
    private Switch btStateSwitch;
    private TextView btFlag;
    private ListView pairedListView, scannedListView;
    private BluetoothAdapter btAdapter;
    private Set<BluetoothDevice> pairedDevice; // paired devices is stored in set
    private ArrayAdapter<String> pairedBtArrayAdapter, scannedBtArrayAdapter;
    private HandleProgressDialog pDialog;
    private BluetoothService mBtService = null;
    private SessionManager session = null;
    private String mConnectedDeviceName = null;
    public BtSettingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bt_setting, container, false);
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState){
        String scanning = getResources().getText(R.string.scanning).toString();
        pDialog = new HandleProgressDialog(getActivity(), scanning);

        searchBtn = (Button) v.findViewById(R.id.searchBtn);
        listDeviceBtn = (Button) v.findViewById(R.id.listDeviceBtn);

        btStateSwitch = (Switch) v.findViewById(R.id.btStateSwitch);
        btFlag = (TextView) v.findViewById(R.id.btFlag);

        pairedListView = (ListView) v.findViewById(R.id.pairedDeviceLt);
        scannedListView = (ListView) v.findViewById(R.id.availableDeviceLt);
        // Find and set up the ListView for paired devices
        pairedBtArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
        pairedListView.setAdapter(pairedBtArrayAdapter);
        pairedListView.setOnItemClickListener(mDeviceClickListener);
        // Find and set up the ListView for newly discovered devices
        scannedBtArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
        scannedListView.setAdapter(scannedBtArrayAdapter);
        scannedListView.setOnItemClickListener(mDeviceClickListener);

        btAdapter = BluetoothAdapter.getDefaultAdapter();
        if(btAdapter == null ){
            btStateSwitch.setClickable(false);
            searchBtn.setEnabled(false);
            listDeviceBtn.setEnabled(false);
            Toast.makeText(getActivity().getApplicationContext(),
                    R.string.btNotAvai, Toast.LENGTH_LONG).show();
        }else{
            if(btAdapter.isEnabled()){
                btStateSwitch.setChecked(true);
                btFlag.setText("Status: On");
            }else{
                diableBtn();
                btFlag.setText("Status: Off");
            }
            btStateSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        enableBt();
                    }else{
                        disableBt();
                    }
                }
            });
            listDeviceBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listPairedDevice();
                }
            });

            searchBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doDiscovery();
                }
            });

        }

        mBtService = new BluetoothService(getActivity(), mHandler);
        session = new SessionManager(getActivity().getApplicationContext());
        // Register for broadcasts when a device is discovered
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        getActivity().registerReceiver(mReceiver, filter);

        // Register for broadcasts when discovery has finished
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        getActivity().registerReceiver(mReceiver, filter);
    }// end onViewCreated()

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Make sure we're not doing discovery anymore
        if (btAdapter != null) {
            btAdapter.cancelDiscovery();
        }

        // Unregister broadcast listeners
        getActivity().unregisterReceiver(mReceiver);
    }

    /*
    When the user is done with the subsequent activity and returns,
    the system calls your activity's onActivityResult() method.
    In this case, the user decide if the app access the bluetooth.
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == REQUEST_ENABLE_BT){
            if (resultCode == Activity.RESULT_OK){
                btFlag.setText("Status: On");

            }else{
                btFlag.setText("Status: Off");
                btStateSwitch.setChecked(false);
                diableBtn();
                isDenied = true;
                Toast.makeText(getActivity().getApplicationContext(),
                        R.string.btDeny, Toast.LENGTH_LONG).show();
            }

        }
    }

    /*
    This method is to enable the bluetooth
     */
    private  void enableBt(){
        if(!btAdapter.isEnabled()){
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            btFlag.setText("Status: On");
            searchBtn.setEnabled(true);
            listDeviceBtn.setEnabled(true);
            if(!isDenied){
                Toast.makeText(getActivity().getApplicationContext(),
                        R.string.btOn, Toast.LENGTH_LONG).show();
            }
        }
    }
    /*
    This method is to disable the bluetooth
     */
    private void disableBt(){
        btAdapter.disable();
        btFlag.setText("Status: Off");
        diableBtn();
        if(!isDenied){
            Toast.makeText(getActivity().getApplicationContext(),
                    R.string.btOff, Toast.LENGTH_LONG).show();
        }
    }
    /*
    This method is to list the paired devices
     */
    private void listPairedDevice(){
        pairedDevice = btAdapter.getBondedDevices();
        final List<BluetoothDevice> btList = new ArrayList<BluetoothDevice>(pairedDevice);
        if(pairedDevice.size() > 0){
            pairedBtArrayAdapter.clear();
            for(BluetoothDevice device : pairedDevice){
                pairedBtArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        }
        Toast.makeText(getActivity().getApplicationContext(),
                R.string.displayPaired, Toast.LENGTH_LONG).show();
    }

    private void doDiscovery(){
        pDialog.showDialog();
        // If we're already discovering, stop it
        if (btAdapter.isDiscovering()) {
            btAdapter.cancelDiscovery();
        }

        // Request discover from BluetoothAdapter
        btAdapter.startDiscovery();
    }

    private void diableBtn(){
        searchBtn.setEnabled(false);
        listDeviceBtn.setEnabled(false);
        scannedBtArrayAdapter.clear();
        pairedBtArrayAdapter.clear();
    }


    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // If it's already paired, skip it, because it's been listed already
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    scannedBtArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                }
                // When discovery is finished, change the Activity title
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                pDialog.hideDialog();
                Toast.makeText(getActivity().getApplicationContext(),
                        "Scanning completed", Toast.LENGTH_LONG).show();
                if (scannedBtArrayAdapter.getCount() == 0) {
                    String noDevices = getResources().getText(R.string.none_found).toString();
                    scannedBtArrayAdapter.add(noDevices);
                }
            }
        }
    };

    private final Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            FragmentActivity activity = getActivity();
            switch (msg.what){
                case Constants.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
                    if (null != activity) {
                        Toast.makeText(activity, "Connected to "
                                + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Constants.MESSAGE_TOAST:
                    if (null != activity) {
                        Toast.makeText(activity, msg.getData().getString(Constants.TOAST),
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }

    };

    private AdapterView.OnItemClickListener mDeviceClickListener =
            new AdapterView.OnItemClickListener(){

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    btAdapter.cancelDiscovery();

                    // Get the device MAC address, which is the last 17 chars in the View
                    String info = ((TextView) view).getText().toString();
                    String address = info.substring(info.length() - 17);
                    if(!session.isConnected()){
                        connectDevice(address, true);
                        if(mBtService.getState() == BluetoothService.STATE_CONNECTED ){
                            session.setConnected(true);
                        }
                    }
                }
            };
    private void connectDevice(String address, boolean secure) {
        // Get the BluetoothDevice object
        BluetoothDevice device = btAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        mBtService.connect(device, secure);
    }

}
