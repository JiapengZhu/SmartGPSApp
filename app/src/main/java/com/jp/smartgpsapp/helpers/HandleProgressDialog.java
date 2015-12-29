package com.jp.smartgpsapp.helpers;

import android.app.ProgressDialog;
import android.content.Context;

import com.jp.smartgpsapp.R;

/**
 * Created by Zhu on 2015-12-28.
 */
public class HandleProgressDialog {
    private ProgressDialog pDialog;

    public HandleProgressDialog(Context context, String msg){
        pDialog = new ProgressDialog(context);
        pDialog.setCancelable(false);
        pDialog.setMessage(msg);
    }

    public void showDialog(){
        if (!pDialog.isShowing())
            pDialog.show();
    }
    public void hideDialog(){
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}
