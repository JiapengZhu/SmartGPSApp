package com.jp.smartgpsapp.activities;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.jp.smartgpsapp.R;
import com.jp.smartgpsapp.adapters.ViewPagerAdapter;
import com.jp.smartgpsapp.fragments.BtSettingFragment;
import com.jp.smartgpsapp.fragments.MyAccountFragment;
import com.jp.smartgpsapp.fragments.RoutingFragment;
import com.jp.smartgpsapp.fragments.SearchRouteFragment;
import com.jp.smartgpsapp.fragments.SendCmdFragment;
import com.jp.smartgpsapp.helpers.Constants;
import com.jp.smartgpsapp.helpers.SessionManager;
import com.jp.smartgpsapp.services.BluetoothService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainMenuActivity extends FragmentActivity implements ActionBar.TabListener{
    private static ViewPager mViewPager;
    private ActionBar actionBar;
    private SessionManager session;



    private BluetoothService btService;
    private ViewPagerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        actionBar = getActionBar();

        mViewPager = (ViewPager) findViewById(R.id.pager);
        setupViewPager(mViewPager);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When swiping between different app sections, select the corresponding tab.
                // We can also use ActionBar.Tab#select() to do this if we have a reference to the
                // Tab.
                actionBar.setSelectedNavigationItem(position);
            }
        });
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        session = new SessionManager(this.getApplicationContext());
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    private void setupViewPager(ViewPager viewPager) {
        //ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new BtSettingFragment(), "Bluetooth Setting");
        adapter.addFragment(new RoutingFragment(), "Routing");
        adapter.addFragment(new SearchRouteFragment(), "Search");
        adapter.addFragment(new SendCmdFragment(), "Send Command");
        adapter.addFragment(new MyAccountFragment(), "My Account");
        this.adapter = adapter;
        for(int i = 0; i < adapter.getCount(); i++){
            actionBar.addTab(actionBar.newTab().setText(adapter.getPageTitle(i)).setTabListener(this));
        }
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        session.setConnected(false);
    }

    public static ViewPager getmViewPager(){
        return mViewPager;
    }

    public BluetoothService getBtService() {
        BtSettingFragment btSettingFragment = (BtSettingFragment)adapter.getItem(Constants.BT_SETTING_FRAGMENT);
        btService = btSettingFragment.getmBtService();
        return btService;
    }

   /* @Override
    public void onDataPass(BluetoothService btService) {
        //this.btService = btService;
        Bundle bundle = new Bundle();
        bundle.putSerializable("btService", btService);
        Fragment sendCmdFragment = new SendCmdFragment();
       // Fragment routingFragment = new RoutingFragment();
        sendCmdFragment = adapter.getItem(Constants.SEND_COMMAND_FRAGMENT);
        sendCmdFragment.setArguments(bundle);
       // routingFragment.setArguments(bundle);
        *//*bundle.putBoolean("isConnected", data);
        // set Fragmentclass Arguments
        Fragment sendCmdFragment = adapter.getItem(Constants.SEND_COMMAND_FRAGMENT);
        Fragment routingFragment = adapter.getItem(Constants.ROUTING_FRAGMENT);
        sendCmdFragment.setArguments(bundle);*//*
        //routingFragment.setArguments(bundle);
    }*/
}
