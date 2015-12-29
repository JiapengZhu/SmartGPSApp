package com.jp.smartgpsapp.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.jp.smartgpsapp.activities.MainActivity;
import com.jp.smartgpsapp.R;
import com.jp.smartgpsapp.helpers.SQLiteHandler;
import com.jp.smartgpsapp.helpers.SessionManager;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyAccountFragment extends Fragment {
    private Button editProfileBtn, settingBtn, logoutBtn;
    private TextView callsignTxt, userNameTxt;
    private SessionManager session;
    private SQLiteHandler db;
    private HashMap<String, String> user;

    public MyAccountFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        session = new SessionManager(getActivity().getApplicationContext());
        db = new SQLiteHandler(getActivity().getApplicationContext());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_account, container, false);
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState){
        super.onViewCreated(v, savedInstanceState);
        editProfileBtn = (Button) v.findViewById(R.id.editProfileBtn);
        settingBtn = (Button) v.findViewById(R.id.settingBtn);
        logoutBtn = (Button) v.findViewById(R.id.logoutBtn);

        callsignTxt = (TextView) v.findViewById(R.id.callsignTxt);
        userNameTxt = (TextView) v.findViewById(R.id.userNameTxt);

        if (!session.isLoggedIn()) {
            logout();
        }
        // Fetching user details from sqlite
        user = db.getUserDetails();
        String callsign = user.get("callsign");
        String username = user.get("username");

        System.out.println("callsign is "+callsign+"\tusername is "+username);

        // Displaying the user details on the screen
        userNameTxt.setText(username);
        callsignTxt.setText(callsign);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void logout(){
        session.setLogin(false);
        db.deleteUsers();
        Intent intent = new Intent(getActivity(), MainActivity.class);
        getActivity().startActivity(intent);
        getActivity().finish();
    }

}
