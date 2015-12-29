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

import java.util.ArrayList;
import java.util.List;

public class MainMenuActivity extends FragmentActivity implements ActionBar.TabListener{
    private ViewPager mViewPager;
    private ActionBar actionBar;

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
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new BtSettingFragment(), "Bluetooth Setting");
        adapter.addFragment(new RoutingFragment(), "Routing");
        adapter.addFragment(new SearchRouteFragment(), "Search");
        adapter.addFragment(new SendCmdFragment(), "Send Command");
        adapter.addFragment(new MyAccountFragment(), "My Account");

        for(int i = 0; i < adapter.getCount(); i++){
            actionBar.addTab(actionBar.newTab().setText(adapter.getPageTitle(i)).setTabListener(this));
        }
        viewPager.setAdapter(adapter);
    }
}
