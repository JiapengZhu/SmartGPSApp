package com.jp.smartgpsapp.fragments;


import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jp.smartgpsapp.R;
import com.jp.smartgpsapp.activities.MainMenuActivity;
import com.jp.smartgpsapp.activities.SendSMS;
import com.jp.smartgpsapp.helpers.SessionManager;
import com.jp.smartgpsapp.services.BluetoothService;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SendCmdFragment extends Fragment {
    private BluetoothService mBtService = null;
    private BluetoothAdapter btAdapter;
    private Button sendCmdBtn, sendSmsBtn;
    private ListView cmdListView;
    private ArrayAdapter<String> listAdapter;
    private String command;
    private SessionManager session;
    public SendCmdFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_send_cmd, container, false);
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState){
        session = new SessionManager(getActivity().getApplicationContext());
        sendCmdBtn = (Button) v.findViewById(R.id.sendCmdBtn);
        sendSmsBtn = (Button) v.findViewById(R.id.sendSmsBtn);
        cmdListView = (ListView) v.findViewById(R.id.cmdListView);
        command = null;
        setupListView(listAdapter);
        cmdListView.setOnItemClickListener(mCmdClickListener);
        sendCmdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCommand(command);
            }
        });
        sendSmsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SendSMS.class);
                startActivity(intent);

            }
        });
        mBtService = new BluetoothService(getActivity(), handler);
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        if(!btAdapter.isEnabled()){
            displayDialog();
            sendCmdBtn.setClickable(false);
            sendSmsBtn.setClickable(false);
            cmdListView.setEnabled(false);
        }
    }


    private void displayDialog(){
        String title = "Turn On Bluetooth";
        String msg = "You need to turn on the bluetooth. Do you want to turn on it now?";
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        builder.setMessage(msg);
        // Add the "OK" buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainMenuActivity.getmViewPager().setCurrentItem(0);
                    }
                }
        );
        // Add the "Cancel" button
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                //txtView.setText("Status: cancel");
            }
        });

        builder.create().show();
    }

    private void setupListView(ArrayAdapter<String> arrAdapter){
        List<String> listViewContentLt = new ArrayList<String>();
        String cmd1 = getResources().getText(R.string.cmd1).toString();
        String cmd2 = getResources().getText(R.string.cmd2).toString();
        String cmd3 = getResources().getText(R.string.cmd3).toString();
        listViewContentLt.add(cmd1);
        listViewContentLt.add(cmd2);
        listViewContentLt.add(cmd3);
        arrAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.listview_values,listViewContentLt);
        cmdListView.setAdapter(arrAdapter);
    }

    private void sendCommand(String cmd){
        if(cmd != null && cmd != ""){
            sendMessage(cmd);
        }else{
            Toast.makeText(getActivity(), R.string.not_select_cmd, Toast.LENGTH_SHORT).show();
        }

    }

    private AdapterView.OnItemClickListener mCmdClickListener =
            new AdapterView.OnItemClickListener(){

        @Override
        public void onItemClick(AdapterView<?> parent, View view,
                                int position, long id) {
            command = ((TextView) view).getText().toString();
        }
    };

    private final Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            FragmentActivity activity = getActivity();
            switch (msg.what){
                
            }
        }
    };
    private void sendMessage(String message) {
        // Check that we're actually connected before trying anything
        /*if (mBtService.getState() != BluetoothService.STATE_CONNECTED) {
            Toast.makeText(getActivity(), R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }*/
        if(!session.isConnected()){
            Toast.makeText(getActivity(), R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            mBtService.write(send);

        }
    }




}
