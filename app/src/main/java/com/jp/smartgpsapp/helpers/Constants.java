package com.jp.smartgpsapp.helpers;

/**
 * Created by Zhu on 2015-12-30.
 */
public class Constants {
    // Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    // Index of fragment in view pager

    public static final int BT_SETTING_FRAGMENT = 0;
    public static final int ROUTING_FRAGMENT = 1;
    public static final int SEARCH_ROUTING_FRAGMENT = 2;
    public static final int SEND_COMMAND_FRAGMENT = 3;
    public static final int ACCOUNT_FRAGMENT = 4;
}
