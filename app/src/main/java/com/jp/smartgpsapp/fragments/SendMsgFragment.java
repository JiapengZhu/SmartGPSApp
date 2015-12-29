package com.jp.smartgpsapp.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jp.smartgpsapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SendMsgFragment extends Fragment {


    public SendMsgFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_send_msg, container, false);
    }


}
