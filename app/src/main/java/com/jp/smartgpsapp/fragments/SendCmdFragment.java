package com.jp.smartgpsapp.fragments;


import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jp.smartgpsapp.R;
import com.jp.smartgpsapp.activities.MainMenuActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SendCmdFragment extends Fragment {
    private BluetoothAdapter btAdapter;


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
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        if(!btAdapter.isEnabled()){
            //displayDialog();
        }
    }

/*
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

                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(SendCmdFragment.class, btSettingFragment);
                        fragmentTransaction.commit();

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
*/


}
